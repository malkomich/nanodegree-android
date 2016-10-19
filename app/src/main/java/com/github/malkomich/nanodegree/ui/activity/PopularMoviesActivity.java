package com.github.malkomich.nanodegree.ui.activity;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.malkomich.nanodegree.R;
import com.github.malkomich.nanodegree.callback.OnDetailItemSelectedListener;
import com.github.malkomich.nanodegree.data.database.MovieContract;
import com.github.malkomich.nanodegree.ui.fragment.MovieDetailsFragment;
import com.github.malkomich.nanodegree.ui.fragment.PopularMoviesFragment;

/**
 * Popular movies app activity.
 */
public class PopularMoviesActivity extends AppCompatActivity implements OnDetailItemSelectedListener {

    private static final String TAG = PopularMoviesActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);

        // Check whether the activity is using the layout version with
        // the fragment_container FrameLayout. If so, we must add the first fragment
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            PopularMoviesFragment gridFragment = new PopularMoviesFragment();

            // In case this activity was started with special instructions from an Intent,
            // pass the Intent's extras to the fragment as arguments
            gridFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, gridFragment)
                .commit();
        }
    }

    @Override
    public void onItemSelected(Cursor cursor) {

        MovieDetailsFragment detailsFragment = (MovieDetailsFragment)
            getSupportFragmentManager().findFragmentById(R.id.movie_details_fragment);

        Uri detailsUri = MovieContract.VideoEntry.
            buildVideoUriWithMovieId(cursor.getLong(PopularMoviesFragment.COL_MOVIE_ID));

        Bundle args = new Bundle();
        args.putParcelable(MovieDetailsFragment.URI, detailsUri);

        if (detailsFragment != null) {
            // If article frag is available, we're in two-pane layout...
            detailsFragment.updateMovie(args);

        } else {
            // If the frag is not available, we're in the one-pane layout and must swap frags...
            detailsFragment = new MovieDetailsFragment();
            detailsFragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, detailsFragment)
                .addToBackStack(null)
                .commit();
        }
    }
}
