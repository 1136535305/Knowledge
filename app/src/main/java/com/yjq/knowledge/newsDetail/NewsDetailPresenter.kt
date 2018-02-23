package com.yjq.knowledge.newsDetail

import com.orhanobut.logger.Logger
import com.yjq.knowledge.beans.zhihu.ZhihuNewsDetail
import com.yjq.knowledge.beans.zhihu.ZhihuStoryExtra
import com.yjq.knowledge.contract.NewsDetailContract


import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * 文件： NewsDetailPresenter
 * 描述：
 * 作者： YangJunQuan   2017/12/19.
 */

class NewsDetailPresenter(private val mView: NewsDetailContract.Iview) : NewsDetailContract.Ipresenter {
    private val mModel: NewsDetailContract.Imodel

    init {

        mModel = NewsDetailModel()
    }

    override fun loadNewsDetailById(id: Int) {
        mModel.loadNewsDetailById(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : Subscriber<ZhihuNewsDetail>() {
                    override fun onCompleted() {
                        Logger.i("加载【知乎日报新闻详情页】完成！")
                    }

                    override fun onError(e: Throwable) {
                        Logger.i("加载【知乎日报新闻详细页】出错！")
                        e.printStackTrace()
                    }

                    override fun onNext(zhihuNewsDetail: ZhihuNewsDetail) {
                        mView.showNewsDetail(zhihuNewsDetail)
                    }
                })
    }

    override fun showNewsExtra(id: Int) {
        mModel.showNewsExtra(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : Subscriber<ZhihuStoryExtra>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        Logger.i("加载知乎日报新闻详细页额外数据出错！")
                        e.printStackTrace()

                    }

                    override fun onNext(zhihuStoryExtra: ZhihuStoryExtra) {
                        Logger.i("加载知乎日报新闻详情页额外完成！")
                        mView.showNewsExtra(zhihuStoryExtra)
                    }
                })
    }
}
