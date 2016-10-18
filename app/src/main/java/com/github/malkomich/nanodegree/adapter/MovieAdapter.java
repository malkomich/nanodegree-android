package com.github.malkomich.nanodegree.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.github.malkomich.nanodegree.R;
import com.github.malkomich.nanodegree.callback.OnMovieSelectedListener;
import com.github.malkomich.nanodegree.data.database.MovieContract;
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

        int index = cursor.getColumnIndex(MovieContract.MovieEntry.COL_POSTER_PATH);
        String url = cursor.getString(index);

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
