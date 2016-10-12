package com.github.malkomich.nanodegree.domain;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Model which represents the list of available videos of a specific movie.
 */
public class VideoResults {

    private static final String ID = "id";
    private static final String VIDEOS = "results";

    private int movieId;
    private List<Video> videos = new ArrayList<>();

    public VideoResults(JSONObject json) {

        movieId = json.optInt(ID, 0);

        try {
            JSONArray videosJSONArray = json.getJSONArray(VIDEOS);

            for (int i = 0; i < videosJSONArray.length(); i++) {
                Video video = new Video(videosJSONArray.getJSONObject(i));
                videos.add(video);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public List<Video> getVideos() {
        return videos;
    }

    public URL getTrailerLink() {
        for(Video video : videos) {
            if(Video.VideoType.TRAILER.equals(video.getType()) && Video.SITE_YOUTUBE.equals(video.getSite())) {
                return video.getURL();
            }
        }
        return null;
    }
}
