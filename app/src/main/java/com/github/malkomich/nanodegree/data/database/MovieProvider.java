package com.github.malkomich.nanodegree.data.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class MovieProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDBHelper mOpenHelper;

    static final int MOVIE = 100;
    static final int VIDEOS_WITH_MOVIE_ID = 201;

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
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {

        final int match = sUriMatcher.match(uri);

        switch (match) {
            case MOVIE:
                return MovieContract.MovieEntry.CONTENT_TYPE;
            case VIDEOS_WITH_MOVIE_ID:
                return MovieContract.VideoEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDBHelper(getContext());
        return true;
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
            case VIDEOS_WITH_MOVIE_ID:
                responseCursor = getVideosByMovieId(uri, projection, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        responseCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return responseCursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
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
            VIDEOS_WITH_MOVIE_ID);
        return matcher;
    }

    private Cursor getVideosByMovieId(Uri uri, String[] projection, String sortOrder) {
        long movieId = MovieContract.MovieEntry.getMovieIdFromUri(uri);
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
