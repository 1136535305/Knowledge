package com.yjq.knowledge.network


import com.yjq.knowledge.beans.zhihu.ZhihuDaily
import com.yjq.knowledge.beans.zhihu.ZhihuLongComments
import com.yjq.knowledge.beans.zhihu.ZhihuNewsDetail
import com.yjq.knowledge.beans.zhihu.ZhihuShortComments
import com.yjq.knowledge.beans.zhihu.ZhihuStoryExtra
import com.yjq.knowledge.beans.zhihu.ZhihuThemeListDetail
import com.yjq.knowledge.beans.zhihu.ZhihuThemeList

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import rx.Observable

/**
 * 文件： ZhiHuNewsAPI
 * 描述： 知乎日报 API 数据接口
 * 作者： YangJunQuan   2017/12/14.
 */

interface ZhiHuNewsAPI {

    @get:GET("/api/4/news/latest")
    val lastDaily: Observable<ZhihuDaily>

    /**
     * 完整URL   https://news-at.zhihu.com/api/4/themes
     *
     * @return 主题日报列表查看
     */
    @get:GET("/api/4/themes")
    val themeList: Observable<ZhihuThemeList>


    @GET("/api/4/news/before/{date}")
    fun loadMore(@Path("date") date: String): Observable<ZhihuDaily>

    /**
     * @param id 新闻的ID
     * @return 新闻消息详细内容
     */
    @GET("/api/4/news/{id}")
    fun loadNewsDetailById(@Path("id") id: Int): Observable<ZhihuNewsDetail>


    /**
     * 完整URL  https://news-at.zhihu.com/api/4/story-extra/#{id}
     *
     * @param id 新闻的ID
     * @return 获取对应新闻的额外信息，如评论数量，所获的『赞』的数量。
     */
    @GET("/api/4/story-extra/{id}")
    fun getStotyExtra(@Path("id") id: Int): Observable<ZhihuStoryExtra>


    /**
     * 完整URL    https://news-at.zhihu.com/api/4/story/4232852/short-comments
     *
     * @param id 新闻的ID
     * @return 新闻对应短评论查看
     */
    @GET("/api/4/story/{id}/short-comments")
    fun getShortCommnets(@Path("id") id: Int): Observable<ZhihuShortComments>

    /**
     * 完整URL    https://news-at.zhihu.com/api/4/story/8997528/long-comments
     *
     * @param id 新闻的ID
     * @return 新闻对应长评论查看
     */
    @GET("/api/4/story/{id}/long-comments")
    fun getLongComments(@Path("id") id: Int): Observable<ZhihuLongComments>


    /**
     * 完整URL: https://news-at.zhihu.com/api/4/theme/{id}
     * 主题日报内容查看
     *
     * @param id 主题日报列表查看 中获得需要查看的主题日报的 id
     * @return
     */
    @GET("/api/4/theme/{id}")
    fun getThemeDetailById(@Path("id") id: Int): Observable<ZhihuThemeListDetail>

    /**
     * @param themeId
     * @param newsId
     * @return
     */
    @GET("/api/4/theme/{themeId}/before/{newsId}")
    fun loadMoreThemeNews(@Path("themeId") themeId: Int, @Path("newsId") newsId: Int): Observable<ZhihuThemeListDetail>

    /**
     * 完整Url：https://news-at.zhihu.com/api/4/editor/70/profile-page/android
     * 查看某一主编的主页
     *
     * @param id 主编的Id
     * @return
     */
    @GET("/api/4/editor/{id}/profile-page/android")
    fun getEditorMainPageHtml(@Path("id") id: Int): Observable<ResponseBody>
}
