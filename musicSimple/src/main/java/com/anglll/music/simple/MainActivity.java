package com.anglll.music.simple;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.lineageos.eleven.Config;
import org.lineageos.eleven.IElevenService;
import org.lineageos.eleven.MusicPlaybackService;
import org.lineageos.eleven.loaders.SongLoader;
import org.lineageos.eleven.model.Song;
import org.lineageos.eleven.sectionadapter.SectionCreator;
import org.lineageos.eleven.sectionadapter.SectionListContainer;
import org.lineageos.eleven.utils.MusicUtils;
import org.lineageos.eleven.utils.SectionCreatorUtils;

import java.lang.ref.WeakReference;

import static org.lineageos.eleven.utils.MusicUtils.mService;

public class MainActivity extends AppCompatActivity implements ServiceConnection {
    private TextView textView;
    private MusicUtils.ServiceToken mToken;
    private static String TAG = "music_code_token";
    /**
     * Broadcast receiver
     */
    private PlaybackStatus mPlaybackStatus;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        textView = (TextView) findViewById(R.id.textview);
        Log.d(TAG, "init start!");
        init();
    }

    private void init() {
        // Bind Eleven's service
        mToken = MusicUtils.bindToService(this, this);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        Log.d(TAG, "service connected");

        mService = IElevenService.Stub.asInterface(service);

        getSupportLoaderManager().initLoader(1, null, new LoaderManager.LoaderCallbacks<SectionListContainer<Song>>() {
            @NonNull
            @Override
            public Loader<SectionListContainer<Song>> onCreateLoader(int id, @Nullable Bundle args) {
                // get the context
                Context context = MainActivity.this;

                // create the underlying song loader
                SongLoader songLoader = new SongLoader(context);

                // get the song comparison method to create the headers with
                SectionCreatorUtils.IItemCompare<Song> songComparison = SectionCreatorUtils.createSongComparison(context);

                // return the wrapped section creator
                return new SectionCreator<Song>(context, songLoader, songComparison);
            }

            @Override
            public void onLoadFinished(@NonNull Loader<SectionListContainer<Song>> loader, SectionListContainer<Song> data) {
                long[] ret = new long[data.mListResults.size()];
                for (int i = 0; i < data.mListResults.size(); i++) {
                    ret[i] = data.mListResults.get(i).mSongId;
                }
                MusicUtils.playAll(MainActivity.this, ret, 5, -1, Config.IdType.NA, false);
            }

            @Override
            public void onLoaderReset(@NonNull Loader<SectionListContainer<Song>> loader) {

            }
        });

    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        Log.d(TAG, "service disconnected");
        mService = null;
    }

    @Override
    protected void onStart() {
        super.onStart();
        final IntentFilter filter = new IntentFilter();
        // Play and pause changes
        filter.addAction(MusicPlaybackService.PLAYSTATE_CHANGED);
        // Track changes
        filter.addAction(MusicPlaybackService.META_CHANGED);
        // Update a list, probably the playlist fragment's
        filter.addAction(MusicPlaybackService.REFRESH);
        // If a playlist has changed, notify us
        filter.addAction(MusicPlaybackService.PLAYLIST_CHANGED);
        // If there is an error playing a track
        filter.addAction(MusicPlaybackService.TRACK_ERROR);
        registerReceiver(mPlaybackStatus, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unbind from the service
        if (mToken != null) {
            MusicUtils.unbindFromService(mToken);
            mToken = null;
        }

        // Unregister the receiver
        try {
            unregisterReceiver(mPlaybackStatus);
        } catch (final Throwable e) {
            //$FALL-THROUGH$
        }
    }

    private final static class PlaybackStatus extends BroadcastReceiver {

        private final WeakReference<AppCompatActivity> mReference;

        /**
         * Constructor of <code>PlaybackStatus</code>
         */
        public PlaybackStatus(final AppCompatActivity activity) {
            mReference = new WeakReference<AppCompatActivity>(activity);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onReceive(final Context context, final Intent intent) {
            final String action = intent.getAction();
            AppCompatActivity baseActivity = mReference.get();
            if (baseActivity != null) {
                if (action.equals(MusicPlaybackService.META_CHANGED)) {
//                    baseActivity.onMetaChanged();
                    Log.d(TAG, "mata changed");
                } else if (action.equals(MusicPlaybackService.PLAYSTATE_CHANGED)) {
                    // Set the play and pause image
//                    baseActivity.mPlayPauseProgressButton.getPlayPauseButton().updateState();
                    Log.d(TAG, "play state changed");
                } else if (action.equals(MusicPlaybackService.REFRESH)) {
//                    baseActivity.restartLoader();
                    Log.d(TAG, "refresh");
                } else if (action.equals(MusicPlaybackService.PLAYLIST_CHANGED)) {
//                    baseActivity.onPlaylistChanged();
                    Log.d(TAG, "playlist changed");
                } else if (action.equals(MusicPlaybackService.TRACK_ERROR)) {
                    final String errorMsg = context.getString(R.string.error_playing_track,
                            intent.getStringExtra(MusicPlaybackService.TrackErrorExtra.TRACK_NAME));
                    Toast.makeText(baseActivity, errorMsg, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
