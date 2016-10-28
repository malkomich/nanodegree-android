package com.github.malkomich.nanodegree.ui.activity;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.github.malkomich.nanodegree.R;
import com.github.malkomich.nanodegree.callback.OnDetailItemSelectedListener;
import com.github.malkomich.nanodegree.data.database.MovieContract;
import com.github.malkomich.nanodegree.ui.fragment.MovieDetailsFragment;
import com.github.malkomich.nanodegree.ui.fragment.PopularMoviesFragment;

/**
 * Popular movies app activity.
 */
public class PopularMoviesActivity extends AppCompatActivity implements OnDetailItemSelectedListener,
    FragmentManager.OnBackStackChangedListener {

    private static final String TAG = PopularMoviesActivity.class.getName();

    /* (non-Javadoc)
     * @see android.support.v7.app.AppCompatActivity#onCreate()
     */
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

        // Add listener for back stack changes in the fragment manager.
        getSupportFragmentManager().addOnBackStackChangedListener(this);
    }

    /* (non-Javadoc)
     * @see com.github.malkomich.nanodegree.callback.OnDetailItemSelectedListener#onItemSelected()
     */
    @Override
    public void onItemSelected(Cursor cursor) {

        MovieDetailsFragment detailsFragment = (MovieDetailsFragment)
            getSupportFragmentManager().findFragmentById(R.id.movie_details_fragment);

        Bundle args = new Bundle();
        args.putLong(MovieDetailsFragment.MOVIE_ID, cursor.getLong(PopularMoviesFragment.COL_MOVIE_ID));

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

    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentManager.OnBackStackChangedListener#onBackStackChanged()
     */
    @Override
    public void onBackStackChanged() {
        // Enable Up button if there are entries in the back stack.
        boolean enableUp = getSupportFragmentManager().getBackStackEntryCount() > 0;
        getSupportActionBar().setDisplayHomeAsUpEnabled(enableUp);
        getSupportActionBar().setDisplayUseLogoEnabled(!enableUp);
    }

    /* (non-Javadoc)
     * @see android.support.v7.app.AppCompatActivity#onSupportNavigateUp()
     */
    @Override
    public boolean onSupportNavigateUp() {
        // Called when the up button is pressed.
        getSupportFragmentManager().popBackStack();
        return true;
    }
}
