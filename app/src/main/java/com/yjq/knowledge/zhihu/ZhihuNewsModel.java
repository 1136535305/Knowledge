package com.yjq.knowledge.zhihu;


import com.yjq.knowledge.beans.ZhihuDaily;
import com.yjq.knowledge.contract.ZhihuContract;
import com.yjq.knowledge.network.ApiManager;
import com.yjq.knowledge.network.ZhiHuNewsAPI;

import rx.Observable;

/**
 * 文件： ZhihuNewsModel
 * 版权： 2017-2019  康佳集团股份有限公司
 * 保留多有版权
 * 描述：
 * 作者： YangJunQuan   2017/12/15.
 */

public class ZhihuNewsModel implements ZhihuContract.Imodel {
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