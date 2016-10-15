package com.github.malkomich.nanodegree.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Test for Movie provider behaviour.
 */
@RunWith(AndroidJUnit4.class)
public class MovieProviderTest {

    private static final String TAG = MovieProviderTest.class.getSimpleName();

    private static Context mContext;

    @BeforeClass
    public static void init() {
        mContext = InstrumentationRegistry.getTargetContext();
    }

    @Test
    public void getType() throws Exception {

        // content://com.github.malkomich.nanodegree/movie
        String type = mContext.getContentResolver().getType(MovieContract.MovieEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.github.malkomich.nanodegree/movie
        assertEquals("Error: the MovieEntry CONTENT_URI for popular movies should return MovieEntry.CONTENT_TYPE",
            MovieContract.MovieEntry.CONTENT_TYPE, type);

        // content://com.github.malkomich.nanodegree/movie/1/video
        type = mContext.getContentResolver().getType(MovieContract.VideoEntry.buildVideoUri(1));
        // vnd.android.cursor.dir/com.github.malkomich.nanodegree/video
        assertEquals("Error: the VideoEntry CONTENT_URI with movie id should return VideoEntry.CONTENT_TYPE",
            MovieContract.VideoEntry.CONTENT_TYPE, type);
    }

    public void testBasicMovieQuery() {

        MovieDBHelper dbHelper = new MovieDBHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = TestUtilities.createDummyMovieValues();
        TestUtilities.insertDummyMovie(mContext);

        // Test the basic content provider query
        Cursor movieCursor = mContext.getContentResolver().query(
            MovieContract.MovieEntry.CONTENT_URI,
            null,
            null,
            null,
            null
        );

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicMovieQuery, movie query", movieCursor, testValues);

        // Has the NotificationUri been set correctly? --- we can only test this easily against API
        // level 19 or greater because getNotificationUri was added in API level 19.
        if ( Build.VERSION.SDK_INT >= 19 ) {
            assertEquals("Error: Movie Query did not properly set NotificationUri",
                movieCursor.getNotificationUri(), MovieContract.MovieEntry.CONTENT_URI);
        }
    }

    public void testBasicVideoQuery() {

        MovieDBHelper dbHelper = new MovieDBHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long movieRowId = TestUtilities.insertDummyMovie(mContext);

        // Fantastic.  Now that we have a location, add some weather!
        ContentValues videoValues = TestUtilities.createDummyVideoValues(movieRowId);

        long videoRowId = db.insert(MovieContract.VideoEntry.TABLE_NAME, null, videoValues);
        assertTrue("Unable to Insert VideoEntry into the Database", videoRowId != -1);

        db.close();

        // Test the basic content provider query
        Cursor videoCursor = mContext.getContentResolver().query(
            MovieContract.VideoEntry.CONTENT_URI,
            null,
            null,
            null,
            null
        );

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicVideoQuery", videoCursor, videoValues);
    }

}