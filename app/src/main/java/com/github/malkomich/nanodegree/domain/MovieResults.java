package com.github.malkomich.nanodegree.domain;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Model which represents a page of movies.
 */
public class MovieResults {

    private static final String MOVIES = "results";
    private static final String PAGE = "page";

    private List<Movie> movies = new ArrayList<>();
    private int page;

    public MovieResults(JSONObject json) {

        page = json.optInt(PAGE, 0);

        try {
            JSONArray moviesJSONArray = json.getJSONArray(MOVIES);

            for (int i = 0; i < moviesJSONArray.length(); i++) {
                Movie movie = new Movie(moviesJSONArray.getJSONObject(i));
                movies.add(movie);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public int getPage() {
        return page;
    }

}
