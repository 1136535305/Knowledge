package com.yjq.knowledge.juhe;

import com.orhanobut.logger.Logger;
import com.yjq.knowledge.beans.juhe.JuheTop;
import com.yjq.knowledge.contract.JuheContract;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 文件： JuheNewsPresenter
 * 描述：
 * 作者： Yang   2017/11/17.
 */

public class JuheNewsPresenter implements JuheContract.Ipresenter {
    private JuheContract.Imodel mModel;
    private JuheContract.Iview mView;

    public JuheNewsPresenter(JuheContract.Iview view) {
        this.mView = view;
        mModel = new JuheNewsModel();
    }

    @Override
    public void loadNews(String type) {
        mModel.loadNews(type)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<JuheTop>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        Logger.e("加载网络数据失败了，执行了回调方法onError");
                    }

                    @Override
                    public void onNext(JuheTop juheTop) {

                        Logger.i("加载【聚合数据】成功，返回数据%s", juheTop.getReason());
                        if (null == juheTop.getResult()) {
                            mView.showErrorMessage(juheTop.getReason());
                        } else {
                            mView.showNews(juheTop);
                        }

                    }
                });

    }

}
