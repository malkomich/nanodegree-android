package com.github.malkomich.nanodegree.data;

import com.github.malkomich.nanodegree.domain.MovieResults;
import com.github.malkomich.nanodegree.domain.VideoResults;

/**
 * Definition of a movie data getter service.
 */
public interface MovieService {

    String API_KEY = "apiKey";
    String LANG = "language";
    String PAGE = "page";
    String MOVIE_ID = "movieId";

    MovieResults getPopularMovies(String apiKey, String language, int page);

    MovieResults getPopularMovies(String apiKey, String language);

    MovieResults getPopularMovies(String apiKey, int page);

    MovieResults getPopularMovies(String apiKey);

    VideoResults getMovieVideos(String apiKey, int movieId, String language);

    VideoResults getMovieVideos(String apiKey, int movieId);
}
