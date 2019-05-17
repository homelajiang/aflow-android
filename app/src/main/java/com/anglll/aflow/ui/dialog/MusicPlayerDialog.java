package com.anglll.aflow.ui.dialog;

import com.anglll.aflow.R;
import com.anglll.aflow.base.MusicDialogFragment;

public class MusicPlayerDialog extends MusicDialogFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_music_play;
    }

    public void onViewCreated(){
        super.onViewCreated();
        init();
    }

    void init() {

    }

    @Override
    public void restartLoader() {

    }

    @Override
    public void onPlaylistChanged() {

    }

    @Override
    public void onMetaChanged() {

    }

    @Override
    public void onUpdateController() {

    }
}
