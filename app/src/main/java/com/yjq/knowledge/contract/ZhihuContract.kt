package com.yjq.knowledge.contract


import com.yjq.knowledge.beans.zhihu.ZhihuDaily

import rx.Observable

/**
 * 文件： ZhihuContract
 * 描述：
 * 作者： YangJunQuan   2017/12/15.
 */

interface ZhihuContract {
    interface Ipresenter {
        fun loadZhihuNews()

        fun loadMoreZhihuNews(data: String)
    }

    interface Iview {
        fun showNews(zhihuDaily: ZhihuDaily)

        fun showMoreNews(zhihuDaily: ZhihuDaily)

    }

    interface Imodel {

        fun loadNews(): Observable<ZhihuDaily>

        fun loadMore(data: String): Observable<ZhihuDaily>
    }
}
