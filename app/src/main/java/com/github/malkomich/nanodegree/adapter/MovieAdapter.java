package com.github.malkomich.nanodegree.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.malkomich.nanodegree.R;
import com.github.malkomich.nanodegree.ui.fragment.PopularMoviesFragment;
import com.squareup.picasso.Picasso;

import static android.widget.ImageView.ScaleType.CENTER_CROP;

/**
 * Adapter for the movies images.
 */
public class MovieAdapter extends CursorAdapter {

    public MovieAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return new ImageView(context);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ImageView imageView = (ImageView) view;
        imageView.setScaleType(CENTER_CROP);

        String url = cursor.getString(PopularMoviesFragment.COL_MOVIE_POSTER_PATH);

        // Trigger the download of the URL asynchronously into the image view.
        Picasso.with(context)
            .load(url)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.error)
            .fit()
            .tag(context)
            .into(imageView);
    }

}
