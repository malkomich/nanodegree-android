package com.github.malkomich.nanodegree.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.malkomich.nanodegree.R;
import com.github.malkomich.nanodegree.Util.MathUtils;
import com.github.malkomich.nanodegree.domain.Movie;
import com.squareup.picasso.Picasso;

import org.joda.time.LocalDate;

/**
 * Movie details view.
 */
public class MovieDetailsFragment extends Fragment {

    private static final String TAG = MovieDetailsFragment.class.getName();
    public static final String MOVIE = "movie";

    private Movie mCurrentMovie;

    private ImageView imageView;
    private TextView titleView;
    private TextView descriptionView;
    private TextView popularityView;
    private TextView rateView;
    private TextView dateView;

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

}
