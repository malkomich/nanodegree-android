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

    /**
     * Get popular movies from API.
     *
     * @param apiKey web service API key
     * @param language language preferred of the response
     * @param page number of page requested
     * @return A Retrofit call to get the data
     */
    @GET(RES_POPULAR)
    Call<MovieResults> getPopularMovies(
        @Query(API_KEY_PARAM) String apiKey,
        @Query(LANG_PARAM) String language,
        @Query(PAGE_PARAM) int page);

    /**
     * Get the first page of popular movies from API.
     *
     * @param apiKey web service API key
     * @param language language preferred of the response
     * @return A Retrofit call to get the data
     */
    @GET(RES_POPULAR)
    Call<MovieResults> getPopularMovies(
        @Query(API_KEY_PARAM) String apiKey,
        @Query(LANG_PARAM) String language);

    /**
     * Get a specific page of popular movies from API, given in the default language(en).
     *
     * @param apiKey web service API key
     * @param page number of page requested
     * @return A Retrofit call to get the data
     */
    @GET(RES_POPULAR)
    Call<MovieResults> getPopularMovies(
        @Query(API_KEY_PARAM) String apiKey,
        @Query(PAGE_PARAM) int page);

    /**
     * Get the first page of popular movies from API, given in the default language(en).
     *
     * @param apiKey web service API key
     * @return A Retrofit call to get the data
     */
    @GET(RES_POPULAR)
    Call<MovieResults> getPopularMovies(@Query(API_KEY_PARAM) String apiKey);

    /**
     * Get movie details from API.
     *
     * @param movieId unique ID of the movie to look for
     * @param apiKey web service API key
     * @param language language preferred of the response
     * @param appendResources optional resources to append to the response
     * @return A Retrofit call to get the data
     */
    @GET(RES_MOVIE_DETAILS)
    Call<Movie> getMovieDetails(
        @Path("movie_id") int movieId,
        @Query(API_KEY_PARAM) String apiKey,
        @Query(LANG_PARAM) String language,
        @Query(APPEND_PARAM) String appendResources);

    /**
     * Get movie details from API, given in the default language(en).
     *
     * @param movieId unique ID of the movie to look for
     * @param apiKey web service API key
     * @param appendResources optional resources to append to the response
     * @return A Retrofit call to get the data
     */
    @GET(RES_MOVIE_DETAILS)
    Call<Movie> getMovieDetails(
        @Path("movie_id") long movieId,
        @Query(API_KEY_PARAM) String apiKey,
        @Query(APPEND_PARAM) String appendResources);
}
