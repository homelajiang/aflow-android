package org.lineageos.eleven.loaders;

import android.content.Context;
import android.support.annotation.Nullable;

import org.lineageos.eleven.Config;
import org.lineageos.eleven.model.Playlist;
import org.lineageos.eleven.utils.MusicUtils;

public class SmartPlaylistInfoLoader extends WrappedAsyncTaskLoader<Playlist> {

    private final Config.SmartPlaylistType type;

    /**
     * Constructor of <code>WrappedAsyncTaskLoader</code>
     *
     * @param context The {@link Context} to use.
     */
    public SmartPlaylistInfoLoader(Context context, Config.SmartPlaylistType type) {
        super(context);
        this.type = type;
    }

    @Nullable
    @Override
    public Playlist loadInBackground() {
        return MusicUtils.getCountAndCoverForSmartPlaylist(getContext(), this.type);
    }
}
