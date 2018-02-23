package com.yjq.knowledge.newsToday


import com.yjq.knowledge.beans.zhihu.ZhihuDaily
import com.yjq.knowledge.contract.ZhihuContract
import com.yjq.knowledge.network.ApiManager
import com.yjq.knowledge.network.ZhiHuNewsAPI

import rx.Observable

/**
 * 文件： NewsTodayModel
 * 描述：
 * 作者： YangJunQuan   2017/12/15.
 */

class NewsTodayModel : ZhihuContract.Imodel {
    private val zhiHuNewsAPI = ApiManager.instance.createZhihuService()

    override fun loadNews(): Observable<ZhihuDaily> {
        return zhiHuNewsAPI.lastDaily
    }

    override fun loadMore(data: String): Observable<ZhihuDaily> {
        return zhiHuNewsAPI.loadMore(data)
    }
}
