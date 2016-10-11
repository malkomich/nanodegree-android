package com.github.malkomich.nanodegree.domain;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Movie results model tests.
 */
public class MovieResultsTest {

    private final static String DATA_SAMPLE = "{\"page\":3,\"results\":[{\"poster_path" +
        "\":\"\\/5N20rQURev5CNDcMjHVUZhpoCNC.jpg\",\"adult\":false,\"overview\":\"Following the events of Age of " +
        "Ultron, the collective governments...\",\"release_date\":\"2016-04-27\",\"genre_ids\":[28,878," +
        "53],\"id\":271110,\"original_title\":\"Captain America: Civil War\",\"original_language\":\"en\"," +
        "\"title\":\"Captain America: Civil War\",\"backdrop_path\":\"\\/m5O3SZvQ6EgD5XXXLPIP1wLppeW.jpg\"," +
        "\"popularity\":27.052977,\"vote_count\":3172,\"video\":false,\"vote_average\":6.77}, {\"poster_path" +
        "\":\"\\/uSHjeRVuObwdpbECaXJnvyDoeJK.jpg\",\"adult\":false,\"overview\":\"A teenager finds himself " +
        "transported...\",\"release_date\":\"2016-09-28\",\"genre_ids\":[14],\"id\":283366," +
        "\"original_title\":\"Miss Peregrine's Home for Peculiar Children\",\"original_language\":\"en\"," +
        "\"title\":\"Miss Peregrine's Home for Peculiar Children\",\"backdrop_path\":\"\\/qXQinDhDZkTiqEGLnav0h1YSUu8" +
        ".jpg\",\"popularity\":26.881577,\"vote_count\":143,\"video\":false,\"vote_average\":6.37}]," +
        "\"total_results\":19602," +
        "\"total_pages\":981}";

    @Test
    public void testMovieResultsBuild() throws JSONException {
        JSONObject json = new JSONObject(DATA_SAMPLE);
        MovieResults results = new MovieResults(json);
        assertEquals(3, results.getPage());
        assertEquals(2, results.getMovies().size());
    }
}