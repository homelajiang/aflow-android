package com.anglll.aflow.ui.music.playlist;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.anglll.aflow.R;
import com.anglll.aflow.base.BaseMusicActivity;
import com.anglll.aflow.ui.dialog.MenuDialog;
import com.anglll.aflow.utils.DefaultDecoration;
import com.anglll.aflow.utils.Router;
import com.anglll.aflow.widget.menu.ActionMenu;

import org.lineageos.eleven.loaders.PlaylistLoader;
import org.lineageos.eleven.model.Playlist;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayListActivity extends BaseMusicActivity implements
        LoaderManager.LoaderCallbacks<List<Playlist>>, PlayListController.MusicPlayListCallback {
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.toolBar)
    Toolbar mToolBar;
    @BindView(R.id.app_bar)
    AppBarLayout mAppBar;

    private PlayListController controller = new PlayListController(this, null);
    private List<Playlist> playLists;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_playlist);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        setSupportActionBar(mToolBar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.playlist);
        }
        controller.setSpanCount(2);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
        manager.setSpanSizeLookup(controller.getSpanSizeLookup());
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DefaultDecoration(getContext().getResources().getDimensionPixelOffset(R.dimen.divider_playlist_card),
                getContext().getResources().getDimensionPixelOffset(R.dimen.divider_playlist_card),
                DefaultDecoration.COLOR_NO));
        mRecyclerView.setAdapter(controller.getAdapter());

        initLoader(2, null, this);
    }

    private void updateController() {
        controller.setData(playLists);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.playlist, menu);
        return true;
    }

    @NonNull
    @Override
    public Loader<List<Playlist>> onCreateLoader(int id, @Nullable Bundle args) {
        return new PlaylistLoader(this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Playlist>> loader, List<Playlist> data) {
        this.playLists = data;
        updateController();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Playlist>> loader) {

    }

    @Override
    public void onPlayListClick(Playlist playlist) {
        Router.openPlayList(this, playlist);
    }

    @Override
    public void showPlayListMenu(Playlist playlist) {
        ActionMenu menu = new ActionMenu(getContext());
        getMenuInflater().inflate(R.menu.playlist_action, menu);
        if (playlist.isSmartPlaylist()) {
            menu.removeItem(R.id.action_rename);
            menu.removeItem(R.id.action_delete);
        }
        new MenuDialog()
                .setMenu(menu)
                .setOnMenuSelectedCallback(new MenuDialog.MenuClickCallback() {
                    @Override
                    public void onMenuItemClick(MenuItem menuItem) {
                        TT(menuItem.getTitle().toString());
                    }
                })
                .show(getSupportFragmentManager(), "playlist");
    }

}
