package com.yjq.knowledge.newsToday;


import com.yjq.knowledge.beans.zhihu.ZhihuDaily;
import com.yjq.knowledge.contract.ZhihuContract;
import com.yjq.knowledge.network.ApiManager;
import com.yjq.knowledge.network.ZhiHuNewsAPI;

import rx.Observable;

/**
 * 文件： NewsTodayModel
 * 描述：
 * 作者： YangJunQuan   2017/12/15.
 */

public class NewsTodayModel implements ZhihuContract.Imodel {
    private ZhiHuNewsAPI zhiHuNewsAPI = ApiManager.getInstance().createZhihuService();

    @Override
    public Observable<ZhihuDaily> loadNews() {
        return zhiHuNewsAPI.getLastDaily();
    }

    @Override
    public Observable<ZhihuDaily> loadMore(String data) {
        return zhiHuNewsAPI.loadMore(data);
    }
}