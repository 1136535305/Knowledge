package com.yjq.knowledge.contract


import com.yjq.knowledge.beans.zhihu.ZhihuNewsDetail
import com.yjq.knowledge.beans.zhihu.ZhihuStoryExtra

import rx.Observable

/**
 * 文件： NewsDetailContract
 * 描述：
 * 作者： YangJunQuan   2017/12/19.
 */

interface NewsDetailContract {
    interface Iview {

        fun showNewsDetail(zhihuNewsDetail: ZhihuNewsDetail)
        fun showNewsExtra(zhihuStoryExtra: ZhihuStoryExtra)
    }

    interface Ipresenter {
        fun loadNewsDetailById(id: Int)
        fun showNewsExtra(id: Int)
    }

    interface Imodel {


        fun loadNewsDetailById(id: Int): Observable<ZhihuNewsDetail>
        fun showNewsExtra(id: Int): Observable<ZhihuStoryExtra>
    }
}
