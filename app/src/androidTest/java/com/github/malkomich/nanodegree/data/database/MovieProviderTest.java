package com.github.malkomich.nanodegree.data.database;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.github.malkomich.nanodegree.data.database.TestUtilities.BULK_INSERT_RECORDS_TO_INSERT;
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

    @AfterClass
    public static void finish() {
        deleteAllRecords();
    }

    @Before
    public void setUp() {
        deleteAllRecords();
    }

    @Test
    public void getType() throws Exception {

        // content://com.github.malkomich.nanodegree/movie
        String type = mContext.getContentResolver().getType(MovieContract.MovieEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.github.malkomich.nanodegree/movie
        assertEquals("Error: the MovieEntry CONTENT_URI for popular movies should return MovieEntry.CONTENT_TYPE",
            MovieContract.MovieEntry.CONTENT_TYPE, type);

        // content://com.github.malkomich.nanodegree/movie/1/video
        type = mContext.getContentResolver().getType(MovieContract.VideoEntry.buildVideoUriWithMovieId(1));
        // vnd.android.cursor.dir/com.github.malkomich.nanodegree/video
        assertEquals("Error: the VideoEntry CONTENT_URI with movie id should return VideoEntry.CONTENT_TYPE",
            MovieContract.VideoEntry.CONTENT_TYPE, type);
    }

    @Test
    public void testBasicMovieQuery() {

        ContentValues testValues = TestUtilities.createDummyMovieValues();
        TestUtilities.insertDummyMovie(mContext);

        // Test the basic content provider query
        Cursor movieCursor = mContext.getContentResolver().query(
            MovieContract.MovieEntry.CONTENT_URI,
            TestUtilities.PROJECTION_MOVIE,
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
                MovieContract.MovieEntry.CONTENT_URI, movieCursor.getNotificationUri());
        }
    }

    @Test
    public void testBasicVideoQuery() {

        MovieDBHelper dbHelper = new MovieDBHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long movieRowId = TestUtilities.insertDummyMovie(mContext);

        // Fantastic.  Now that we have a movie, add a video!
        ContentValues videoValues = TestUtilities.createDummyVideoValues(movieRowId);

        long videoRowId = db.insert(MovieContract.VideoEntry.TABLE_NAME, null, videoValues);
        assertTrue("Unable to Insert VideoEntry into the Database", videoRowId != -1);

        db.close();

        // Test the basic content provider query
        Cursor videoCursor = mContext.getContentResolver().query(
            MovieContract.VideoEntry.buildVideoUriWithMovieId(movieRowId),
            TestUtilities.PROJECTION_VIDEO,
            null,
            null,
            null
        );

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicVideoQuery", videoCursor, videoValues);
    }

    @Test
    public void testInsertProvider() {
        ContentValues testValues = TestUtilities.createDummyMovieValues();

        // Register a content observer for our insert.  This time, directly with the content resolver
        TestUtilities.TestContentObserver observer = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(MovieContract.MovieEntry.CONTENT_URI, true, observer);
        Uri movieUri = mContext.getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, testValues);

        // Check if getContext().getContentResolver().notifyChange(uri, null) is being called;
        observer.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(observer);

        long movieRowId = ContentUris.parseId(movieUri);

        // Verify we got a row back.
        assertTrue(movieRowId != -1);

        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
            MovieContract.MovieEntry.CONTENT_URI,
            TestUtilities.PROJECTION_MOVIE,
            null,
            null,
            null
        );

        TestUtilities.validateCursor("testInsertProvider. Error validating MovieEntry.",
            cursor, testValues);

        // Add video
        ContentValues videoValues = TestUtilities.createDummyVideoValues(movieRowId);
        // The TestContentObserver is a one-shot class
        observer = TestUtilities.getTestContentObserver();

        mContext.getContentResolver()
            .registerContentObserver(MovieContract.VideoEntry.buildVideoUriWithMovieId(movieRowId), true, observer);

        Uri videoInsertUri = mContext.getContentResolver()
            .insert(MovieContract.VideoEntry.buildVideoUriWithMovieId(movieRowId), videoValues);
        assertTrue(videoInsertUri != null);

        // Check if getContext().getContentResolver().notifyChange(uri, null) is being called;
        observer.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(observer);

        // A cursor is your primary interface to the query results.
        Cursor videoCursor = mContext.getContentResolver().query(
            MovieContract.VideoEntry.buildVideoUriWithMovieId(movieRowId),
            TestUtilities.PROJECTION_VIDEO,
            null,
            null,
            null
        );

        TestUtilities.validateCursor("testInsertProvider. Error validating VideoEntry insert.",
            videoCursor, videoValues);
    }

    @Test
    public void testUpdateMovie() {

        // Create a new map of values, where column names are the keys
        ContentValues values = TestUtilities.createDummyMovieValues();

        Uri movieUri = mContext.getContentResolver().
            insert(MovieContract.MovieEntry.CONTENT_URI, values);
        long movieRowId = ContentUris.parseId(movieUri);

        // Verify we got a row back.
        assertTrue(movieRowId != -1);

        ContentValues updatedValues = new ContentValues(values);
        updatedValues.put(MovieContract.MovieEntry._ID, movieRowId);
        updatedValues.put(MovieContract.MovieEntry.COL_TITLE, "Deadpool");

        // Create a cursor with observer to make sure that the content provider is notifying
        // the observers as expected
        Cursor movieCursor = mContext.getContentResolver().query(
            MovieContract.MovieEntry.CONTENT_URI,
            TestUtilities.PROJECTION_MOVIE,
            null,
            null,
            null
        );

        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        movieCursor.registerContentObserver(tco);

        int count = mContext.getContentResolver().update(
            MovieContract.MovieEntry.CONTENT_URI, updatedValues, MovieContract.MovieEntry._ID + "= ?",
            new String[] { Long.toString(movieRowId)});
        assertEquals(1, count);

        // Test to make sure our observer is called.  If not, we throw an assertion.
        //
        // Students: If your code is failing here, it means that your content provider
        // isn't calling getContext().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();

        movieCursor.unregisterContentObserver(tco);
        movieCursor.close();

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
            MovieContract.MovieEntry.CONTENT_URI,
            TestUtilities.PROJECTION_MOVIE,
            MovieContract.MovieEntry._ID + " = " + movieRowId,
            null,
            null
        );

        TestUtilities.validateCursor("testUpdateMovie.  Error validating movie entry update.",
            cursor, updatedValues);

        cursor.close();
    }

    @Test
    public void testBulkInsertVideos() {

        ContentValues testValues = TestUtilities.createDummyMovieValues();
        Uri movieUri = mContext.getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, testValues);
        long movieRowId = ContentUris.parseId(movieUri);

        // Verify we got a row back.
        assertTrue(movieRowId != -1);

        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
            MovieContract.MovieEntry.CONTENT_URI,
            TestUtilities.PROJECTION_MOVIE,
            null,
            null,
            null
        );

        TestUtilities.validateCursor("testBulkInsert. Error validating MovieEntry.",
            cursor, testValues);

        ContentValues[] bulkInsertContentValues = TestUtilities.createBulkInsertVideoValues(movieRowId);

        // Register a content observer for our bulk insert.
        TestUtilities.TestContentObserver videoObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver()
            .registerContentObserver(MovieContract.VideoEntry.CONTENT_URI, true, videoObserver);

        int insertCount = mContext.getContentResolver()
            .bulkInsert(MovieContract.VideoEntry.CONTENT_URI, bulkInsertContentValues);

        // Check if `getContext().getContentResolver().notifyChange(uri, null)´ is being called.
        videoObserver.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(videoObserver);

        assertEquals(BULK_INSERT_RECORDS_TO_INSERT, insertCount);

        // A cursor is your primary interface to the query results.
        cursor = mContext.getContentResolver().query(
            MovieContract.VideoEntry.buildVideoUriWithMovieId(movieRowId),
            TestUtilities.PROJECTION_VIDEO,
            null,
            null,
            null
        );

        // we should have as many records in the database as we've inserted
        assertEquals(BULK_INSERT_RECORDS_TO_INSERT, cursor.getCount());

        // and let's make sure they match the ones we created
        cursor.moveToFirst();
        for ( int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++, cursor.moveToNext() ) {
            TestUtilities.validateCurrentRecord("testBulkInsert.  Error validating VideoEntry " + i,
                cursor, bulkInsertContentValues[i]);
        }
        cursor.close();
    }

    @Test
    public void testBulkInsertMovies() {

        ContentValues[] bulkInsertContentValues = TestUtilities.createBulkInsertMovieValues();

        // Register a content observer for our bulk insert.
        TestUtilities.TestContentObserver movieObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver()
            .registerContentObserver(MovieContract.MovieEntry.CONTENT_URI, true, movieObserver);

        int insertCount = mContext.getContentResolver()
            .bulkInsert(MovieContract.MovieEntry.CONTENT_URI, bulkInsertContentValues);

        // Check if `getContext().getContentResolver().notifyChange(uri, null)´ is being called.
        movieObserver.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(movieObserver);

        assertEquals(BULK_INSERT_RECORDS_TO_INSERT, insertCount);

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
            MovieContract.MovieEntry.CONTENT_URI,
            TestUtilities.PROJECTION_MOVIE,
            null,
            null,
            null
        );

        // we should have as many records in the database as we've inserted
        assertEquals(BULK_INSERT_RECORDS_TO_INSERT, cursor.getCount());

        // and let's make sure they match the ones we created
        cursor.moveToFirst();
        for ( int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++, cursor.moveToNext() ) {
            TestUtilities.validateCurrentRecord("testBulkInsert.  Error validating MovieEntry " + i,
                cursor, bulkInsertContentValues[i]);
        }
        cursor.close();
    }

    private static void deleteAllRecords() {

        mContext.getContentResolver().delete(
            MovieContract.VideoEntry.CONTENT_URI,
            null,
            null
        );
        mContext.getContentResolver().delete(
            MovieContract.MovieEntry.CONTENT_URI,
            null,
            null
        );

        Cursor cursor = mContext.getContentResolver().query(
            MovieContract.VideoEntry.CONTENT_URI,
            null,
            null,
            null,
            null
        );
        assertEquals("Error: Records not deleted from Video table during delete", 0, cursor.getCount());
        cursor.close();

        cursor = mContext.getContentResolver().query(
            MovieContract.MovieEntry.CONTENT_URI,
            null,
            null,
            null,
            null
        );
        assertEquals("Error: Records not deleted from Movie table during delete", 0, cursor.getCount());
        cursor.close();
    }

}