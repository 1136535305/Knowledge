package com.yjq.knowledge.network;


import com.yjq.knowledge.beans.ZhihuDaily;
import com.yjq.knowledge.beans.ZhihuNewsDetail;
import com.yjq.knowledge.beans.ZhihuStoryExtra;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * 文件： ZhiHuNewsAPI
 * 描述：
 * 作者： YangJunQuan   2017/12/14.
 */

public interface ZhiHuNewsAPI {

    @GET("/api/4/news/latest")
    Observable<ZhihuDaily> getLastDaily();


    @GET("/api/4/news/before/{date}")
    Observable<ZhihuDaily> loadMore(@Path("date") String date);

    /**
     * @param id  新闻的ID
     * @return    新闻消息详细内容
     */
    @GET("/api/4/news/{id}")
    Observable<ZhihuNewsDetail> loadNewsDetailById(@Path("id") int id);


    /**
     * 完整URL  https://news-at.zhihu.com/api/4/story-extra/#{id}
     * @param id 新闻的ID
     * @return 获取对应新闻的额外信息，如评论数量，所获的『赞』的数量。
     */
    @GET("/api/4/story-extra/{id}")
    Observable<ZhihuStoryExtra> getStotyExtra(@Path("id") int id);
}
