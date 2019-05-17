package com.anglll.aflow.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anglll.aflow.ui.main.MainActivity;
import com.anglll.aflow.ui.main.MusicStateListener;

import butterknife.ButterKnife;

public abstract class MusicDialogFragment extends DialogFragment implements MusicStateListener {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, view);
/*        // set the background color
        view.setBackgroundColor(getResources().getColor(org.lineageos.eleven.R.color.background_color));
        // eat any touches that fall through to the root so they aren't
        // passed on to fragments "behind" the current one.
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent me) { return true; }
        });*/
        onViewCreated();
        return view;
    }

    public void onViewCreated() {
        getContainingActivity().addMusicStateListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getContainingActivity().removeMusicStateListener(this);
    }

    protected abstract int getLayoutId();

    protected MainActivity getContainingActivity() {
        return (MainActivity) getActivity();
    }
}
