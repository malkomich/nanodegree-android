package com.github.malkomich.nanodegree.data.database;

import android.provider.BaseColumns;

/**
 * Defines table & column names for the movie local database.
 */
public class MovieContract {

    /**
     * Inner class that defines the table contents of the movie table
     */
    public static final class MovieEntry implements BaseColumns {

        public static final String TABLE_NAME = "movie";

        public static final String COL_TITLE = "title";
        public static final String COL_DESCRIPTION = "overview";
        public static final String COL_DATE = "release_date";
        public static final String COL_POSTER_PATH = "poster_path";
        public static final String COL_POPULARITY = "popularity";
        public static final String COL_VOTE_COUNT = "vote_count";
        public static final String COL_VOTE_AVERAGE = "vote_average";

    }

    /**
     * Inner class that defines the table contents of the video table
     */
    public static final class VideoEntry implements BaseColumns {

        public static final String TABLE_NAME = "video";

        public static final String COL_MOVIE_ID = "id";
        public static final String COL_KEY = "key";
        public static final String COL_TYPE = "type";
        public static final String COL_SITE = "site";

    }

}
