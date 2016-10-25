package com.github.malkomich.nanodegree.ui.fragment;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.malkomich.nanodegree.R;
import com.github.malkomich.nanodegree.adapter.VideoAdapter;
import com.github.malkomich.nanodegree.data.database.MovieContract;
import com.github.malkomich.nanodegree.data.webservice.HttpClientGenerator;
import com.github.malkomich.nanodegree.domain.Video;
import com.github.malkomich.nanodegree.util.MathUtils;
import com.github.malkomich.nanodegree.data.webservice.MovieService;
import com.github.malkomich.nanodegree.domain.VideoResults;
import com.squareup.picasso.Picasso;

import org.joda.time.LocalDate;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Movie details view.
 */
public class MovieDetailsFragment extends Fragment implements Callback<VideoResults>,
    LoaderManager.LoaderCallbacks<Cursor> {

    public static final String DETAILS_URI = "uri";

    // Index for the projected columns of Movie's table.
    public static final int COL_MOVIE_ID = 0;
    public static final int COL_MOVIE_API_ID = 1;
    public static final int COL_MOVIE_TITLE = 2;
    public static final int COL_MOVIE_DESCRIPTION = 3;
    public static final int COL_MOVIE_DATE = 4;
    public static final int COL_MOVIE_POSTER_PATH = 5;
    public static final int COL_MOVIE_POPULARITY = 6;
    public static final int COL_MOVIE_VOTE_COUNT = 7;
    public static final int COL_MOVIE_VOTE_AVERAGE = 8;
    public static final int COL_VIDEO_ID = 9;
    public static final int COL_VIDEO_API_ID = 10;
    public static final int COL_VIDEO_KEY = 11;
    public static final int COL_VIDEO_TYPE = 12;
    public static final int COL_VIDEO_SITE = 13;

    private static final String TAG = MovieDetailsFragment.class.getName();
    private static final int DETAILS_LOADER = 1;

    // Projection for Movie's query.
    private static final String[] DETAILS_PROJECTION = {
        MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry._ID + " AS movieId",
        MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry.COL_API_ID,
        MovieContract.MovieEntry.COL_TITLE,
        MovieContract.MovieEntry.COL_DESCRIPTION,
        MovieContract.MovieEntry.COL_DATE,
        MovieContract.MovieEntry.COL_POSTER_PATH,
        MovieContract.MovieEntry.COL_POPULARITY,
        MovieContract.MovieEntry.COL_VOTE_COUNT,
        MovieContract.MovieEntry.COL_VOTE_AVERAGE,
        MovieContract.VideoEntry.TABLE_NAME + "." + MovieContract.VideoEntry._ID,
        MovieContract.VideoEntry.TABLE_NAME + "." + MovieContract.VideoEntry.COL_API_ID,
        MovieContract.VideoEntry.COL_KEY,
        MovieContract.VideoEntry.COL_TYPE,
        MovieContract.VideoEntry.COL_SITE,
    };

    private Uri mUri;
    private VideoAdapter videoAdapter;

    @BindView(R.id.movie_image) protected ImageView imageView;
    @BindView(R.id.movie_title) protected TextView titleView;
    @BindView(R.id.movie_description) protected TextView descriptionView;
    @BindView(R.id.movie_popularity) protected TextView popularityView;
    @BindView(R.id.movie_rate) protected TextView rateView;
    @BindView(R.id.movie_date) protected TextView dateView;
    @BindView(R.id.trailer_list) protected ListView trailerListView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movie_details, container, false);
        ButterKnife.bind(this, view);

        videoAdapter = new VideoAdapter(getContext(), null, 0);
        trailerListView.setAdapter(videoAdapter);
        trailerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                String site = cursor.getString(COL_VIDEO_SITE);
                if(Video.SITE_YOUTUBE.equals(site)) {
                    String key = cursor.getString(COL_VIDEO_KEY);
                    startActivity(new Intent(Intent.ACTION_VIEW, Video.getUri(key)));
                } else {
                    Toast.makeText(getContext(), getString(R.string.video_site_invalid, site), Toast.LENGTH_SHORT)
                        .show();
                }
            }
        });

        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(DETAILS_URI);
        }

        if(savedInstanceState != null && savedInstanceState.containsKey(DETAILS_URI)) {
            mUri = (Uri) savedInstanceState.get(DETAILS_URI);
        }

        return view;
    }

    public void updateMovie(Bundle args) {
        mUri = args.getParcelable(DETAILS_URI);

        LoaderManager manager = getLoaderManager();
        if(manager.getLoader(DETAILS_LOADER) == null) {
            manager.initLoader(DETAILS_LOADER, args, this);
        } else {
            manager.restartLoader(DETAILS_LOADER, args, this);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(mUri != null) {
            getLoaderManager().initLoader(DETAILS_LOADER, null, this);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(DETAILS_URI, mUri);
    }

    @Override
    public void onResponse(Call<VideoResults> call, Response<VideoResults> response) {

        if(response.isSuccessful()) {
            VideoResults videoResults = response.body();

            long movieId = MovieContract.MovieEntry.getMovieIdFromUri(mUri);

            int size = videoResults.getVideos().size();
            ContentValues[] valuesArray = new ContentValues[size];
            for(int i=0; i < size; i++) {
                ContentValues values = new ContentValues();
                Video video = videoResults.getVideos().get(i);
                values.put(MovieContract.VideoEntry.COL_API_ID, video.getId());
                values.put(MovieContract.VideoEntry.COL_MOVIE_ID, movieId);
                values.put(MovieContract.VideoEntry.COL_KEY, video.getKey());
                values.put(MovieContract.VideoEntry.COL_TYPE, video.getType().getName());
                values.put(MovieContract.VideoEntry.COL_SITE, video.getSite());
                valuesArray[i] = values;
            }
            getContext().getContentResolver().bulkInsert(MovieContract.VideoEntry.CONTENT_URI, valuesArray);
        }
    }

    @Override
    public void onFailure(Call<VideoResults> call, Throwable t) {
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
            getContext(),
            mUri,
            DETAILS_PROJECTION,
            null,
            null,
            MovieContract.VideoEntry.COL_TYPE
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "onLoadFinished");
        if(data != null) {
            updateUI(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(TAG, "onLoaderReset");
        videoAdapter.swapCursor(null);
    }

    /**
     * Refresh details of movie from web service.
     */
    private void refreshData(long movieId) {

        String apiKey = getString(R.string.tmdbApiKey);

        // HTTP Client initialization
        MovieService service = HttpClientGenerator.createService(
            MovieService.class,
            MovieService.BASE_URL
        );

        service.getMovieVideos(movieId, apiKey).enqueue(this);
    }

    /*
     * Update movie details UI components.
     */
    private void updateUI(Cursor data) {

        Log.d(TAG, "updateUI() --> Cursor.getCount(): " + data.getCount());
        if(!data.moveToFirst()) {
            return;
        }

        if(titleView.getText() == null || titleView.getText().length() == 0) {
            String title = data.getString(COL_MOVIE_TITLE);
            String posterPath = data.getString(COL_MOVIE_POSTER_PATH);
            String description = data.getString(COL_MOVIE_DESCRIPTION);
            String dateString = data.getString(COL_MOVIE_DATE);
            double popularity = data.getDouble(COL_MOVIE_POPULARITY);
            double voteAverage = data.getDouble(COL_MOVIE_VOTE_AVERAGE);

            titleView.setText(title);
            Picasso.with(getContext()).load(posterPath).into(imageView);
            descriptionView.setText(description);
            popularityView.setText(
                String.valueOf(MathUtils.roundDouble(popularity, 2))
            );
            rateView.setText(
                String.valueOf(MathUtils.roundDouble(voteAverage, 2))
            );

            LocalDate date = new LocalDate(dateString);
            dateView.setText(getString(R.string.date,
                date.getDayOfMonth(),
                date.toString("MMMM"),
                date.getYear())
            );
        }

        // FIXING...
//        if(data.getString(COL_VIDEO_KEY) != null) {
//            videoAdapter.swapCursor(data);
//        } else {
//            refreshData(data.getLong(COL_MOVIE_API_ID));
//        }
    }

}
