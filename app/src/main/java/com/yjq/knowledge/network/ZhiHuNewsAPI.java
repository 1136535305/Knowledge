package com.yjq.knowledge.network;


import com.yjq.knowledge.beans.zhihu.ZhihuDaily;
import com.yjq.knowledge.beans.zhihu.ZhihuNewsDetail;
import com.yjq.knowledge.beans.zhihu.ZhihuShortComments;
import com.yjq.knowledge.beans.zhihu.ZhihuStoryExtra;
import com.yjq.knowledge.beans.zhihu.ZhihuThemeDetail;
import com.yjq.knowledge.beans.zhihu.ZhihuThemeList;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * 文件： ZhiHuNewsAPI
 * 描述： 知乎日报 API 数据接口
 * 作者： YangJunQuan   2017/12/14.
 */

public interface ZhiHuNewsAPI {

    @GET("/api/4/news/latest")
    Observable<ZhihuDaily> getLastDaily();


    @GET("/api/4/news/before/{date}")
    Observable<ZhihuDaily> loadMore(@Path("date") String date);

    /**
     * @param id 新闻的ID
     * @return 新闻消息详细内容
     */
    @GET("/api/4/news/{id}")
    Observable<ZhihuNewsDetail> loadNewsDetailById(@Path("id") int id);


    /**
     * 完整URL  https://news-at.zhihu.com/api/4/story-extra/#{id}
     *
     * @param id 新闻的ID
     * @return 获取对应新闻的额外信息，如评论数量，所获的『赞』的数量。
     */
    @GET("/api/4/story-extra/{id}")
    Observable<ZhihuStoryExtra> getStotyExtra(@Path("id") int id);


    /**
     * 完整URL    https://news-at.zhihu.com/api/4/story/4232852/short-comments
     *
     * @param id 新闻的ID
     * @return 新闻对应短评论查看
     */
    @GET("/api/4/story/{id}/short-comments")
    Observable<ZhihuShortComments> getShortCommnets(@Path("id") int id);

    /**
     * 完整URL   https://news-at.zhihu.com/api/4/themes
     *
     * @return 主题日报列表查看
     */
    @GET("/api/4/themes")
    Observable<ZhihuThemeList> getThemeList();


    /**
     * 完整URL: https://news-at.zhihu.com/api/4/theme/{id}
     * 主题日报内容查看
     *
     * @param id 主题日报列表查看 中获得需要查看的主题日报的 id
     * @return
     */
    @GET("/api/4/theme/{id}")
    Observable<ZhihuThemeDetail> getThemeDetailById(@Path("id") int id);
}
