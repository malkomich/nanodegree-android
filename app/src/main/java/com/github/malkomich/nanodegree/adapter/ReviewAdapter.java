package com.github.malkomich.nanodegree.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.malkomich.nanodegree.R;
import com.github.malkomich.nanodegree.ui.fragment.MovieDetailsFragment;

/**
 * Adapter for the movie's video list.
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private final Context mContext;
    private Cursor cursor;

    public ReviewAdapter(Context context) {
        mContext = context;
    }

    /* (non-Javadoc)
     * @see android.support.v7.widget.RecyclerView.Adapter#onCreateViewHolder()
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.movie_detail_review_item, parent, false);
        return new ViewHolder(view);
    }

    /* (non-Javadoc)
     * @see android.support.v7.widget.RecyclerView.Adapter#onBindViewHolder()
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        cursor.moveToPosition(position);
        holder.authorView.setText(cursor.getString(MovieDetailsFragment.COL_REVIEW_AUTHOR));
        holder.contentView.setText(cursor.getString(MovieDetailsFragment.COL_REVIEW_CONTENT));
    }

    /* (non-Javadoc)
     * @see android.support.v7.widget.RecyclerView.Adapter#getItemCount()
     */
    @Override
    public int getItemCount() {
        return cursor != null ? cursor.getCount() : 0;
    }

    /**
     * Update adapter data with a new Cursor
     *
     * @param newCursor Data from DB query
     */
    public void swapCursor(Cursor newCursor) {
        if (newCursor != null) {
            cursor = newCursor;
            notifyDataSetChanged();
        }
    }

    /**
     * Encapsulates the view elements for each item in the adapter.
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView authorView;
        public final TextView contentView;

        public ViewHolder(View view) {
            super(view);
            authorView = (TextView) view.findViewById(R.id.review_author);
            contentView = (TextView) view.findViewById(R.id.review_content);
            view.setOnClickListener(this);
        }

        /* (non-Javadoc)
         * @see android.view.View.OnClickListener#onClick()
         */
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            cursor.moveToPosition(position);
            String url = cursor.getString(MovieDetailsFragment.COL_REVIEW_URL);
            mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        }
    }
}
