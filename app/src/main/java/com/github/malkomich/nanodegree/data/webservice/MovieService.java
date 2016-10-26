package com.github.malkomich.nanodegree.data.webservice;

import com.github.malkomich.nanodegree.domain.Movie;
import com.github.malkomich.nanodegree.domain.MovieResults;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Definition of a movie data getter service.
 */
public interface MovieService {

    // Paths
    String BASE_URL = "http://api.themoviedb.org/3/";
    String RES_POPULAR = "movie/popular";
    String RES_MOVIE_DETAILS = "movie/{movie_id}";

    // URL Query params
    String API_KEY_PARAM = "api_key";
    String LANG_PARAM = "language";
    String PAGE_PARAM = "page";
    String APPEND_PARAM = "append_to_response";

    @GET(RES_POPULAR)
    Call<MovieResults> getPopularMovies(
        @Query(API_KEY_PARAM) String apiKey,
        @Query(LANG_PARAM) String language,
        @Query(PAGE_PARAM) int page);

    @GET(RES_POPULAR)
    Call<MovieResults> getPopularMovies(
        @Query(API_KEY_PARAM) String apiKey,
        @Query(LANG_PARAM) String language);

    @GET(RES_POPULAR)
    Call<MovieResults> getPopularMovies(
        @Query(API_KEY_PARAM) String apiKey,
        @Query(PAGE_PARAM) int page);

    @GET(RES_POPULAR)
    Call<MovieResults> getPopularMovies(@Query(API_KEY_PARAM) String apiKey);

    @GET(RES_MOVIE_DETAILS)
    Call<Movie> getMovieDetails(
        @Path("movie_id") int movieId,
        @Query(API_KEY_PARAM) String apiKey,
        @Query(LANG_PARAM) String language,
        @Query(APPEND_PARAM) String appendResources);

    @GET(RES_MOVIE_DETAILS)
    Call<Movie> getMovieDetails(
        @Path("movie_id") long movieId,
        @Query(API_KEY_PARAM) String apiKey,
        @Query(APPEND_PARAM) String appendResources);
}
