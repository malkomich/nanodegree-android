package com.github.malkomich.nanodegree.presenter;

import com.github.malkomich.nanodegree.data.database.MovieContract;
import com.github.malkomich.nanodegree.data.webservice.HttpClientGenerator;
import com.github.malkomich.nanodegree.data.webservice.MovieService;
import com.github.malkomich.nanodegree.domain.Movie;
import com.github.malkomich.nanodegree.domain.ReviewResults;
import com.github.malkomich.nanodegree.domain.VideoResults;
import com.github.malkomich.nanodegree.ui.view.MovieDetailsView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Responsible class of the logic of MovieDetails View, and his interaction with the model.
 */
public class MovieDetailsPresenter implements Callback<Movie> {

    private static final String APPENDED_RESOURCES = "videos,reviews";

    private MovieDetailsView mView;
    private boolean mUpdated;

    public MovieDetailsPresenter(MovieDetailsView view) {
        mView = view;
    }


    public void requestMovieDetails(String apiKey, long movieId) {

        // HTTP Client initialization
        MovieService service = HttpClientGenerator.createService(
            MovieService.class,
            MovieService.BASE_URL
        );

        service.getMovieDetails(movieId, apiKey, APPENDED_RESOURCES).enqueue(this);

        mUpdated = true;
    }

    @Override
    public void onResponse(Call<Movie> call, Response<Movie> response) {

        if(response.isSuccessful()) {
            Movie movie = response.body();

            // Persist video items in DB
            VideoResults videoResults = movie.getVideoResults();
            mView.syncVideoResults(videoResults);

            // Persist review items in DB
            ReviewResults reviewResults = movie.getReviewResults();
            mView.syncReviewResults(reviewResults);

            // Update movie date of last sync with API
            mView.syncMovieDetails(movie);
        }
    }

    @Override
    public void onFailure(Call<Movie> call, Throwable t) {
    }

}
