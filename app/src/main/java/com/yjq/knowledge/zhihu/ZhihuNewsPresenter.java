package com.yjq.knowledge.zhihu;

import com.orhanobut.logger.Logger;
import com.yjq.knowledge.beans.ZhihuDaily;
import com.yjq.knowledge.contract.ZhihuContract;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 文件： ZhihuNewsPresenter
 * 版权： 2017-2019  康佳集团股份有限公司
 * 保留多有版权
 * 描述：
 * 作者： YangJunQuan   2017/12/15.
 */

public class ZhihuNewsPresenter implements ZhihuContract.Ipresenter {
    private ZhihuContract.Imodel model;
    private ZhihuContract.Iview view;

    public ZhihuNewsPresenter(ZhihuContract.Iview view) {
        this.model = new ZhihuNewsModel();
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
                        Logger.i("加载网络知乎日报数据完成！！！");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("网络加载知乎日报数据报错！！！");
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
