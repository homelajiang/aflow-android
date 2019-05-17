package com.anglll.aflow.ui.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anglll.aflow.R;
import com.anglll.aflow.base.BaseFragment;

import butterknife.ButterKnife;

/**
 * Created by yuan on 2017/11/30.
 */

public class UserFragment extends BaseFragment {

    private static final String TAG = UserFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    public static UserFragment newInstance() {
        return new UserFragment();
    }

    @Override
    protected void lazyInit() {
    }
}
