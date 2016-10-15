package com.github.malkomich.nanodegree.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.runner.AndroidJUnit4;

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

    static final String TEST_MOVIE_TITLE = "Hello World!";
    static final String TEST_MOVIE_DESCRIPTION = "A dummy movie";
    static final String TEST_MOVIE_DATE = "15-10-2016";
    static final String TEST_VIDEO_KEY = "d9TpRfDdyU0";
    static final String TEST_VIDEO_TYPE = "Clip";
    static final String TEST_VIDEO_SITE = "YouTube";

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static ContentValues createDummyMovieValues() {

        ContentValues movieValues = new ContentValues();
        movieValues.put(MovieContract.MovieEntry.COL_TITLE, TEST_MOVIE_TITLE);
        movieValues.put(MovieContract.MovieEntry.COL_DESCRIPTION, TEST_MOVIE_DESCRIPTION);
        movieValues.put(MovieContract.MovieEntry.COL_DATE, TEST_MOVIE_DATE);

        return movieValues;
    }

    static ContentValues createDummyVideoValues(long movieRowId) {
        ContentValues weatherValues = new ContentValues();
        weatherValues.put(MovieContract.VideoEntry.COL_MOVIE_ID, movieRowId);
        weatherValues.put(MovieContract.VideoEntry.COL_KEY, TEST_VIDEO_KEY);
        weatherValues.put(MovieContract.VideoEntry.COL_TYPE, TEST_VIDEO_TYPE);
        weatherValues.put(MovieContract.VideoEntry.COL_SITE, TEST_VIDEO_SITE);

        return weatherValues;
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

    private static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                "' did not match the expected value '" +
                expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

}
