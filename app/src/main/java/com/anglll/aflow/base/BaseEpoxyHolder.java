package com.anglll.aflow.base;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.view.View;

import com.airbnb.epoxy.EpoxyHolder;

import butterknife.ButterKnife;

/**
 * Creating a base holder class allows us to leverage ButterKnife's view binding for all subclasses.
 * This makes subclasses much cleaner, and is a highly recommended pattern.
 */
public abstract class BaseEpoxyHolder<T> extends EpoxyHolder {
    public Context context;
    @CallSuper
    @Override
    protected void bindView(View itemView) {
        ButterKnife.bind(this, itemView);
        context = itemView.getContext();
    }

    protected abstract void bindData(T data);
}
