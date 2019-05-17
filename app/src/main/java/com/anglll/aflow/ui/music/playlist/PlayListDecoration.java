package com.anglll.aflow.ui.music.playlist;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class PlayListDecoration extends RecyclerView.ItemDecoration {

    private final int decoration;

    public PlayListDecoration(int decoration) {
        this.decoration = decoration;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildViewHolder(view).getLayoutPosition();

        if (position < 4) {
            if (position % 2 == 0) {
                outRect.set(decoration, decoration, decoration / 2, 0);
            } else {
                outRect.set(decoration / 2, decoration, decoration, 0);
            }
        } else if (position == 4) {
            outRect.set(decoration, decoration, decoration, decoration);
        } else {
            outRect.set(decoration, 0, decoration, decoration);
        }
    }
}
