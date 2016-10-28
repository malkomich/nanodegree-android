package com.github.malkomich.nanodegree.presenter;

import com.github.malkomich.nanodegree.data.webservice.HttpClientGenerator;
import com.github.malkomich.nanodegree.data.webservice.MovieService;
import com.github.malkomich.nanodegree.domain.MovieResults;
import com.github.malkomich.nanodegree.ui.view.PopularMoviesView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Responsible class of the logic of PopularMovies View, and his interaction with the model.
 */
public class PopularMoviesPresenter implements Callback<MovieResults> {

    private final PopularMoviesView mView;

    public PopularMoviesPresenter(PopularMoviesView view) {
        mView = view;
    }

    public void requestPopularMovies(String apiKey) {

        // HTTP Client initialization
        MovieService service = HttpClientGenerator.createService(
            MovieService.class,
            MovieService.BASE_URL
        );

        service.getPopularMovies(apiKey).enqueue(this);
    }

    @Override
    public void onResponse(Call<MovieResults> call, Response<MovieResults> response) {

        if(response.isSuccessful()) {
            MovieResults results = response.body();
            mView.syncMovieResults(results);
        }
    }

    @Override
    public void onFailure(Call<MovieResults> call, Throwable t) {
        t.printStackTrace();
    }
}
