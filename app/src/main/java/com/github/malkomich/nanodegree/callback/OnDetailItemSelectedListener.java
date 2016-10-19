package com.github.malkomich.nanodegree.callback;

import android.database.Cursor;

/**
 * Triggered when an item is selected from an AdapterView.
 */
public interface OnDetailItemSelectedListener {

    void onItemSelected(Cursor cursor);
}
