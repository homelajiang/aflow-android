package com.anglll.beelayout;

import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class BeeAdapter<VH extends BeeViewHolder> {
    private List<VH> beeViewHolderList = new ArrayList<>();

    public abstract BeeViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

    public int getItemCount() {
        return 7;
    }

    public int getItemViewType(int position) {
        return 0;
    }


    public VH getViewHolder(int position) {
        if (position < beeViewHolderList.size() && position >= 0)
            return beeViewHolderList.get(position);
        return null;
    }

    public abstract void onBindViewHolder(VH viewHolder, int position);

    public abstract void notifyItemChanged(int position);

    public void setViewHolders(List<VH> viewHolders) {
        this.beeViewHolderList = viewHolders;
    }
}
