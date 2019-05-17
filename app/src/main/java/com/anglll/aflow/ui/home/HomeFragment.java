package com.anglll.aflow.ui.home;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anglll.aflow.R;
import com.anglll.aflow.base.BaseFragment;
import com.anglll.aflow.ui.main.MainActivity;
import com.anglll.aflow.ui.main.MusicStateListener;
import com.anglll.beelayout.BeeImageView;
import com.anglll.beelayout.BeeLayout;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.AbstractDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.postprocessors.IterativeBoxBlurPostProcessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import org.lineageos.eleven.utils.MusicUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yuan on 2017/11/30.
 */

public class HomeFragment extends BaseFragment implements MusicStateListener {

    private static final String TAG = HomeFragment.class.getSimpleName();
    @BindView(R.id.bee_layout)
    BeeLayout mBeeLayout;
    @BindView(R.id.temperature_degree)
    TextView mTemperatureDegree;
    @BindView(R.id.weather_description)
    TextView mWeatherDescription;
    @BindView(R.id.icon_umbrella)
    AppCompatImageView mIconUmbrella;
    @BindView(R.id.rainfall_probability)
    TextView mRainfallProbability;
    @BindView(R.id.simpleDraweeView)
    SimpleDraweeView mSimpleDraweeView;
    private HomeBeeAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        getContainingActivity().addMusicStateListener(this);
        initView();
        return view;
    }

    @Override
    protected void lazyInit() {
        Log.d(TAG, "lazyInit");
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.updateMeta();
        adapter.updateController();
    }

    @Override
    public void onDestroyView() {
        getContainingActivity().removeMusicStateListener(this);
        super.onDestroyView();
    }

    private void initView() {
        adapter = new HomeBeeAdapter(getActivity());
        mBeeLayout.setAdapter(adapter);
    }

    public MainActivity getContainingActivity() {
        return (MainActivity) getActivity();
    }

    @Override
    public void restartLoader() {

    }

    @Override
    public void onPlaylistChanged() {

    }

    @Override
    public void onMetaChanged() {
        adapter.updateMeta();
        updateMetaBackground();
    }

    private void updateMetaBackground() {
        try {
            Uri uri = MusicUtils.getAlbumUri(MusicUtils.getCurrentAlbumId());
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                    .setPostprocessor(new IterativeBoxBlurPostProcessor(3, 10))
                    .build();
            AbstractDraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setOldController(mSimpleDraweeView.getController())
                    .setImageRequest(request)
                    .build();
            mSimpleDraweeView.setController(controller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateController() {
        adapter.updateController();
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }
}
