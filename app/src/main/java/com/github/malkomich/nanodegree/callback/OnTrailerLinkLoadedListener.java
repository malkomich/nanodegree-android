package com.github.malkomich.nanodegree.callback;

import com.github.malkomich.nanodegree.domain.VideoResults;

/**
 * Triggered when the available videos of a movie are loaded.
 */
public interface OnTrailerLinkLoadedListener {

    void onVideosLoaded(VideoResults videoResults);
}
