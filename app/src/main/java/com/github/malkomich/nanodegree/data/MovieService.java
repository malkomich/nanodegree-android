package com.github.malkomich.nanodegree.data;

import com.github.malkomich.nanodegree.domain.MovieResults;
import com.github.malkomich.nanodegree.domain.VideoResults;

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
    String RES_VIDEOS = "movie/{movie_id}/videos";

    // URL Query params
    String API_KEY_PARAM = "api_key";
    String LANG_PARAM = "language";
    String PAGE_PARAM = "page";

    String API_KEY = "apiKey";
    String LANG = "language";
    String PAGE = "page";
    String MOVIE_ID = "movieId";

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

    @GET(RES_VIDEOS)
    Call<VideoResults> getMovieVideos(
        @Path("movie_id") int movieId,
        @Query(API_KEY_PARAM) String apiKey,
        @Query(LANG_PARAM) String language);

    @GET(RES_VIDEOS)
    Call<VideoResults> getMovieVideos(
        @Path("movie_id") int movieId,
        @Query(API_KEY_PARAM) String apiKey);
}
