package com.yjq.knowledge.newsToday

import com.orhanobut.logger.Logger
import com.yjq.knowledge.beans.zhihu.ZhihuDaily
import com.yjq.knowledge.contract.ZhihuContract

import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * 文件： NewsTodayPresenter
 * 描述：
 * 作者： YangJunQuan   2017/12/15.
 */

class NewsTodayPresenter(private val view: ZhihuContract.Iview) : ZhihuContract.Ipresenter {
    private val model: ZhihuContract.Imodel = NewsTodayModel()

    override fun loadZhihuNews() {
        model.loadNews()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe { view.showNews(it) }
    }

    override fun loadMoreZhihuNews(data: String) {
        model.loadMore(data)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe { view.showMoreNews(it) }
    }
}
