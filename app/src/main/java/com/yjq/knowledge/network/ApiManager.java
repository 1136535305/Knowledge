package com.yjq.knowledge.network;


import com.yjq.knowledge.App;
import com.yjq.knowledge.network.interceptor.CommonInterceptor;
import com.yjq.knowledge.util.net.NetWorkUtil;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 文件： ApiManager
 * 描述：
 * 作者： Yang   2017/11/17.
 */

public class ApiManager {

    public static ApiManager apiManager;
    private JuHeNewsAPI mJuheNewsApi;
    private ZhiHuNewsAPI mZhihuNewsApi;
    private static final String CACHE_DIR = "ZhihuCache";
    private static File httpCacheDirectory = new File(App.getInstance().getCacheDir(), CACHE_DIR);
    private static int cacheSize = 10 * 1024 * 1024;  //10mb
    private static Cache cache = new Cache(httpCacheDirectory, cacheSize);
    private static final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());
            if (NetWorkUtil.isNetWorkAvailable(App.getInstance())) {
                int maxAge = 60; // 在线缓存在1分钟内可读取
                return originalResponse.newBuilder()
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .header("Cache-Control", "public, max-age=" + maxAge)    //public：告訴任何途径的缓存者，可以无条件地缓存该响应
                        //max-age: 缓存一分钟
                        .build();
            } else {
                int maxStale = 60 * 60 * 24 * 28; // 离线时缓存保存4周
                return originalResponse.newBuilder()
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
        }
    };

    /**
     * 创建聚合数据Retrofit接口对象对象
     */
    public JuHeNewsAPI createJuheService() {
        if (mJuheNewsApi == null) {

            CommonInterceptor commonInterceptor = new CommonInterceptor(
                    "key", "888b491fdf9179b676e6f11e202c0c91");

            OkHttpClient client = new OkHttpClient.Builder()
                    //.addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
                    .addInterceptor(commonInterceptor)
                    .build();

            mJuheNewsApi = new Retrofit.Builder()
                    .baseUrl("http://v.juhe.cn/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build().create(JuHeNewsAPI.class);
        }
        return mJuheNewsApi;
    }

    /**
     * 创建知乎数据Retrofit接口对象
     */
    public ZhiHuNewsAPI createZhihuService() {
        if (mZhihuNewsApi == null) {

            mZhihuNewsApi = new Retrofit.Builder()
                    .baseUrl("http://news-at.zhihu.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build()
                    .create(ZhiHuNewsAPI.class);
        }
        return mZhihuNewsApi;
    }


    public static ApiManager getInstance() {

        if (null == apiManager) {
            synchronized (ApiManager.class) {

                if (null == apiManager) {
                    apiManager = new ApiManager();
                }
            }
        }
        return apiManager;

    }


}
