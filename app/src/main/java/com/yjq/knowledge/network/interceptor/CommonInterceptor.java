package com.yjq.knowledge.network.interceptor;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 文件： CommonInterceptor
 * 描述：
 * 作者： Yang   2017/11/17.
 */

public class CommonInterceptor implements Interceptor {
    private final String mApiKey;
    private final String mApiSecret;

    public CommonInterceptor(String mApiKey, String mApiSecret) {
        this.mApiKey = mApiKey;
        this.mApiSecret = mApiSecret;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request oldRequest = chain.request();       //旧的Request请求

        HttpUrl.Builder authorizedUrlBuilder = oldRequest.url()
                .newBuilder()
                .scheme(oldRequest.url().scheme())
                .addQueryParameter(mApiKey, mApiSecret);//添加公共参数

        Request newRequest = oldRequest.newBuilder()    //构造新的请求
                .method(oldRequest.method(), oldRequest.body())
                .url(authorizedUrlBuilder.build())
                .build();

        return chain.proceed(newRequest);          //发出新的请求


    }
}
