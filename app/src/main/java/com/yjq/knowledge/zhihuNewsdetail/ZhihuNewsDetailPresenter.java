package com.yjq.knowledge.zhihuNewsdetail;

import com.orhanobut.logger.Logger;
import com.yjq.knowledge.beans.ZhihuNewsDetail;
import com.yjq.knowledge.contract.ZhihuNewsDetailContract;


import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 文件： ZhihuNewsDetailPresenter
 * 版权： 2017-2019  康佳集团股份有限公司
 * 保留多有版权
 * 描述：
 * 作者： YangJunQuan   2017/12/19.
 */

public class ZhihuNewsDetailPresenter implements ZhihuNewsDetailContract.Ipresenter {

    private ZhihuNewsDetailContract.Iview mView;
    private ZhihuNewsDetailContract.Imodel mModel;

    public ZhihuNewsDetailPresenter(ZhihuNewsDetailContract.Iview mView) {
        this.mView = mView;

        mModel = new ZhihuNewsDetailModel();
    }

    @Override
    public void loadNewsDetailById(int id) {
        mModel.loadNewsDetailById(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ZhihuNewsDetail>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.i("加载知乎日报新闻详细页出错！");

                    }

                    @Override
                    public void onNext(ZhihuNewsDetail zhihuNewsDetail) {
                        Logger.i("加载知乎日报新闻详情页完成！");
                        mView.showNewsDetail(zhihuNewsDetail);
                    }
                });
    }
}