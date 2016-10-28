package com.github.malkomich.nanodegree.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Movie database creator.
 */
public class MovieDBHelper extends SQLiteOpenHelper {

    /*
     * Version is required(IN PRODUCTION) to be increased each time the database schema is updated.
     * While the app is in debug mode, is enough to remove data from device.
     */
    private static final int DB_VERSION = 2;

    static final String DB_NAME = "movie_db";

    public MovieDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /* (non-Javadoc)
     * @see android.database.sqlite.SQLiteOpenHelper#onCreate()
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + "(" +

            MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            MovieContract.MovieEntry.COL_API_ID + " INTEGER NOT NULL UNIQUE, " +
            MovieContract.MovieEntry.COL_TITLE + " TEXT NOT NULL, " +
            MovieContract.MovieEntry.COL_DESCRIPTION + " TEXT NOT NULL, " +
            MovieContract.MovieEntry.COL_DATE + " TEXT NOT NULL, " +
            MovieContract.MovieEntry.COL_POSTER_PATH + " TEXT, " +
            MovieContract.MovieEntry.COL_POPULARITY + " REAL DEFAULT 0.0, " +
            MovieContract.MovieEntry.COL_VOTE_COUNT + " INTEGER DEFAULT 0, " +
            MovieContract.MovieEntry.COL_VOTE_AVERAGE + " REAL DEFAULT 0.0, " +
            MovieContract.MovieEntry.COL_DURATION + " INTEGER DEFAULT 0, " +
            MovieContract.MovieEntry.COL_FAVORITE + " INTEGER DEFAULT 0, " +
            MovieContract.MovieEntry.COL_UPDATE_DATE + " INTEGER DEFAULT 0 " +
            ");";

        final String SQL_CREATE_VIDEO_TABLE = "CREATE TABLE " + MovieContract.VideoEntry.TABLE_NAME + "(" +

            MovieContract.VideoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            MovieContract.VideoEntry.COL_API_ID + " TEXT NOT NULL UNIQUE, " +
            MovieContract.VideoEntry.COL_MOVIE_ID + " INTEGER NOT NULL, " +
            MovieContract.VideoEntry.COL_KEY + " TEXT NOT NULL, " +
            MovieContract.VideoEntry.COL_TYPE + " TEXT NOT NULL, " +
            MovieContract.VideoEntry.COL_SITE + " TEXT NOT NULL, " +

            "FOREIGN KEY (" + MovieContract.VideoEntry.COL_MOVIE_ID + ") REFERENCES " +
            MovieContract.MovieEntry.TABLE_NAME + "(" + MovieContract.MovieEntry._ID + ") " +
            ");";

        final String SQL_CREATE_REVIEW_TABLE = "CREATE TABLE " + MovieContract.ReviewEntry.TABLE_NAME + "(" +

            MovieContract.ReviewEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            MovieContract.ReviewEntry.COL_API_ID + " TEXT NOT NULL UNIQUE, " +
            MovieContract.ReviewEntry.COL_MOVIE_ID + " INTEGER NOT NULL, " +
            MovieContract.ReviewEntry.COL_AUTHOR + " TEXT NOT NULL, " +
            MovieContract.ReviewEntry.COL_CONTENT + " TEXT NOT NULL, " +
            MovieContract.ReviewEntry.COL_URL + " TEXT, " +

            "FOREIGN KEY (" + MovieContract.ReviewEntry.COL_MOVIE_ID + ") REFERENCES " +
            MovieContract.MovieEntry.TABLE_NAME + "(" + MovieContract.MovieEntry._ID + ") " +
            ");";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
        db.execSQL(SQL_CREATE_VIDEO_TABLE);
        db.execSQL(SQL_CREATE_REVIEW_TABLE);
    }

    /* (non-Javadoc)
     * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade()
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        final String SQL_DROP_MOVIE_TABLE = "DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME;
        final String SQL_DROP_VIDEO_TABLE = "DROP TABLE IF EXISTS " + MovieContract.VideoEntry.TABLE_NAME;
        final String SQL_DROP_REVIEW_TABLE = "DROP TABLE IF EXISTS " + MovieContract.ReviewEntry.TABLE_NAME;

        db.execSQL(SQL_DROP_MOVIE_TABLE);
        db.execSQL(SQL_DROP_VIDEO_TABLE);
        db.execSQL(SQL_DROP_REVIEW_TABLE);
        onCreate(db);
    }
}
