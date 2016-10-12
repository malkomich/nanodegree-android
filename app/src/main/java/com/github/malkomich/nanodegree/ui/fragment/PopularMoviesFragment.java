package com.github.malkomich.nanodegree.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.github.malkomich.nanodegree.R;
import com.github.malkomich.nanodegree.adapter.MovieAdapter;
import com.github.malkomich.nanodegree.callback.OnMovieSelectedListener;
import com.github.malkomich.nanodegree.callback.OnMoviesLoadedListener;
import com.github.malkomich.nanodegree.data.MovieClient;
import com.github.malkomich.nanodegree.data.MovieService;
import com.github.malkomich.nanodegree.domain.MovieResults;
import com.squareup.picasso.Picasso;

/**
 * Movies fragment containing the grid view of poster images of the popular films.
 */
public class PopularMoviesFragment extends Fragment implements OnMoviesLoadedListener {

    private static final String TAG = PopularMoviesFragment.class.getName();
    private static final String PREFS_NAME = "PopularMoviesPrefs";
    private static final String PREFS_ORDER = "order";
    private static final int PREFS_ORDER_POPULARITY = 0;
    private static final int PREFS_ORDER_RATE = 1;

    private GridView gridView;
    private MovieAdapter adapter;
    private OnMovieSelectedListener onMovieSelectedListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        adapter = new MovieAdapter(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_popular_movies, container, false);

        gridView = (GridView) view.findViewById(R.id.grid_view);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onMovieSelectedListener.onMovieSelected(adapter.getItem(position));
            }
        });

        // Call async task after creating grid view, if necessary
        if(adapter == null || adapter.isEmpty()) {
            Bundle params = new Bundle();
            params.putString(MovieService.API_KEY, getString(R.string.tmdbApiKey));
            new GetPopularMovies(this).execute(params);
        }

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_popular_movies, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Picasso.with(getContext()).cancelTag(this);
    }

    @Override
    public void onMoviesLoaded(MovieResults results) {

        Log.d(TAG, "onMoviesLoaded");

        SharedPreferences preferences = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int order = preferences.getInt("order", PREFS_ORDER_POPULARITY);

        adapter.setMovies(results.getMovies());
        sortBy(order, false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_sort_popularity:
                sortBy(PREFS_ORDER_POPULARITY, true);
                return true;
            case R.id.action_sort_rated:
                sortBy(PREFS_ORDER_RATE, true);
                return true;
            case android.R.id.home:
                Log.d(TAG, "AAAAAAA");
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
            onMovieSelectedListener = (OnMovieSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnPlaceUpdatedListener");
        }
    }

    /**
     * Change the appearing order of the movies.
     *
     * @param order int
     * @param fromActionsMenu
     *                      Indicates if the operation requests come from the actions menu or not
     */
    private void sortBy(final int order, boolean fromActionsMenu) {

        switch (order) {
            case PREFS_ORDER_POPULARITY:
                adapter.sortByPopularity();
                break;
            case PREFS_ORDER_RATE:
                Log.d(TAG, "sortByRate");
                adapter.sortByRate();
                break;
        }

        if(fromActionsMenu) {
            // Save order in preferences
            SharedPreferences preferences = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(PREFS_ORDER, order);
            editor.apply();
        }
    }

    class GetPopularMovies extends AsyncTask<Bundle, Void, MovieResults> {

        private OnMoviesLoadedListener callback;

        public GetPopularMovies(OnMoviesLoadedListener callback) {
            this.callback = callback;
        }

        @Override
        protected MovieResults doInBackground(@NonNull Bundle... params) {
            String apiKey = params[0].getString(MovieService.API_KEY);

            MovieService client = new MovieClient();

            return client.getPopularMovies(apiKey);
        }

        @Override
        protected void onPostExecute(MovieResults movieResults) {
            super.onPostExecute(movieResults);
            if(movieResults != null) {
                callback.onMoviesLoaded(movieResults);
            }
        }
    }
}
