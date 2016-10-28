package com.github.malkomich.nanodegree.data.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashSet;

import static org.junit.Assert.*;

/**
 * Test for Movie database creation.
 */
@RunWith(AndroidJUnit4.class)
public class MovieDBHelperTest {

    private final Context mContext = InstrumentationRegistry.getTargetContext();

    @Before
    public void setUp() {
        mContext.deleteDatabase(MovieDBHelper.DB_NAME);
    }

    @Test
    public void onCreate() throws Exception {

        final HashSet<String> tableNameHashSet = new HashSet<>();
        tableNameHashSet.add(MovieContract.MovieEntry.TABLE_NAME);
        tableNameHashSet.add(MovieContract.VideoEntry.TABLE_NAME);

        mContext.deleteDatabase(MovieDBHelper.DB_NAME);
        SQLiteDatabase db = new MovieDBHelper(mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        // have we created the tables we want?
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly",
            cursor.moveToFirst());

        // verify that the tables have been created
        do {
            tableNameHashSet.remove(cursor.getString(0));
        } while( cursor.moveToNext() );

        // if this fails, it means that the database doesn't contain any entry tables
        assertTrue("Error: Your database was created without any entry tables",
            tableNameHashSet.isEmpty());

        // now, do our tables contain the correct columns?
        cursor = db.rawQuery("PRAGMA table_info(" + MovieContract.MovieEntry.TABLE_NAME + ")",
            null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
            cursor.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> movieColumnHashSet = new HashSet<>();
        movieColumnHashSet.add(MovieContract.MovieEntry._ID);
        movieColumnHashSet.add(MovieContract.MovieEntry.COL_TITLE);
        movieColumnHashSet.add(MovieContract.MovieEntry.COL_DESCRIPTION);
        movieColumnHashSet.add(MovieContract.MovieEntry.COL_DATE);
        movieColumnHashSet.add(MovieContract.MovieEntry.COL_POSTER_PATH);
        movieColumnHashSet.add(MovieContract.MovieEntry.COL_POPULARITY);
        movieColumnHashSet.add(MovieContract.MovieEntry.COL_VOTE_COUNT);
        movieColumnHashSet.add(MovieContract.MovieEntry.COL_VOTE_AVERAGE);

        int columnNameIndex = cursor.getColumnIndex("name");
        do {
            String columnName = cursor.getString(columnNameIndex);
            movieColumnHashSet.remove(columnName);
        } while(cursor.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required entry columns
        assertTrue("Error: The database doesn't contain all of the required entry columns",
            movieColumnHashSet.isEmpty());
        db.close();
    }

}