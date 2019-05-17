package com.anglll.aflow.data.retrofit;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by yuan on 2017/7/20 0020.
 */

public class HeaderInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request()
                .newBuilder()
//                .addHeader("platform", Config.HTTP_HEADER)
//                .addHeader("app_version", String.valueOf(Config.versionCode))
                .build();
        return chain.proceed(request);
    }
}
