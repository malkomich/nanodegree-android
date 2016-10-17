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
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Resources paths which can be accessed by the content provider
    public static final String PATH_MOVIE = "movie";
    public static final String PATH_VIDEO = "video";

    /**
     * Inner class that defines the table contents of the movie table
     */
    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
            .appendPath(PATH_MOVIE)
            .build();

        public static final String CONTENT_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        // Database table name
        public static final String TABLE_NAME = "movie";

        // Database columns name
        public static final String COL_TITLE = "title";
        public static final String COL_DESCRIPTION = "overview";
        public static final String COL_DATE = "release_date";
        public static final String COL_POSTER_PATH = "poster_path";
        public static final String COL_POPULARITY = "popularity";
        public static final String COL_VOTE_COUNT = "vote_count";
        public static final String COL_VOTE_AVERAGE = "vote_average";

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
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

        public static long getMovieIdFromUri(Uri uri) {
            String movieIdString = uri.getQueryParameter(COL_MOVIE_ID);
            return movieIdString != null ? Long.parseLong(movieIdString) : 0;
        }

    }

}
