package com.github.malkomich.nanodegree.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.github.malkomich.nanodegree.util.PollingCheck;

import org.junit.runner.RunWith;

import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Util functions for database tests, like filling DB with testing values.
 */
@RunWith(AndroidJUnit4.class)
public class TestUtilities {

    static final int BULK_INSERT_RECORDS_TO_INSERT = 5;
    static final String[] PROJECTION_MOVIE = {
        MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry._ID,
        MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry.COL_API_ID,
        MovieContract.MovieEntry.COL_TITLE,
        MovieContract.MovieEntry.COL_DESCRIPTION,
        MovieContract.MovieEntry.COL_DATE,
        MovieContract.MovieEntry.COL_POSTER_PATH,
        MovieContract.MovieEntry.COL_POPULARITY,
        MovieContract.MovieEntry.COL_VOTE_COUNT,
        MovieContract.MovieEntry.COL_VOTE_AVERAGE
    };
    static final String[] PROJECTION_VIDEO = {
        MovieContract.VideoEntry.TABLE_NAME + "." + MovieContract.VideoEntry._ID,
        MovieContract.VideoEntry.TABLE_NAME + "." + MovieContract.VideoEntry.COL_API_ID,
        MovieContract.VideoEntry.COL_MOVIE_ID,
        MovieContract.VideoEntry.COL_KEY,
        MovieContract.VideoEntry.COL_TYPE,
        MovieContract.VideoEntry.COL_SITE
    };

    private static final String TEST_MOVIE_TITLE = "Hello World!";
    private static final String TEST_MOVIE_DESCRIPTION = "A dummy movie";
    private static final String TEST_MOVIE_DATE = "15-10-2016";
    private static final String TEST_VIDEO_KEY = "d9TpRfDdyU0";
    private static final String TEST_VIDEO_TYPE_CLIP = "Clip";
    private static final String TEST_VIDEO_TYPE_TRAILER = "Trailer";
    private static final String TEST_VIDEO_SITE = "YouTube";

    private static final String[] TEST_VIDEO_KEYS_ARRAY = {
        "9vN6DHB6bJc", "8rvYrVTnSWw", "NjL5BrYD-w0", "J_WhlatQgEc", "s7EdQ4FqbhY"
    };

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                "' did not match the expected value '" +
                valueCursor.getString(idx) + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

    static ContentValues createDummyMovieValues() {

        ContentValues movieValues = new ContentValues();
        movieValues.put(MovieContract.MovieEntry.COL_API_ID, 1);
        movieValues.put(MovieContract.MovieEntry.COL_TITLE, TEST_MOVIE_TITLE);
        movieValues.put(MovieContract.MovieEntry.COL_DESCRIPTION, TEST_MOVIE_DESCRIPTION);
        movieValues.put(MovieContract.MovieEntry.COL_DATE, TEST_MOVIE_DATE);

        return movieValues;
    }

    static ContentValues createDummyVideoValues(long movieRowId) {

        ContentValues videoValues = new ContentValues();
        videoValues.put(MovieContract.VideoEntry.COL_API_ID, "1");
        videoValues.put(MovieContract.VideoEntry.COL_MOVIE_ID, movieRowId);
        videoValues.put(MovieContract.VideoEntry.COL_KEY, TEST_VIDEO_KEY);
        videoValues.put(MovieContract.VideoEntry.COL_TYPE, TEST_VIDEO_TYPE_CLIP);
        videoValues.put(MovieContract.VideoEntry.COL_SITE, TEST_VIDEO_SITE);

        return videoValues;
    }

    static ContentValues[] createBulkInsertMovieValues() {
        ContentValues[] returnContentValues = new ContentValues[BULK_INSERT_RECORDS_TO_INSERT];

        for ( int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++ ) {
            ContentValues movieValues = new ContentValues();
            movieValues.put(MovieContract.MovieEntry.COL_API_ID, i + 1);
            movieValues.put(MovieContract.MovieEntry.COL_TITLE, TEST_MOVIE_TITLE + " " + String.valueOf(i));
            movieValues.put(MovieContract.MovieEntry.COL_DESCRIPTION, TEST_MOVIE_DESCRIPTION);
            movieValues.put(MovieContract.MovieEntry.COL_DATE, TEST_MOVIE_DATE);
            returnContentValues[i] = movieValues;
        }
        return returnContentValues;
    }

    static ContentValues[] createBulkInsertVideoValues(long movieRowId) {
        ContentValues[] returnContentValues = new ContentValues[BULK_INSERT_RECORDS_TO_INSERT];

        for ( int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++ ) {
            ContentValues videoValues = new ContentValues();
            videoValues.put(MovieContract.VideoEntry.COL_API_ID, String.valueOf(i + 1));
            videoValues.put(MovieContract.VideoEntry.COL_MOVIE_ID, movieRowId);
            videoValues.put(MovieContract.VideoEntry.COL_KEY, TEST_VIDEO_KEYS_ARRAY[i]);
            videoValues.put(MovieContract.VideoEntry.COL_TYPE, TEST_VIDEO_TYPE_TRAILER);
            videoValues.put(MovieContract.VideoEntry.COL_SITE, TEST_VIDEO_SITE);
            returnContentValues[i] = videoValues;
        }
        return returnContentValues;
    }

    static long insertDummyMovie(Context context) {
        // insert our test records into the database
        MovieDBHelper dbHelper = new MovieDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = createDummyMovieValues();

        long movieRowId = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue("Error: Failure to insert Dummy Movie Values", movieRowId != -1);

        return movieRowId;
    }

    static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged;

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        // On earlier versions of Android, this onChange method is called
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }

        public void waitForNotificationOrFail() {
            // Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
            // It's useful to look at the Android CTS source for ideas on how to test your Android
            // applications.  The reason that PollingCheck works is that, by default, the JUnit
            // testing framework is not running on the main Android application thread.
            new PollingCheck(5000) {
                @Override
                protected boolean check() {
                    return mContentChanged;
                }
            }.run();
            mHT.quit();
        }
    }

    static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }

}
