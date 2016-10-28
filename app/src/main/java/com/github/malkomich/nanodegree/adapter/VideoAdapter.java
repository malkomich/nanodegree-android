package com.github.malkomich.nanodegree.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.malkomich.nanodegree.R;
import com.github.malkomich.nanodegree.domain.Video;
import com.github.malkomich.nanodegree.ui.fragment.MovieDetailsFragment;

/**
 * Adapter for the movie's video list.
 */
public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    private final Context mContext;
    private Cursor cursor;

    public VideoAdapter(Context context) {
        mContext = context;
    }

    /* (non-Javadoc)
     * @see android.support.v7.widget.RecyclerView.Adapter#onCreateViewHolder()
     */
    @Override
    public VideoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.movie_detail_video_item, parent, false);
        return new ViewHolder(view);
    }

    /* (non-Javadoc)
     * @see android.support.v7.widget.RecyclerView.Adapter#onBindViewHolder()
     */
    @Override
    public void onBindViewHolder(VideoAdapter.ViewHolder holder, int position) {

        cursor.moveToPosition(position);
        holder.textView.setText(cursor.getString(MovieDetailsFragment.COL_VIDEO_TYPE));
    }

    /* (non-Javadoc)
     * @see android.support.v7.widget.RecyclerView.Adapter#getItemCount()
     */
    @Override
    public int getItemCount() {
        return cursor != null ? cursor.getCount() : 0;
    }

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

        public final TextView textView;

        public ViewHolder(View view) {
            super(view);
            textView = (TextView) view.findViewById(R.id.video_type);
            view.setOnClickListener(this);
        }

        /* (non-Javadoc)
         * @see android.view.View.OnClickListener#onClick()
         */
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            cursor.moveToPosition(position);
            String site = cursor.getString(MovieDetailsFragment.COL_VIDEO_SITE);
            if(Video.SITE_YOUTUBE.equals(site)) {
                String key = cursor.getString(MovieDetailsFragment.COL_VIDEO_KEY);
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Video.getUri(key)));
            } else {
                Toast.makeText(mContext, mContext.getString(R.string.video_site_invalid, site), Toast.LENGTH_SHORT)
                    .show();
            }
        }
    }
}
