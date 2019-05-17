package com.anglll.aflow.ui.dialog;

import android.app.Dialog;
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
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.airbnb.epoxy.TypedEpoxyController;
import com.anglll.aflow.R;
import com.anglll.aflow.ui.epoxy.models.MusicQueueModel_;

import org.lineageos.eleven.MusicPlaybackService;
import org.lineageos.eleven.loaders.QueueLoader;
import org.lineageos.eleven.model.Song;
import org.lineageos.eleven.utils.MusicUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static org.lineageos.eleven.utils.MusicUtils.mService;

public class NowPlayingDialog extends BottomSheetDialogFragment implements
        LoaderCallbacks<List<Song>>, ServiceConnection, PlayQueueCallback {
    private NowPlayingController controller;
    private List<Song> nowPlayingQueue = new ArrayList<>();
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    /**
     * LoaderCallbacks identifier
     */
    private static final int LOADER = 0;
    private QueueUpdateListener mQueueUpdateListener;
    private MusicUtils.ServiceToken mToken;
    private LinearLayoutManager manager;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = View.inflate(getContext(),R.layout.dialog_now_playing,null);
        ButterKnife.bind(this, view);
        controller =
                new NowPlayingController(this);
        manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(controller.getAdapter());
        dialog.setContentView(view);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (getActivity().getResources().getDisplayMetrics().heightPixels * 0.6);
        view.setLayoutParams(layoutParams);
        BottomSheetBehavior.from((View) view.getParent());
        return dialog;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Initialize the broadcast receiver
        mQueueUpdateListener = new QueueUpdateListener(this);

        // Bind Eleven's service
        mToken = MusicUtils.bindToService(getActivity(), this);
    }

    public void updateController() {
        controller.setData(nowPlayingQueue);
    }

    @NonNull
    @Override
    public Loader<List<Song>> onCreateLoader(int id, @Nullable Bundle args) {
        return new QueueLoader(getActivity());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Song>> loader, List<Song> data) {
        this.nowPlayingQueue = data;
        updateController();
        mTitle.setText(String.format(getString(R.string.play_queue_count), nowPlayingQueue.size()));
        mRecyclerView.scrollToPosition(getScrollPosition(MusicUtils.getQueuePosition(), nowPlayingQueue.size()));
    }

    private int getScrollPosition(int targetPosition, int total) {
        if (targetPosition >= total)
            return 0;
        return targetPosition - 3 < 0 ? 0 : targetPosition - 3;
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Song>> loader) {

    }


    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        refreshQueue();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    @Override
    public void onStart() {
        super.onStart();
        final IntentFilter filter = new IntentFilter();
        filter.addAction(MusicPlaybackService.PLAYSTATE_CHANGED);
        filter.addAction(MusicPlaybackService.QUEUE_CHANGED);
        filter.addAction(MusicPlaybackService.META_CHANGED);
        if (getActivity() != null)
            getActivity().registerReceiver(mQueueUpdateListener, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (getActivity() != null)
                getActivity().unregisterReceiver(mQueueUpdateListener);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (mService != null) {
            MusicUtils.unbindFromService(mToken);
            mToken = null;
        }
    }

    private void refreshQueue() {
        if (isAdded())
            getLoaderManager().restartLoader(LOADER, null, this);
    }

    private void updateCurrentQueuePosition() {
        updateController();
    }

    @Override
    public void removeFromQueue(Song songInfo,int position) {
/*        MusicUtils.removeTrackAtPosition(songInfo.mSongId, songInfo.index);
        refreshQueue();*/
// TODO: 2018/6/21 0021  
    }

    @Override
    public void playIndex(int position) {
        MusicUtils.setQueuePosition(position);
    }


    public static class NowPlayingController extends TypedEpoxyController<List<Song>> {

        private final PlayQueueCallback callback;

        public NowPlayingController(PlayQueueCallback callback) {
            this.callback = callback;
        }

        @Override
        protected void buildModels(List<Song> songs) {
            int playingIndex = MusicUtils.getQueuePosition();
            for (int i = 0; i < songs.size(); i++) {
                add(new MusicQueueModel_()
                        .callback(callback)
                        // TODO: 2018/6/25 id
                        .id(i)
                        .playingSong(songs.get(i))
                        .index(i)
                        .playing(playingIndex == i));
            }
        }
    }


    public static final class QueueUpdateListener extends BroadcastReceiver {
        private final WeakReference<NowPlayingDialog> mReference;

        public QueueUpdateListener(final NowPlayingDialog fragment) {
            mReference = new WeakReference<>(fragment);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (MusicPlaybackService.META_CHANGED.equals(action)) {
                mReference.get().updateCurrentQueuePosition();
            } else if (MusicPlaybackService.PLAYSTATE_CHANGED.equals(action)) {
                //update play status
            } else if (MusicPlaybackService.QUEUE_CHANGED.equals(action)) {
                mReference.get().refreshQueue();
            }
        }
    }

}
