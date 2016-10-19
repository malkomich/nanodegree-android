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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.malkomich.nanodegree.R;
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

    public static final String URI = "uri";

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

    // Projection for Movie's query.
    private static final String[] DETAILS_PROJECTION = {
        MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry._ID,
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

    private static final String TAG = MovieDetailsFragment.class.getName();
    private static final int DETAILS_LOADER = 1;

    private Uri mUri;

    @BindView(R.id.movie_image) protected ImageView imageView;
    @BindView(R.id.movie_title) protected TextView titleView;
    @BindView(R.id.movie_description) protected TextView descriptionView;
    @BindView(R.id.movie_popularity) protected TextView popularityView;
    @BindView(R.id.movie_rate) protected TextView rateView;
    @BindView(R.id.movie_date) protected TextView dateView;
    @BindView(R.id.movie_trailer_button) protected Button trailerButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            mUri = savedInstanceState.getParcelable(URI);
        }

        View view = inflater.inflate(R.layout.fragment_movie_details, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    public void updateMovie(Bundle args) {
        getLoaderManager().restartLoader(DETAILS_LOADER, args, this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(DETAILS_LOADER, getArguments(), this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(URI, mUri);
    }

    @Override
    public void onResponse(Call<VideoResults> call, Response<VideoResults> response) {

        if(response.isSuccessful()) {
            VideoResults videoResults = response.body();

            int size = videoResults.getVideos().size();
            ContentValues[] valuesArray = new ContentValues[size];
            for(int i=0; i < size; i++) {
                ContentValues values = new ContentValues();
                Video video = videoResults.getVideos().get(i);
                values.put(MovieContract.VideoEntry.COL_API_ID, video.getId());
                values.put(MovieContract.VideoEntry.COL_MOVIE_ID, videoResults.getMovieId());
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

        if(args == null || !args.containsKey(URI)) {
            return null;
        }

        Uri uri = args.getParcelable(URI);
        return new CursorLoader(
            getContext(),
            uri,
            DETAILS_PROJECTION,
            null,
            null,
            MovieContract.VideoEntry.COL_TYPE
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(loader.isStarted()) {
            updateUI(data);
        }
    }

    private void updateTrailerUI(Cursor data) {

        // Temporal solution for a unique video
        Video trailer = null;

        for(int i = 0; i < data.getCount(); i++, data.moveToFirst()) {
            if("Trailer".equals(data.getString(COL_VIDEO_TYPE)) &&
                    "YouTube".equals(data.getString(COL_VIDEO_SITE))) {
                trailer = new Video(
                    data.getString(COL_VIDEO_API_ID),
                    data.getString(COL_VIDEO_KEY),
                    data.getString(COL_VIDEO_TYPE),
                    data.getString(COL_VIDEO_SITE)
                );
            }
        }

        final Uri trailerLink = trailer != null ? trailer.getUri() : null;
        if(trailerLink != null) {
            trailerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Intent.ACTION_VIEW, trailerLink));
                }
            });
            Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
            trailerButton.setVisibility(View.VISIBLE);
            trailerButton.setAnimation(anim);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    /**
     * Refresh details of movie from web service.
     */
    private void refreshData(int movieId) {

        String apiKey = getString(R.string.tmdbApiKey);

        // HTTP Client initialization
        MovieService service = HttpClientGenerator.createService(
            MovieService.class,
            MovieService.BASE_URL
        );

        service.getMovieVideos(movieId, apiKey).enqueue(this);
    }

    private void updateUI(Cursor data) {

        Log.d(TAG, "updateUI() --> Cursor.getCount(): " + data.getCount());
        if(!data.moveToFirst()) {
            return;
        }
        String key = data.getString(COL_VIDEO_KEY);

        String posterPath = data.getString(COL_MOVIE_POSTER_PATH);
        String title = data.getString(COL_MOVIE_TITLE);
        String description = data.getString(COL_MOVIE_DESCRIPTION);
        String dateString = data.getString(COL_MOVIE_DATE);
        double popularity = data.getDouble(COL_MOVIE_POPULARITY);
        double voteAverage = data.getDouble(COL_MOVIE_VOTE_AVERAGE);

        Picasso.with(getContext()).load(posterPath).into(imageView);
        titleView.setText(title);
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

        if(data.getString(COL_VIDEO_KEY) != null) {
            updateTrailerUI(data);
        } else {
            refreshData(data.getInt(COL_MOVIE_API_ID));
        }

    }

}
