package com.github.malkomich.nanodegree.ui.fragment;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dgreenhalgh.android.simpleitemdecoration.linear.DividerItemDecoration;
import com.github.malkomich.nanodegree.R;
import com.github.malkomich.nanodegree.adapter.ReviewAdapter;
import com.github.malkomich.nanodegree.adapter.VideoAdapter;
import com.github.malkomich.nanodegree.data.database.MovieContract;
import com.github.malkomich.nanodegree.domain.Movie;
import com.github.malkomich.nanodegree.domain.Review;
import com.github.malkomich.nanodegree.domain.ReviewResults;
import com.github.malkomich.nanodegree.domain.Video;
import com.github.malkomich.nanodegree.presenter.MovieDetailsPresenter;
import com.github.malkomich.nanodegree.ui.view.MovieDetailsView;
import com.github.malkomich.nanodegree.util.MathUtils;
import com.github.malkomich.nanodegree.domain.VideoResults;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Movie details view.
 */
public class MovieDetailsFragment extends Fragment implements MovieDetailsView,
    LoaderManager.LoaderCallbacks<Cursor> {

    public static final String DETAILS_VIDEO_URI = "video_uri";
    public static final String DETAILS_REVIEW_URI = "review_uri";

    // Index for the projected columns of Movie's table.
    private static final int COL_MOVIE_ID = 0;
    private static final int COL_MOVIE_API_ID = 1;
    private static final int COL_MOVIE_TITLE = 2;
    private static final int COL_MOVIE_DESCRIPTION = 3;
    private static final int COL_MOVIE_DATE = 4;
    private static final int COL_MOVIE_POSTER_PATH = 5;
    private static final int COL_MOVIE_POPULARITY = 6;
    private static final int COL_MOVIE_VOTE_COUNT = 7;
    private static final int COL_MOVIE_VOTE_AVERAGE = 8;
    private static final int COL_MOVIE_FAVORITE = 9;
    private static final int COL_MOVIE_UPDATE_DATE = 10;

    public static final int COL_VIDEO_ID = 11;
    public static final int COL_VIDEO_API_ID = 12;
    public static final int COL_VIDEO_KEY = 13;
    public static final int COL_VIDEO_TYPE = 14;
    public static final int COL_VIDEO_SITE = 15;

    public static final int COL_REVIEW_ID = 11;
    public static final int COL_REVIEW_API_ID = 12;
    public static final int COL_REVIEW_AUTHOR = 13;
    public static final int COL_REVIEW_CONTENT = 14;
    public static final int COL_REVIEW_URL = 15;

    private static final String TAG = MovieDetailsFragment.class.getName();
    private static final int DETAILS_VIDEO_LOADER = 1;
    private static final int DETAILS_REVIEW_LOADER = 2;

    // Projections for Movie's query.
    private static final String[] DETAILS_VIDEO_PROJECTION = {
        MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry._ID + " AS movieId",
        MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry.COL_API_ID,
        MovieContract.MovieEntry.COL_TITLE,
        MovieContract.MovieEntry.COL_DESCRIPTION,
        MovieContract.MovieEntry.COL_DATE,
        MovieContract.MovieEntry.COL_POSTER_PATH,
        MovieContract.MovieEntry.COL_POPULARITY,
        MovieContract.MovieEntry.COL_VOTE_COUNT,
        MovieContract.MovieEntry.COL_VOTE_AVERAGE,
        MovieContract.MovieEntry.COL_FAVORITE,
        MovieContract.MovieEntry.COL_UPDATE_DATE,
        MovieContract.VideoEntry.TABLE_NAME + "." + MovieContract.VideoEntry._ID,
        MovieContract.VideoEntry.TABLE_NAME + "." + MovieContract.VideoEntry.COL_API_ID,
        MovieContract.VideoEntry.COL_KEY,
        MovieContract.VideoEntry.COL_TYPE,
        MovieContract.VideoEntry.COL_SITE,
    };
    private static final String[] DETAILS_REVIEW_PROJECTION = {
        MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry._ID + " AS movieId",
        MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry.COL_API_ID,
        MovieContract.MovieEntry.COL_TITLE,
        MovieContract.MovieEntry.COL_DESCRIPTION,
        MovieContract.MovieEntry.COL_DATE,
        MovieContract.MovieEntry.COL_POSTER_PATH,
        MovieContract.MovieEntry.COL_POPULARITY,
        MovieContract.MovieEntry.COL_VOTE_COUNT,
        MovieContract.MovieEntry.COL_VOTE_AVERAGE,
        MovieContract.MovieEntry.COL_FAVORITE,
        MovieContract.MovieEntry.COL_UPDATE_DATE,
        MovieContract.ReviewEntry.TABLE_NAME + "." + MovieContract.ReviewEntry._ID,
        MovieContract.ReviewEntry.TABLE_NAME + "." + MovieContract.ReviewEntry.COL_API_ID,
        MovieContract.ReviewEntry.COL_AUTHOR,
        MovieContract.ReviewEntry.COL_CONTENT,
        MovieContract.ReviewEntry.COL_URL
    };

    private MovieDetailsPresenter mPresenter;
    private Uri mVideoUri;
    private Uri mReviewUri;
    private VideoAdapter videoAdapter;
    private ReviewAdapter reviewAdapter;
    private boolean favorite;

    @BindView(R.id.details_layout) protected LinearLayout detailsView;
    @BindView(R.id.empty_view_layout) protected RelativeLayout emptyView;
    @BindView(R.id.movie_title) protected TextView titleView;
    @BindView(R.id.favorite_icon) protected ImageView favoriteIcon;
    @BindView(R.id.movie_image) protected ImageView imageView;
    @BindView(R.id.movie_date) protected TextView dateView;
    @BindView(R.id.movie_popularity) protected TextView popularityView;
    @BindView(R.id.movie_rate) protected TextView rateView;
    @BindView(R.id.movie_description) protected TextView descriptionView;
    @BindView(R.id.trailer_list) protected RecyclerView trailerList;
    @BindView(R.id.review_list) protected RecyclerView reviewList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new MovieDetailsPresenter(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getString(R.string.title_fragment_details_view));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movie_details, container, false);
        ButterKnife.bind(this, view);

        favoriteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Force to update UI in next Loader sync
                mPresenter.setViewUpdated(false);

                ((ImageView) v).getDrawable();
                ContentValues values = new ContentValues();
                values.put(MovieContract.MovieEntry.COL_FAVORITE, !favorite);
                getContext().getContentResolver().update(
                    MovieContract.MovieEntry.CONTENT_URI,
                    values,
                    MovieContract.MovieEntry._ID + "=?",
                    new String[]{String.valueOf(MovieContract.MovieEntry.getMovieIdFromUri(mVideoUri))}
                );
            }
        });

        videoAdapter = new VideoAdapter(getContext());
        trailerList.setLayoutManager(new LinearLayoutManager(getContext()));
        trailerList.setAdapter(videoAdapter);
        Drawable horizontalDivider = ContextCompat.getDrawable(getContext(), R.drawable.divider);
        trailerList.addItemDecoration(new DividerItemDecoration(horizontalDivider));
        trailerList.setNestedScrollingEnabled(false);

        reviewAdapter = new ReviewAdapter(getContext());
        reviewList.setLayoutManager(new LinearLayoutManager(getContext()));
        reviewList.setAdapter(reviewAdapter);
        reviewList.setNestedScrollingEnabled(false);

        Bundle arguments = getArguments();
        if (arguments != null) {
            mVideoUri = arguments.getParcelable(DETAILS_VIDEO_URI);
            mReviewUri = arguments.getParcelable(DETAILS_REVIEW_URI);
        }

        if(savedInstanceState != null) {
            if(savedInstanceState.containsKey(DETAILS_VIDEO_URI)) {
                mVideoUri = (Uri) savedInstanceState.get(DETAILS_VIDEO_URI);
            }
            if(savedInstanceState.containsKey(DETAILS_REVIEW_URI)) {
                mReviewUri = (Uri) savedInstanceState.get(DETAILS_REVIEW_URI);
            }
        }

        return view;
    }

    public void updateMovie(Bundle args) {
        mPresenter.setViewUpdated(false);

        mVideoUri = args.getParcelable(DETAILS_VIDEO_URI);
        mReviewUri = args.getParcelable(DETAILS_REVIEW_URI);

        LoaderManager manager = getLoaderManager();
        if(manager.getLoader(DETAILS_VIDEO_LOADER) == null) {
            manager.initLoader(DETAILS_VIDEO_LOADER, null, this);
        } else {
            manager.restartLoader(DETAILS_VIDEO_LOADER, null, this);
        }

        if(manager.getLoader(DETAILS_REVIEW_LOADER) == null) {
            manager.initLoader(DETAILS_REVIEW_LOADER, null, this);
        } else {
            manager.restartLoader(DETAILS_REVIEW_LOADER, null, this);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(mVideoUri != null) {
            getLoaderManager().initLoader(DETAILS_VIDEO_LOADER, null, this);
        }
        if(mReviewUri != null) {
            getLoaderManager().initLoader(DETAILS_REVIEW_LOADER, null, this);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(DETAILS_VIDEO_URI, mVideoUri);
        outState.putParcelable(DETAILS_REVIEW_URI, mReviewUri);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case DETAILS_VIDEO_LOADER:
                return new CursorLoader(
                    getContext(),
                    mVideoUri,
                    DETAILS_VIDEO_PROJECTION,
                    null,
                    null,
                    null);
            case DETAILS_REVIEW_LOADER:
                return new CursorLoader(
                    getContext(),
                    mReviewUri,
                    DETAILS_REVIEW_PROJECTION,
                    null,
                    null,
                    null);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "onLoadFinished");
        switch (loader.getId()) {
            case DETAILS_VIDEO_LOADER:
                videoAdapter.swapCursor(data);
                break;
            case DETAILS_REVIEW_LOADER:
                reviewAdapter.swapCursor(data);
                break;
            default:
                break;
        }

        if(data != null && !mPresenter.isViewUpdated()) {
            updateUI(data);
            showDetailsView(true);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(TAG, "onLoaderReset");
        videoAdapter.swapCursor(null);
        reviewAdapter.swapCursor(null);
    }

    /**
     * Refresh details of movie from web service.
     */
    private void refreshData(long movieId) {

        String apiKey = getString(R.string.tmdbApiKey);

        mPresenter.requestMovieDetails(apiKey, movieId);
    }

    /*
     * Update movie details UI components.
     */
    private void updateUI(Cursor data) {

        if(!data.moveToFirst()) {
            return;
        }

        String title = data.getString(COL_MOVIE_TITLE);
        favorite = data.getInt(COL_MOVIE_FAVORITE) > 0;
        String posterPath = data.getString(COL_MOVIE_POSTER_PATH);
        String description = data.getString(COL_MOVIE_DESCRIPTION);
        String dateString = data.getString(COL_MOVIE_DATE);
        double popularity = data.getDouble(COL_MOVIE_POPULARITY);
        double voteAverage = data.getDouble(COL_MOVIE_VOTE_AVERAGE);

        titleView.setText(title);
        Drawable favDrawable = favorite ? ContextCompat.getDrawable(getContext(), R.drawable.ic_favorite) :
            ContextCompat.getDrawable(getContext(), R.drawable.ic_favorite_border);
        favoriteIcon.setImageDrawable(favDrawable);
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

        DateTime updateDateTime = new DateTime(data.getLong(COL_MOVIE_UPDATE_DATE));
        if(updateDateTime.isBefore(new DateTime().minusDays(1))) {
            refreshData(data.getLong(COL_MOVIE_API_ID));
        }
    }

    /**
     * Toggle details view & empty view visibility status.
     *
     * @param detailsVisible visibility status of the details view
     */
    private void showDetailsView(boolean detailsVisible) {
        int detailsViewVisibility = detailsVisible ? View.VISIBLE : View.GONE;
        int emptyViewVisibility = detailsVisible ? View.GONE : View.VISIBLE;
        detailsView.setVisibility(detailsViewVisibility);
        emptyView.setVisibility(emptyViewVisibility);
    }

    @Override
    public void syncVideoResults(VideoResults videoResults) {
        long movieId = videoResults.getMovieId();

        int size = videoResults.getVideos().size();
        ContentValues[] videoValues = new ContentValues[size];
        for(int i = 0; i < size; i++) {
            ContentValues values = new ContentValues();
            Video video = videoResults.getVideos().get(i);
            values.put(MovieContract.VideoEntry.COL_API_ID, video.getId());
            values.put(MovieContract.VideoEntry.COL_MOVIE_ID, movieId);
            values.put(MovieContract.VideoEntry.COL_KEY, video.getKey());
            values.put(MovieContract.VideoEntry.COL_TYPE, video.getType().getName());
            values.put(MovieContract.VideoEntry.COL_SITE, video.getSite());
            videoValues[i] = values;
        }
        getContext().getContentResolver().bulkInsert(MovieContract.VideoEntry.CONTENT_URI, videoValues);
        getLoaderManager().restartLoader(DETAILS_VIDEO_LOADER, null, this);
    }

    @Override
    public void syncReviewResults(ReviewResults reviewResults, long movieId) {

        int size = reviewResults.getReviews().size();
        ContentValues[] reviewValues = new ContentValues[size];
        for(int i = 0; i < size; i++) {
            ContentValues values = new ContentValues();
            Review review = reviewResults.getReviews().get(i);
            values.put(MovieContract.ReviewEntry.COL_API_ID, review.getId());
            values.put(MovieContract.ReviewEntry.COL_MOVIE_ID, movieId);
            values.put(MovieContract.ReviewEntry.COL_AUTHOR, review.getAuthor());
            values.put(MovieContract.ReviewEntry.COL_CONTENT, review.getText());
            values.put(MovieContract.ReviewEntry.COL_URL, review.getUrl());
            reviewValues[i] = values;
        }
        getContext().getContentResolver().bulkInsert(MovieContract.ReviewEntry.CONTENT_URI, reviewValues);
        getLoaderManager().restartLoader(DETAILS_REVIEW_LOADER, null, this);
    }

    @Override
    public void syncMovieDetails(Movie movie) {
        long movieId = movie.getId();

        ContentValues movieValues = new ContentValues();
        movieValues.put(MovieContract.MovieEntry.COL_UPDATE_DATE, new DateTime().getMillis());
        getContext().getContentResolver().update(
            MovieContract.MovieEntry.CONTENT_URI,
            movieValues,
            MovieContract.MovieEntry._ID + "=?",
            new String[]{String.valueOf(movieId)}
        );
    }
}
