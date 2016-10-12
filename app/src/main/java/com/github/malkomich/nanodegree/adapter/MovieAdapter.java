package com.github.malkomich.nanodegree.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.github.malkomich.nanodegree.R;
import com.github.malkomich.nanodegree.callback.OnMovieSelectedListener;
import com.github.malkomich.nanodegree.domain.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.widget.ImageView.ScaleType.CENTER_CROP;

/**
 * Adapter for the movies images, which stores a list of the URLs.
 */
public class MovieAdapter extends BaseAdapter {

    private final Context context;
    private List<Movie> movies = new ArrayList<>();

    public MovieAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public Movie getItem(int position) {
        return movies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView view = (ImageView) convertView;
        if (view == null) {
            view = new ImageView(context);
            view.setScaleType(CENTER_CROP);
        }

        final Movie movie = getItem(position);
        String url = movie.getPosterPath();

        // Trigger the download of the URL asynchronously into the image view.
        Picasso.with(context)
            .load(url)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.error)
            .fit()
            .tag(context)
            .into(view);

        return view;
    }

    public void sortByPopularity() {

        Collections.sort(movies, new Comparator<Movie>() {
            @Override
            public int compare(Movie originalMovie, Movie secondMovie) {
                return originalMovie.getPopularity() < secondMovie.getPopularity()  ? 1 : -1;
            }
        });

        notifyDataSetChanged();
    }

    public void sortByRate() {

        Collections.sort(movies, new Comparator<Movie>() {
            @Override
            public int compare(Movie originalMovie, Movie secondMovie) {
                return originalMovie.getVoteAverage() < secondMovie.getVoteAverage()  ? 1 : -1;
            }
        });

        notifyDataSetChanged();
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }
}
