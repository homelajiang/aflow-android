package com.anglll.aflow.base;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.anglll.aflow.ui.dialog.NowPlayingDialog;
import com.anglll.aflow.ui.main.MusicStateListener;

import org.lineageos.eleven.Config;
import org.lineageos.eleven.IElevenService;
import org.lineageos.eleven.MusicPlaybackService;
import org.lineageos.eleven.loaders.QueueLoader;
import org.lineageos.eleven.loaders.SongLoader;
import org.lineageos.eleven.model.Playlist;
import org.lineageos.eleven.model.Song;
import org.lineageos.eleven.sectionadapter.SectionCreator;
import org.lineageos.eleven.sectionadapter.SectionListContainer;
import org.lineageos.eleven.utils.Lists;
import org.lineageos.eleven.utils.MusicUtils;
import org.lineageos.eleven.utils.SectionCreatorUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class BaseMusicActivity extends BaseActivity implements ServiceConnection,
        MusicStateListener {

    private MusicUtils.ServiceToken mToken;
    public IElevenService mService;
    /**
     * Playstate and meta change listener
     */
    private final ArrayList<MusicStateListener> mMusicStateListener = Lists.newArrayList();
    private PlaybackStatus mPlaybackStatus;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Control the media volume
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        //Bind Eleven's service
        mToken = MusicUtils.bindToService(this, this);

        // Initialize the broadcast receiver
        mPlaybackStatus = new PlaybackStatus(this);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        mService = IElevenService.Stub.asInterface(service);
        onUpdateController();
        onMetaChanged();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    public void addMusicStateListener(MusicStateListener status) {
        if (status == this) {
            throw new UnsupportedOperationException("Override the method, don't add a listener");
        }
        if (status != null)
            mMusicStateListener.add(status);
    }

    public void removeMusicStateListener(final MusicStateListener state) {
        if (state != null)
            mMusicStateListener.remove(state);
    }

    @Override
    public void onUpdateController() {
        // update play controller
        for (final MusicStateListener listener : mMusicStateListener) {
            if (listener != null) {
                listener.onUpdateController();
            }
        }
    }

    @Override
    public void restartLoader() {
        // Let the listener know to update a list
        for (final MusicStateListener listener : mMusicStateListener) {
            if (listener != null) {
                listener.restartLoader();
            }
        }
    }

    @Override
    public void onPlaylistChanged() {
        // Let the listener know to update a list
        for (final MusicStateListener listener : mMusicStateListener) {
            if (listener != null) {
                listener.onPlaylistChanged();
            }
        }
    }

    @Override
    public void onMetaChanged() {

        // Let the listener know to the meta chnaged
        for (final MusicStateListener listener : mMusicStateListener) {
            if (listener != null) {
                listener.onMetaChanged();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unbind from the service
        if (mToken != null) {
            MusicUtils.unbindFromService(mToken);
            mToken = null;
        }

        // Unregister the reveiver
        try {
            unregisterReceiver(mPlaybackStatus);
        } catch (final Throwable e) {

        }

        //Remove any music status listeners
        mMusicStateListener.clear();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Set the playback control drawables
        // Current info
        onMetaChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();
        final IntentFilter filter = new IntentFilter();
        // Play and pause changes
        filter.addAction(MusicPlaybackService.PLAYSTATE_CHANGED);
        // Track changes
        filter.addAction(MusicPlaybackService.META_CHANGED);
        // Update a list,probably the playlist fragment's
        filter.addAction(MusicPlaybackService.REFRESH);
        // If a playlist has changed,notify us
        filter.addAction(MusicPlaybackService.PLAYLIST_CHANGED);
        //If there is an error playing a track
        filter.addAction(MusicPlaybackService.TRACK_ERROR);
        registerReceiver(mPlaybackStatus, filter);
    }


    protected void initLoader(int loaderId, Bundle args, LoaderManager.LoaderCallbacks<? extends Object> callback) {
        getContainingLoaderManager().initLoader(loaderId, args, callback);
    }

    protected void restartLoader(int loaderId, Bundle args, LoaderManager.LoaderCallbacks<? extends Object> callback) {
        getContainingLoaderManager().restartLoader(loaderId, args, callback);
    }

    public LoaderManager getContainingLoaderManager() {
        return getSupportLoaderManager();
    }

    public void playPlaylist(Playlist playlist,int index){
        MusicUtils.playAll(getContext(), getIdList(playlist), index,
                playlist.mPlaylistId, Config.IdType.Playlist, false);
    }

    public long[] getIdList(Playlist playlist) {
        if (playlist.isSmartPlaylist()) {
            return MusicUtils.getSongListForSmartPlaylist(getContext(),
                    Config.SmartPlaylistType.getTypeById(playlist.mPlaylistId));
        } else {
            return MusicUtils.getSongListForPlaylist(getContext(),
                    playlist.mPlaylistId);
        }
    }
    /**
     * Used to monitor the state of playback
     */
    private final static class PlaybackStatus extends BroadcastReceiver {

        private final WeakReference<BaseMusicActivity> mReference;

        public PlaybackStatus(final BaseMusicActivity activity) {
            mReference = new WeakReference<BaseMusicActivity>(activity);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            BaseMusicActivity baseMusicActivity = mReference.get();
            if (action.equals(MusicPlaybackService.META_CHANGED)) {
                baseMusicActivity.onMetaChanged();
            } else if (action.equals(MusicPlaybackService.PLAYSTATE_CHANGED)) {
                baseMusicActivity.onUpdateController();
            } else if (action.equals(MusicPlaybackService.REFRESH)) {
                baseMusicActivity.restartLoader();
            } else if (action.equals(MusicPlaybackService.PLAYLIST_CHANGED)) {
                baseMusicActivity.onPlaylistChanged();
            } else if (action.equals(MusicPlaybackService.TRACK_ERROR)) {
                final String errorMsg = context.getString(org.lineageos.eleven.R.string.error_playing_track,
                        intent.getStringExtra(MusicPlaybackService.TrackErrorExtra.TRACK_NAME));
                baseMusicActivity.TT(errorMsg);
            }
        }
    }
}
