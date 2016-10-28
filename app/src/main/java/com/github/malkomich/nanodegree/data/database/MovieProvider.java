package com.github.malkomich.nanodegree.data.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import static android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE;

public class MovieProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDBHelper mOpenHelper;

    private static final int MOVIE = 100;
    private static final int MOVIE_DETAILS = 101;
    private static final int VIDEO = 200;
    private static final int REVIEW = 300;

    // movie._id = ?
    private static final String sMovieIdSelection =
        MovieContract.MovieEntry.TABLE_NAME+
            "." + MovieContract.MovieEntry._ID + " = ? ";

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
            case MOVIE_DETAILS:
                return MovieContract.MovieEntry.CONTENT_ITEM_TYPE;
            case VIDEO:
                return MovieContract.VideoEntry.CONTENT_TYPE;
            case REVIEW:
                return MovieContract.ReviewEntry.CONTENT_TYPE;
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
            case MOVIE_DETAILS:
                responseCursor = getMovieDetailsByMovieId(uri, projection, sortOrder);
                break;
            case VIDEO:
                responseCursor = mOpenHelper.getReadableDatabase().query(
                    MovieContract.VideoEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder
                );
                break;
            case REVIEW:
                responseCursor = mOpenHelper.getReadableDatabase().query(
                    MovieContract.ReviewEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder
                );
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
            case VIDEO:
                _id = db.insert(MovieContract.VideoEntry.TABLE_NAME, null, values);
                if(_id > 0) {
                    responseUri = MovieContract.VideoEntry
                        .buildVideoUri(_id);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            case REVIEW:
                _id = db.insert(MovieContract.ReviewEntry.TABLE_NAME, null, values);
                if(_id > 0) {
                    responseUri = MovieContract.ReviewEntry
                        .buildReviewUri(_id);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver()
            .notifyChange(uri, null);
        return responseUri;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        selection = (selection != null) ? selection : "1";

        switch (match) {
            case MOVIE:
                rowsUpdated = db.update(MovieContract.MovieEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case VIDEO:
                rowsUpdated = db.update(MovieContract.VideoEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case REVIEW:
                rowsUpdated = db.update(MovieContract.ReviewEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if(rowsUpdated > 0) {
            getContext().getContentResolver()
                .notifyChange(uri, null);
        }
        return rowsUpdated;
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
            case VIDEO:
                rowsDeleted = db.delete(MovieContract.VideoEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case REVIEW:
                rowsDeleted = db.delete(MovieContract.ReviewEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if(rowsDeleted > 0) {
            getContext().getContentResolver()
                .notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        int rowsInserted = 0;
        switch (match) {
            case MOVIE:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insertWithOnConflict(
                            MovieContract.MovieEntry.TABLE_NAME, null, value, CONFLICT_REPLACE);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                break;
            case VIDEO:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insertWithOnConflict(
                            MovieContract.VideoEntry.TABLE_NAME, null, value, CONFLICT_REPLACE);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                break;
            case REVIEW:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insertWithOnConflict(
                            MovieContract.ReviewEntry.TABLE_NAME, null, value, CONFLICT_REPLACE);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                break;
            default:
                return super.bulkInsert(uri, values);
        }

        getContext().getContentResolver()
            .notifyChange(uri, null);
        return rowsInserted;
    }

    private static UriMatcher buildUriMatcher() {
        // All paths added to the UriMatcher have a corresponding code to return when a match is
        // found.  The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, MovieContract.PATH_MOVIE, MOVIE);
        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/#", MOVIE_DETAILS);
        matcher.addURI(authority, MovieContract.PATH_VIDEO, VIDEO);
        matcher.addURI(authority, MovieContract.PATH_REVIEW, REVIEW);
        return matcher;
    }

    private Cursor getMovieDetailsByMovieId(Uri uri, String[] projection, String sortOrder) {
        long movieId = MovieContract.MovieEntry.getMovieIdFromUri(uri);

        SQLiteQueryBuilder sMovieDetailsQueryBuilder = new SQLiteQueryBuilder();

        String[] resources = MovieContract.MovieEntry.getAppendResourcesFromUri(uri);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(MovieContract.MovieEntry.TABLE_NAME);
        for(String res: resources) {
            String join = null;
            switch (res) {
                case "videos":
                    join = MovieContract.VideoEntry.TABLE_NAME + " ON " +
                        MovieContract.VideoEntry.TABLE_NAME    + "." + MovieContract.VideoEntry.COL_MOVIE_ID;
                    break;
                case "reviews":
                    join = MovieContract.ReviewEntry.TABLE_NAME + " ON " +
                        MovieContract.ReviewEntry.TABLE_NAME    + "." + MovieContract.ReviewEntry.COL_MOVIE_ID;
                    break;
                default:
                    break;
            }
            if(join != null) {
                stringBuilder.append(" LEFT OUTER JOIN ")
                    .append(join)
                    .append(" = " +  MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry._ID);
            }
        }
        sMovieDetailsQueryBuilder.setTables(stringBuilder.toString());

        return sMovieDetailsQueryBuilder.query(
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
