package com.anglll.aflow.ui.dialog;

import org.lineageos.eleven.model.Song;

public interface PlayQueueCallback {
    void removeFromQueue(Song songInfo, int position);

    void playIndex(int position);
}
