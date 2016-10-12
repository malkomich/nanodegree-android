package com.github.malkomich.nanodegree.ui.fragment;

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
import com.github.malkomich.nanodegree.Util.MathUtils;
import com.github.malkomich.nanodegree.callback.OnTrailerLinkLoadedListener;
import com.github.malkomich.nanodegree.data.MovieClient;
import com.github.malkomich.nanodegree.data.MovieService;
import com.github.malkomich.nanodegree.domain.Movie;
import com.squareup.picasso.Picasso;

import org.joda.time.LocalDate;

import java.net.URL;

/**
 * Movie details view.
 */
public class MovieDetailsFragment extends Fragment implements OnTrailerLinkLoadedListener {

    private static final String TAG = MovieDetailsFragment.class.getName();
    public static final String MOVIE = "movie";

    private Movie mCurrentMovie;

    private ImageView imageView;
    private TextView titleView;
    private TextView descriptionView;
    private TextView popularityView;
    private TextView rateView;
    private TextView dateView;
    private Button trailerButton;

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

        imageView = (ImageView) view.findViewById(R.id.movie_image);
        titleView = (TextView) view.findViewById(R.id.movie_title);
        descriptionView = (TextView) view.findViewById(R.id.movie_description);
        popularityView = (TextView) view.findViewById(R.id.movie_popularity);
        rateView = (TextView) view.findViewById(R.id.movie_rate);
        dateView = (TextView) view.findViewById(R.id.movie_date);
        trailerButton = (Button) view.findViewById(R.id.movie_trailer_button);

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
    public void onTrailerLinkLoaded(URL trailerLink) {
        trailerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: add intent filter for youtube
            }
        });
        Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        trailerButton.setVisibility(View.VISIBLE);
        trailerButton.setAnimation(anim);
    }

    class GetTrailerLink extends AsyncTask<Bundle, Void, URL> {

        private OnTrailerLinkLoadedListener callback;

        public GetTrailerLink(OnTrailerLinkLoadedListener callback) {
            this.callback = callback;
        }

        @Override
        protected URL doInBackground(@NonNull Bundle... params) {
            String apiKey = params[0].getString(MovieService.API_KEY);
            int movieId = params[0].getInt(MovieService.MOVIE_ID);

            MovieService client = new MovieClient();

            return client.getMovieVideos(apiKey, movieId).getTrailerLink();
        }

        @Override
        protected void onPostExecute(URL trailerLink) {
            super.onPostExecute(trailerLink);
            if(trailerLink != null) {
                callback.onTrailerLinkLoaded(trailerLink);
            }
        }
    }

}
