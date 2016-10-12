package com.github.malkomich.nanodegree.data;

import com.github.malkomich.nanodegree.domain.MovieResults;
import com.github.malkomich.nanodegree.domain.VideoResults;

import java.net.URL;

/**
 * REST client which gets movie's data.
 */
public class MovieClient extends RestClient implements MovieService {

    private static final String BASE_URL = "http://api.themoviedb.org/3/movie";

    private static final String RES_POPULAR = "popular";
    private static final String RES_VIDEOS = "videos";

    // Popular Movies params & default values
    private static final String KEY_PARAM = "api_key";
    private static final String LANG_PARAM = "language";
    private static final String PAGE_PARAM = "page";

    private static final String DEFAULT_LANG = "en";
    private static final int DEFAULT_PAGE = 1;

    public MovieClient() {
        super(BASE_URL);
    }

    @Override
    public MovieResults getPopularMovies(String apiKey, String language, int page) {
        String[] paramNames = {
            KEY_PARAM,
            LANG_PARAM,
            PAGE_PARAM
        };
        String[] paramValues = {
            apiKey,
            language,
            String.valueOf(page)
        };
        URL url = buildURL(RES_POPULAR, paramNames, paramValues, paramNames.length);
        if(url != null) {
            doRequest(url);
        }
        return apiSuccess() ? new MovieResults(data) : null;
    }

    @Override
    public MovieResults getPopularMovies(String apiKey, String language) {
        return getPopularMovies(apiKey, language, DEFAULT_PAGE);
    }

    @Override
    public MovieResults getPopularMovies(String apiKey, int page) {
        return getPopularMovies(apiKey, DEFAULT_LANG, page);
    }

    @Override
    public MovieResults getPopularMovies(String apiKey) {
        return getPopularMovies(apiKey, DEFAULT_LANG, DEFAULT_PAGE);
    }

    @Override
    public VideoResults getMovieVideos(String apiKey, int movieId, String language) {
        String[] paramNames = {
            KEY_PARAM,
            LANG_PARAM
        };
        String[] paramValues = {
            apiKey,
            language
        };
        URL url = buildURL(
            String.valueOf(movieId).concat("/").concat(RES_VIDEOS),
            paramNames,
            paramValues,
            paramNames.length
        );
        if(url != null) {
            doRequest(url);
        }
        return apiSuccess() ? new VideoResults(data) : null;
    }

    @Override
    public VideoResults getMovieVideos(String apiKey, int movieId) {
        return getMovieVideos(apiKey, movieId, DEFAULT_LANG);
    }
}
