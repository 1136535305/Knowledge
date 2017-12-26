package com.yjq.knowledge.network;


import com.yjq.knowledge.beans.ZhihuDaily;
import com.yjq.knowledge.beans.ZhihuNewsDetail;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * 文件： ZhiHuNewsAPI
 * 版权： 2017-2019  康佳集团股份有限公司
 * 保留多有版权
 * 描述：
 * 作者： YangJunQuan   2017/12/14.
 */

public interface ZhiHuNewsAPI {

    @GET("/api/4/news/latest")
    Observable<ZhihuDaily> getLastDaily();


    @GET("/api/4/news/before/{date}")
    Observable<ZhihuDaily> loadMore(@Path("date") String date);


    @GET("/api/4/news/{id}")
    Observable<ZhihuNewsDetail>  loadNewsDetailById(@Path("id") int id);
}
