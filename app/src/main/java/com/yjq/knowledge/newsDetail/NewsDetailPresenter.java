package com.yjq.knowledge.newsDetail;

import com.orhanobut.logger.Logger;
import com.yjq.knowledge.beans.zhihu.ZhihuNewsDetail;
import com.yjq.knowledge.beans.zhihu.ZhihuStoryExtra;
import com.yjq.knowledge.contract.NewsDetailContract;


import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 文件： NewsDetailPresenter
 * 描述：
 * 作者： YangJunQuan   2017/12/19.
 */

public class NewsDetailPresenter implements NewsDetailContract.Ipresenter {

    private NewsDetailContract.Iview mView;
    private NewsDetailContract.Imodel mModel;

    public NewsDetailPresenter(NewsDetailContract.Iview mView) {
        this.mView = mView;

        mModel = new NewsDetailModel();
    }

    @Override
    public void loadNewsDetailById(int id) {
        mModel.loadNewsDetailById(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ZhihuNewsDetail>() {
                    @Override
                    public void onCompleted() {
                        Logger.i("加载【知乎日报新闻详情页】完成！");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.i("加载【知乎日报新闻详细页】出错！");
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ZhihuNewsDetail zhihuNewsDetail) {
                        mView.showNewsDetail(zhihuNewsDetail);
                    }
                });
    }

    @Override
    public void showNewsExtra(int id) {
        mModel.showNewsExtra(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ZhihuStoryExtra>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.i("加载知乎日报新闻详细页额外数据出错！");
                        e.printStackTrace();

                    }

                    @Override
                    public void onNext(ZhihuStoryExtra zhihuStoryExtra) {
                        Logger.i("加载知乎日报新闻详情页额外完成！");
                        mView.showNewsExtra(zhihuStoryExtra);
                    }
                });
    }
}
