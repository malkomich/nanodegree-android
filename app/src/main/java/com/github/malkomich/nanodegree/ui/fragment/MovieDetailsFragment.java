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
import com.github.malkomich.nanodegree.domain.Movie;
import com.squareup.picasso.Picasso;

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

        return view;
    }

    public void updateMovie(Movie movie) {

        Picasso.with(getContext()).load(movie.getPosterPath()).into(imageView);
        titleView.setText(movie.getTitle());
        descriptionView.setText(movie.getDescription());
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
