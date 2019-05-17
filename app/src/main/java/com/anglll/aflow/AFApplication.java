package com.anglll.aflow;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.multidex.MultiDex;

import com.facebook.common.executors.UiThreadImmediateExecutorService;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.squareup.leakcanary.LeakCanary;

import org.lineageos.eleven.AlbumCoverGetter;
import org.lineageos.eleven.MusicPlaybackService;

import okhttp3.OkHttpClient;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by yuan on 2017/11/25 0025.
 */

public class AFApplication extends Application {
    private static AFApplication application;

    public static AFApplication getApplication() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        initLeakCanary();
        initFresco();
        initCalligraphy();
        enableStrictMode();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void initCalligraphy() {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
//                .setDefaultFontPath("fonts/Roboto-RobotoRegular.ttf")
                        .setDefaultFontPath("fonts/helvetica_neue.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
    }

    private void initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }

    private void initFresco() {
        OkHttpClient client = new OkHttpClient();
        ImagePipelineConfig config = OkHttpImagePipelineConfigFactory
                .newBuilder(this, client)
                .build();
        Fresco.initialize(this, config);
        //初始化图片加载器
        MusicPlaybackService.setAlbumCoverGetter(new AlbumCoverGetter() {
            @Override
            public void getCover(Uri uri, final Callback callback) {
                ImageRequest imageRequest = ImageRequest.fromUri(uri);
                ImagePipeline imagePipeline = Fresco.getImagePipeline();
                DataSource<CloseableReference<CloseableImage>> dataSource
                        = imagePipeline.fetchDecodedImage(imageRequest, null);
                dataSource.subscribe(new BaseBitmapDataSubscriber() {
                    @Override
                    protected void onNewResultImpl(@Nullable Bitmap bitmap) {
                        callback.load(bitmap);
                    }

                    @Override
                    protected void onFailureImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
                        callback.loadFail();
                    }
                }, UiThreadImmediateExecutorService.getInstance());
            }
        });
    }

    private void enableStrictMode() {
        if (BuildConfig.DEBUG) {
            final StrictMode.ThreadPolicy.Builder threadPolicyBuilder = new StrictMode.ThreadPolicy.Builder()
                    .detectAll().penaltyLog();
            final StrictMode.VmPolicy.Builder vmPolicyBuilder = new StrictMode.VmPolicy.Builder()
                    .detectAll().penaltyLog();

            threadPolicyBuilder.penaltyFlashScreen();
            StrictMode.setThreadPolicy(threadPolicyBuilder.build());
            StrictMode.setVmPolicy(vmPolicyBuilder.build());
        }
    }
}
