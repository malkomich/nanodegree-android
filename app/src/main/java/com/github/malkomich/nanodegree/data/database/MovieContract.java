package com.github.malkomich.nanodegree.data.database;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Defines table & column names for the movie local database.
 */
public class MovieContract {

    // Content provider identifier
    public static final String CONTENT_AUTHORITY = "com.github.malkomich.nanodegree";
    // Base Uri to locate the content provider
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Resources paths which can be accessed by the content provider
    public static final String PATH_MOVIE = "movie";
    public static final String PATH_VIDEO = "video";
    public static final String PATH_REVIEW = "review";

    /**
     * Inner class that defines the table contents of the movie table
     */
    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
            .appendPath(PATH_MOVIE)
            .build();

        public static final String PATH_APPENDED_RESOURCES = "append_to_response";

        public static final String CONTENT_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        // Database table name
        public static final String TABLE_NAME = "movie";

        // Database columns name
        public static final String COL_API_ID = "api_id";
        public static final String COL_TITLE = "title";
        public static final String COL_DESCRIPTION = "overview";
        public static final String COL_DATE = "release_date";
        public static final String COL_POSTER_PATH = "poster_path";
        public static final String COL_POPULARITY = "popularity";
        public static final String COL_VOTE_COUNT = "vote_count";
        public static final String COL_VOTE_AVERAGE = "vote_average";
        public static final String COL_DURATION = "duration";
        public static final String COL_FAVORITE = "favorite";
        public static final String COL_UPDATE_DATE = "update_date";

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildMovieDetailsJoinVideoWithMovieId(long movieId) {
            return buildMovieUri(movieId).buildUpon()
                .appendQueryParameter(PATH_APPENDED_RESOURCES, "videos")
                .build();
        }

        public static Uri buildMovieDetailsJoinReviewWithMovieId(long movieId) {
            return buildMovieUri(movieId).buildUpon()
                .appendQueryParameter(PATH_APPENDED_RESOURCES, "reviews")
                .build();
        }

        public static long getMovieIdFromUri(Uri uri) {
            String idString = uri.getPathSegments().get(1);
            return idString != null ? Long.parseLong(idString) : 0;
        }

        public static String[] getAppendResourcesFromUri(Uri uri) {
            String resources = uri.getQueryParameter(PATH_APPENDED_RESOURCES);
            return resources != null ? resources.split(",") : null;
        }
    }

    /**
     * Inner class that defines the table contents of the video table
     */
    public static final class VideoEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
            .appendPath(PATH_VIDEO)
            .build();

        public static final String CONTENT_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VIDEO;
        public static final String CONTENT_ITEM_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VIDEO;


        // Database table name
        public static final String TABLE_NAME = "video";

        // Database columns name
        public static final String COL_API_ID = "api_id";
        public static final String COL_MOVIE_ID = "movie_id";
        public static final String COL_KEY = "video_key";
        public static final String COL_TYPE = "video_type";
        public static final String COL_SITE = "publish_site";

        public static Uri buildVideoUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildVideoUriWithMovieId(long movieId) {
            return CONTENT_URI.buildUpon()
                .appendQueryParameter(COL_MOVIE_ID, String.valueOf(movieId))
                .build();
        }

    }

    /**
     * Inner class that defines the table contents of the review table
     */
    public static final class ReviewEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
            .appendPath(PATH_REVIEW)
            .build();

        public static final String CONTENT_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEW;
        public static final String CONTENT_ITEM_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEW;


        // Database table name
        public static final String TABLE_NAME = "review";

        // Database columns name
        public static final String COL_API_ID = "api_id";
        public static final String COL_MOVIE_ID = "movie_id";
        public static final String COL_AUTHOR = "author";
        public static final String COL_CONTENT = "content";
        public static final String COL_URL = "url";

        public static Uri buildReviewUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

}
