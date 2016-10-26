package com.github.malkomich.nanodegree.ui.fragment;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.malkomich.nanodegree.R;
import com.github.malkomich.nanodegree.adapter.MovieAdapter;
import com.github.malkomich.nanodegree.callback.OnDetailItemSelectedListener;
import com.github.malkomich.nanodegree.data.database.MovieContract;
import com.github.malkomich.nanodegree.data.webservice.HttpClientGenerator;
import com.github.malkomich.nanodegree.data.webservice.MovieService;
import com.github.malkomich.nanodegree.domain.Movie;
import com.github.malkomich.nanodegree.domain.MovieResults;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Movies fragment containing the grid view of poster images of the popular films.
 */
public class PopularMoviesFragment extends Fragment implements Callback<MovieResults>,
    LoaderManager.LoaderCallbacks<Cursor> {

    // Index for the projected columns of Movie's table.
    public static final int COL_MOVIE_ID = 0;
    public static final int COL_MOVIE_API_ID = 1;
    public static final int COL_MOVIE_TITLE = 2;
    public static final int COL_MOVIE_DESCRIPTION = 3;
    public static final int COL_MOVIE_DATE = 4;
    public static final int COL_MOVIE_POSTER_PATH = 5;
    public static final int COL_MOVIE_POPULARITY = 6;
    public static final int COL_MOVIE_VOTE_COUNT = 7;
    public static final int COL_MOVIE_VOTE_AVERAGE = 8;

    // Projection for Movie's query.
    private static final String[] MOVIE_PROJECTION = {
        MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry._ID,
        MovieContract.MovieEntry.COL_API_ID,
        MovieContract.MovieEntry.COL_TITLE,
        MovieContract.MovieEntry.COL_DESCRIPTION,
        MovieContract.MovieEntry.COL_DATE,
        MovieContract.MovieEntry.COL_POSTER_PATH,
        MovieContract.MovieEntry.COL_POPULARITY,
        MovieContract.MovieEntry.COL_VOTE_COUNT,
        MovieContract.MovieEntry.COL_VOTE_AVERAGE
    };

    private static final String TAG = PopularMoviesFragment.class.getName();
    private static final int MOVIE_LOADER = 0;
    private static final String PREFS_NAME = "PopularMoviesPrefs";
    private static final String PREFS_ORDER = "order";
    private static final String SELECTED_KEY = "selected_position";

    private MovieAdapter adapter;
    private OnDetailItemSelectedListener onMovieSelectedListener;
    // Item position in the grid view for the auto scroll
    private int mPosition;

    @BindView(R.id.refreshSwiper) protected SwipeRefreshLayout refreshSwiper;
    @BindView(R.id.grid_view) protected GridView gridView;
    @BindView(R.id.connection_error_layout) protected RelativeLayout errorView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_popular_movies, container, false);
        ButterKnife.bind(this, view);

        refreshSwiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });

        adapter = new MovieAdapter(getContext(), null, 0);

        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                onMovieSelectedListener.onItemSelected(cursor);
                mPosition = position;
            }
        });
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {}

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                refreshSwiper.setEnabled(gridView.getChildCount() > 0 && gridView.getChildAt(0).getTop() == 0);
            }
        });

        // Restore item position selected on the grid view.
        if(savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(MOVIE_LOADER, savedInstanceState, this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_popular_movies, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(mPosition != GridView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Picasso.with(getContext()).cancelTag(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_sort_popularity:
                sortBy(MovieContract.MovieEntry.COL_POPULARITY);
                return true;
            case R.id.action_sort_rated:
                sortBy(MovieContract.MovieEntry.COL_VOTE_AVERAGE);
                return true;
        }

        return super.onOptionsItemSelected(item); // important line
    }

    /* (non-Javadoc)
     * @see android.app.Fragment#onAttach()
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            onMovieSelectedListener = (OnDetailItemSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnPlaceUpdatedListener");
        }
    }

    /**
     *  Check if exists any connection available.
     *
     * @return boolean
     */
    private boolean isOnline() {
        ConnectivityManager connManager =
            (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connManager.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    /**
     * Refresh movies shown on the grid view.
     */
    private void refreshData() {

        if(isOnline()) {
            String apiKey = getString(R.string.tmdbApiKey);

            // HTTP Client initialization
            MovieService service = HttpClientGenerator.createService(
                MovieService.class,
                MovieService.BASE_URL
            );

            service.getPopularMovies(apiKey).enqueue(this);

        } else {
            refreshSwiper.setRefreshing(false);

            if (adapter == null || adapter.isEmpty()) {
                showGridView(false);
            } else {
                Toast.makeText(getContext(), getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Toggle grid view & error view visibility status.
     *
     * @param gridViewVisible visibility status of the grid view
     */
    private void showGridView(boolean gridViewVisible) {
        int gridViewVisibility = gridViewVisible ? View.VISIBLE : View.GONE;
        int errorViewVisibility = gridViewVisible ? View.GONE : View.VISIBLE;
        gridView.setVisibility(gridViewVisibility);
        errorView.setVisibility(errorViewVisibility);
    }

    /**
     * Change the appearing order of the movies.
     *
     * @param order Field of movie entity to sort by.
     */
    private void sortBy(final String order) {
        // Save order in preferences
        SharedPreferences preferences = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREFS_ORDER, order);
        editor.apply();

        // Restart loader to apply the new sort order stored on preferences.
        getLoaderManager().restartLoader(MOVIE_LOADER, null, this);
    }

    @Override
    public void onResponse(Call<MovieResults> call, Response<MovieResults> response) {

        if(response.isSuccessful()) {
            MovieResults results = response.body();

            int size = results.getMovies().size();
            ContentValues[] valuesArray = new ContentValues[size];
            for(int i=0; i < size; i++) {
                ContentValues values = new ContentValues();
                Movie movie = results.getMovies().get(i);
                values.put(MovieContract.MovieEntry.COL_API_ID, movie.getId());
                values.put(MovieContract.MovieEntry.COL_TITLE, movie.getTitle());
                values.put(MovieContract.MovieEntry.COL_DESCRIPTION, movie.getDescription());
                values.put(MovieContract.MovieEntry.COL_DATE, movie.getDateString());
                values.put(MovieContract.MovieEntry.COL_POSTER_PATH, movie.getPosterPath());
                values.put(MovieContract.MovieEntry.COL_POPULARITY, movie.getPopularity());
                values.put(MovieContract.MovieEntry.COL_VOTE_COUNT, movie.getVoteCount());
                values.put(MovieContract.MovieEntry.COL_VOTE_AVERAGE, movie.getVoteAverage());
                valuesArray[i] = values;
            }
            getContext().getContentResolver().bulkInsert(MovieContract.MovieEntry.CONTENT_URI, valuesArray);

            // Update views
            showGridView(true);
            refreshSwiper.setRefreshing(false);
        }
    }

    @Override
    public void onFailure(Call<MovieResults> call, Throwable t) {
        t.printStackTrace();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader");
        SharedPreferences preferences = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String order = preferences.getString("order", MovieContract.MovieEntry.COL_POPULARITY);

        return new CursorLoader(
            getContext(),
            MovieContract.MovieEntry.CONTENT_URI,
            MOVIE_PROJECTION,
            null,
            null,
            order + " DESC LIMIT 20"
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "onLoadFinished");
        adapter.swapCursor(data);

        int i = adapter.getCount();
        if(adapter.isEmpty()) {
            refreshData();
        }

        if(mPosition != GridView.INVALID_POSITION) {
            gridView.smoothScrollToPosition(mPosition);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(TAG, "onLoaderReset");
        adapter.swapCursor(null);
    }
}
