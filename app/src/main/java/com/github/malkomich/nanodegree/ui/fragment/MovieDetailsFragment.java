package com.github.malkomich.nanodegree.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.malkomich.nanodegree.R;
import com.github.malkomich.nanodegree.util.MathUtils;
import com.github.malkomich.nanodegree.callback.OnTrailerLinkLoadedListener;
import com.github.malkomich.nanodegree.data.MovieClient;
import com.github.malkomich.nanodegree.data.MovieService;
import com.github.malkomich.nanodegree.domain.Movie;
import com.github.malkomich.nanodegree.domain.VideoResults;
import com.squareup.picasso.Picasso;

import org.joda.time.LocalDate;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Movie details view.
 */
public class MovieDetailsFragment extends Fragment implements OnTrailerLinkLoadedListener {

    private static final String TAG = MovieDetailsFragment.class.getName();
    public static final String MOVIE = "movie";

    private Movie mCurrentMovie;

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
            mCurrentMovie = savedInstanceState.getParcelable(MOVIE);
        }

        View view = inflater.inflate(R.layout.fragment_movie_details, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    public void updateMovie(Movie movie) {

        Picasso.with(getContext()).load(movie.getPosterPath()).into(imageView);
        titleView.setText(movie.getTitle());
        descriptionView.setText(movie.getDescription());
        popularityView.setText(
            String.valueOf(MathUtils.roundDouble(movie.getPopularity(), 2))
        );
        rateView.setText(
            String.valueOf(MathUtils.roundDouble(movie.getVoteAverage(), 2))
        );

        LocalDate date = movie.getDate();
        dateView.setText(getString(R.string.date,
            date.getDayOfMonth(),
            movie.getDate().toString("MMMM"),
            date.getYear())
        );

        mCurrentMovie = movie;

        Bundle params = new Bundle();
        params.putString(MovieService.API_KEY, getString(R.string.tmdbApiKey));
        params.putInt(MovieService.MOVIE_ID, movie.getId());
        new GetTrailerLink(this).execute(params);
    }

    @Override
    public void onStart() {
        super.onStart();

        Bundle args = getArguments();
        if (args != null) {
            updateMovie((Movie) args.getParcelable(MOVIE));
        } else if (mCurrentMovie != null) {
            updateMovie(mCurrentMovie);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(MOVIE, mCurrentMovie);
    }

    @Override
    public void onVideosLoaded(final VideoResults videoResults) {

        final Uri trailerLink = videoResults.getTrailerLink();
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

    class GetTrailerLink extends AsyncTask<Bundle, Void, VideoResults> {

        private OnTrailerLinkLoadedListener callback;

        public GetTrailerLink(OnTrailerLinkLoadedListener callback) {
            this.callback = callback;
        }

        @Override
        protected VideoResults doInBackground(@NonNull Bundle... params) {
            String apiKey = params[0].getString(MovieService.API_KEY);
            int movieId = params[0].getInt(MovieService.MOVIE_ID);

            MovieService client = new MovieClient();

            return client.getMovieVideos(apiKey, movieId);
        }

        @Override
        protected void onPostExecute(VideoResults videoResults) {
            super.onPostExecute(videoResults);
            if(videoResults != null) {
                callback.onVideosLoaded(videoResults);
            }
        }
    }

}
