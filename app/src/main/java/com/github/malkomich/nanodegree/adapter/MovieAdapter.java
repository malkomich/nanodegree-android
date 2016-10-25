package com.github.malkomich.nanodegree.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.malkomich.nanodegree.R;
import com.github.malkomich.nanodegree.ui.fragment.PopularMoviesFragment;
import com.squareup.picasso.Picasso;

/**
 * Adapter for the movies images.
 */
public class MovieAdapter extends CursorAdapter {

    public MovieAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.popular_movie_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        String url = cursor.getString(PopularMoviesFragment.COL_MOVIE_POSTER_PATH);

        // Trigger the download of the URL asynchronously into the image view.
        Picasso.with(context)
            .load(url)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.error)
            .fit()
            .tag(context)
            .into(viewHolder.imageView);
    }

    public static class ViewHolder {
        public final ImageView imageView;

        public ViewHolder(View view) {
            imageView = (ImageView) view.findViewById(R.id.popular_movie_image);
        }
    }

}
