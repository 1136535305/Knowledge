package com.yjq.knowledge.network


import com.yjq.knowledge.App
import com.yjq.knowledge.util.net.NetWorkUtil

import java.io.File
import java.io.IOException

import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * 文件： ApiManager
 * 描述：
 * 作者： Yang   2017/11/17.
 */

class ApiManager {
    private var mZhihuNewsApi: ZhiHuNewsAPI? = null


    /**
     * 创建知乎数据Retrofit接口对象
     */
    fun createZhihuService(): ZhiHuNewsAPI {
        if (mZhihuNewsApi == null) {

            val client = OkHttpClient.Builder()
                    .addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
                    .cache(cache)
                    .build()


            mZhihuNewsApi = Retrofit.Builder()
                    .baseUrl("http://news-at.zhihu.com/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build()
                    .create(ZhiHuNewsAPI::class.java)
        }
        return mZhihuNewsApi!!
    }

    companion object {

        private var apiManager: ApiManager? = null
        private val CACHE_DIR = "ZhihuCache"
        private val httpCacheDirectory = File(App.getInstance().cacheDir, CACHE_DIR)//缓存文件存放路径：  /data/data/com.yjq.knowledge/cache/ZhihuCache
        private val cacheSize = 10 * 1024 * 1024                                              //缓存容量        ：  10mb缓存
        private val cache = Cache(httpCacheDirectory, cacheSize.toLong())
        private val REWRITE_CACHE_CONTROL_INTERCEPTOR = Interceptor { chain ->
            val originalResponse = chain.proceed(chain.request())
            if (NetWorkUtil.isNetWorkAvailable(App.getInstance())) {
                val maxAge = 60 // 在线缓存在1分钟内可读取
                originalResponse.newBuilder()
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        //public：告訴任何途径的缓存者，可以无条件地缓存该响应
                        //max-age: 缓存一分钟
                        .build()
            } else {
                val maxStale = 60 * 60 * 24 * 28 // 离线时缓存保存4周
                originalResponse.newBuilder()
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build()
            }
        }


        val instance: ApiManager
            get() {
                if (null == apiManager) {
                    synchronized(ApiManager::class.java) {

                        if (null == apiManager) {
                            apiManager = ApiManager()
                        }
                    }
                }
                return apiManager!!
            }
    }


}
