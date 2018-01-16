package com.yjq.knowledge.newsToday;

import com.orhanobut.logger.Logger;
import com.yjq.knowledge.beans.zhihu.ZhihuDaily;
import com.yjq.knowledge.contract.ZhihuContract;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 文件： NewsTodayPresenter
 * 描述：
 * 作者： YangJunQuan   2017/12/15.
 */

public class NewsTodayPresenter implements ZhihuContract.Ipresenter {
    private ZhihuContract.Imodel model;
    private ZhihuContract.Iview view;

    public NewsTodayPresenter(ZhihuContract.Iview view) {
        this.model = new NewsTodayModel();
        this.view = view;
    }

    @Override
    public void loadZhihuNews() {
        model.loadNews()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ZhihuDaily>() {
                    @Override
                    public void onCompleted() {
                        Logger.i("加载【网络知乎日报今日热闻】数据完成！！！");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("网络加载【知乎日报今日热闻】数据报错！！！");
                        e.printStackTrace();

                    }

                    @Override
                    public void onNext(ZhihuDaily zhihuDaily) {
                        view.showNews(zhihuDaily);
                    }
                });
    }

    @Override
    public void loadMoreZhihuNews(String data) {
        model.loadMore(data)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ZhihuDaily>() {
                    @Override
                    public void onCompleted() {
                        Logger.i("加载更多网络知乎日报数据完成！！！");

                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("网络加载更多知乎日报数据报错！！！");
                        e.printStackTrace();

                    }

                    @Override
                    public void onNext(ZhihuDaily zhihuDaily) {

                        view.showMoreNews(zhihuDaily);
                    }
                });
    }
}
