package com.yjq.knowledge.newsDetail


import com.yjq.knowledge.beans.zhihu.ZhihuNewsDetail
import com.yjq.knowledge.beans.zhihu.ZhihuStoryExtra
import com.yjq.knowledge.contract.NewsDetailContract
import com.yjq.knowledge.network.ApiManager
import com.yjq.knowledge.network.ZhiHuNewsAPI

import rx.Observable

/**
 * 文件： NewsDetailModel
 * 描述：
 * 作者： YangJunQuan   2017/12/19.
 */

class NewsDetailModel : NewsDetailContract.Imodel {
    internal var zhiHuNewsAPI = ApiManager.instance.createZhihuService()

    override fun loadNewsDetailById(id: Int): Observable<ZhihuNewsDetail> {
        return zhiHuNewsAPI.loadNewsDetailById(id)
    }

    override fun showNewsExtra(id: Int): Observable<ZhihuStoryExtra> {
        return zhiHuNewsAPI.getStotyExtra(id)
    }
}
