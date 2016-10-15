package com.github.malkomich.nanodegree.data.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class MovieProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDBHelper mOpenHelper;

    static final int MOVIE = 100;
    static final int VIDEO_WITH_MOVIE_ID = 201;

    private static final SQLiteQueryBuilder sVideoByMovieIdQueryBuilder;

    static {
        sVideoByMovieIdQueryBuilder = new SQLiteQueryBuilder();

        // Inner JOIN:
        sVideoByMovieIdQueryBuilder.setTables(
            MovieContract.VideoEntry.TABLE_NAME + " INNER JOIN " +
                MovieContract.MovieEntry.TABLE_NAME +
                " ON " + MovieContract.VideoEntry.TABLE_NAME +
                "." + MovieContract.VideoEntry.COL_MOVIE_ID +
                " = " + MovieContract.MovieEntry.TABLE_NAME +
                "." + MovieContract.MovieEntry._ID);
    }

    // video.movie_id = ?
    private static final String sMovieIdSelection =
        MovieContract.VideoEntry.TABLE_NAME+
            "." + MovieContract.VideoEntry.COL_MOVIE_ID + " = ? ";

    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDBHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {

        final int match = sUriMatcher.match(uri);

        switch (match) {
            case MOVIE:
                return MovieContract.MovieEntry.CONTENT_TYPE;
            case VIDEO_WITH_MOVIE_ID:
                return MovieContract.VideoEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor responseCursor;
        switch (sUriMatcher.match(uri)) {
            case MOVIE:
                responseCursor = mOpenHelper.getReadableDatabase().query(
                    MovieContract.MovieEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder
                );
                break;
            // movie/#/video
            case VIDEO_WITH_MOVIE_ID:
                responseCursor = getVideosByMovieId(uri, projection, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        responseCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return responseCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri responseUri;
        long _id;

        switch (match) {
            case MOVIE:
                _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);
                if(_id > 0) {
                    responseUri = MovieContract.MovieEntry
                        .buildMovieUri(_id);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            case VIDEO_WITH_MOVIE_ID:
                _id = db.insert(MovieContract.VideoEntry.TABLE_NAME, null, values);
                if(_id > 0) {
                    responseUri = MovieContract.VideoEntry
                        .buildVideoUri(MovieContract.VideoEntry.getMovieIdFromUri(uri), _id);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver()
            .notifyChange(uri, null);
        db.close();
        return responseUri;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;

        selection = (selection != null) ? selection : "1";

        switch (match) {
            case MOVIE:
                rowsDeleted = db.delete(MovieContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case VIDEO_WITH_MOVIE_ID:
                rowsDeleted = db.delete(MovieContract.VideoEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if(rowsDeleted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        db.close();
        return rowsDeleted;
    }

    static UriMatcher buildUriMatcher() {
        // All paths added to the UriMatcher have a corresponding code to return when a match is
        // found.  The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, MovieContract.PATH_MOVIE, MOVIE);
        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/#/" + MovieContract.VideoEntry.PATH_VIDEO,
            VIDEO_WITH_MOVIE_ID
        );
        return matcher;
    }

    private Cursor getVideosByMovieId(Uri uri, String[] projection, String sortOrder) {
        long movieId = MovieContract.VideoEntry.getMovieIdFromUri(uri);
        return sVideoByMovieIdQueryBuilder.query(
            mOpenHelper.getReadableDatabase(),
            projection,
            sMovieIdSelection,
            new String[]{Long.toString(movieId)},
            null,
            null,
            sortOrder
        );
    }
}
