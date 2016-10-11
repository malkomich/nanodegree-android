package com.github.malkomich.nanodegree.domain;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Movie model tests
 */
public class MovieTest {

    private final static String DATA_SAMPLE = "{\"poster_path\":\"\\/uSHjeRVuObwdpbECaXJnvyDoeJK" +
        ".jpg\",\"adult\":false,\"overview\":\"A teenager finds himself transported to an island where he must help " +
        "protect a group of orphans with special powers from creatures intent on destroying them.\"," +
        "\"release_date\":\"2016-09-28\",\"genre_ids\":[14],\"id\":283366,\"original_title\":\"Miss Peregrine's Home " +
        "for Peculiar Children\",\"original_language\":\"en\",\"title\":\"Miss Peregrine's Home for Peculiar " +
        "Children\",\"backdrop_path\":\"\\/qXQinDhDZkTiqEGLnav0h1YSUu8.jpg\",\"popularity\":26.881577," +
        "\"vote_count\":143,\"video\":false,\"vote_average\":6.37}";

    @Test
    public void testMovieBuild() throws JSONException {
        JSONObject json = new JSONObject(DATA_SAMPLE);
        Movie movie = new Movie(json);
        assertEquals(26.881577, movie.getPopularity(), 0.000001);
        assertEquals(143, movie.getVoteCount());
    }

}