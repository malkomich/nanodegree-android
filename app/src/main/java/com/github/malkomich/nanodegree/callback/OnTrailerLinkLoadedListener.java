package com.github.malkomich.nanodegree.callback;

import java.net.URL;

/**
 * Triggered when the trailer link of a movie is loaded.
 */
public interface OnTrailerLinkLoadedListener {

    void onTrailerLinkLoaded(URL trailerLink);
}
