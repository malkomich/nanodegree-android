package com.github.malkomich.nanodegree.callback;

import android.database.Cursor;

/**
 * Listener for each item in a view collection.
 */
public interface OnDetailItemSelectedListener {

    /**
     * Triggered when an item is selected from an AdapterView.
     *
     * @param cursor data from DB query
     */
    void onItemSelected(Cursor cursor);
}
