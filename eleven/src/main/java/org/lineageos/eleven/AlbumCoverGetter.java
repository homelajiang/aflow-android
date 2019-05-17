package org.lineageos.eleven;

import android.graphics.Bitmap;
import android.net.Uri;

public abstract class AlbumCoverGetter {
    public abstract void getCover(Uri uri,Callback callback);

    public interface Callback {
        void load(Bitmap bitmap);

        void loadFail();
    }
}
