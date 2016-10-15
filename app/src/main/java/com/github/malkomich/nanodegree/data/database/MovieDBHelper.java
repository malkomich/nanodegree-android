package com.github.malkomich.nanodegree.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Movie database creator.
 */
public class MovieDBHelper extends SQLiteOpenHelper {

    // Version is required to be increased each time the database schema is updated.
    private static final int DB_VERSION = 2;

    static final String DB_NAME = "movie_db";

    public MovieDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + "(" +

            MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            MovieContract.MovieEntry.COL_TITLE + " TEXT NOT NULL, " +
            MovieContract.MovieEntry.COL_DESCRIPTION + " TEXT NOT NULL, " +
            MovieContract.MovieEntry.COL_DATE + " TEXT NOT NULL, " +
            MovieContract.MovieEntry.COL_POSTER_PATH + " TEXT, " +
            MovieContract.MovieEntry.COL_POPULARITY + " REAL DEFAULT 0.0, " +
            MovieContract.MovieEntry.COL_VOTE_COUNT + " INTEGER DEFAULT 0, " +
            MovieContract.MovieEntry.COL_VOTE_AVERAGE + " REAL DEFAULT 0.0 " +
            ");";

        final String SQL_CREATE_VIDEO_TABLE = "CREATE TABLE " + MovieContract.VideoEntry.TABLE_NAME + "(" +

            MovieContract.VideoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            MovieContract.VideoEntry.COL_MOVIE_ID + " INTEGER NOT NULL, " +
            MovieContract.VideoEntry.COL_KEY + " TEXT NOT NULL, " +
            MovieContract.VideoEntry.COL_TYPE + " TEXT NOT NULL, " +
            MovieContract.VideoEntry.COL_SITE + " TEXT NOT NULL, " +

            "FOREIGN KEY (" + MovieContract.VideoEntry.COL_MOVIE_ID + ") REFERENCES " +
            MovieContract.MovieEntry.TABLE_NAME + "(" + MovieContract.MovieEntry._ID + ") " +
            ");";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
        db.execSQL(SQL_CREATE_VIDEO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        final String SQL_DROP_MOVIE_TABLE = "DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME;
        final String SQL_DROP_VIDEO_TABLE = "DROP TABLE IF EXISTS " + MovieContract.VideoEntry.TABLE_NAME;

        db.execSQL(SQL_DROP_MOVIE_TABLE);
        db.execSQL(SQL_DROP_VIDEO_TABLE);
        onCreate(db);
    }
}
