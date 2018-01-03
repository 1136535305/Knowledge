package com.yjq.knowledge.zhihuNewsdetail;


import com.yjq.knowledge.beans.zhihu.ZhihuNewsDetail;
import com.yjq.knowledge.beans.zhihu.ZhihuStoryExtra;
import com.yjq.knowledge.contract.ZhihuNewsDetailContract;
import com.yjq.knowledge.network.ApiManager;
import com.yjq.knowledge.network.ZhiHuNewsAPI;

import rx.Observable;

/**
 * 文件： ZhihuNewsDetailModel
 * 描述：
 * 作者： YangJunQuan   2017/12/19.
 */

public class ZhihuNewsDetailModel implements ZhihuNewsDetailContract.Imodel {
    ZhiHuNewsAPI zhiHuNewsAPI = ApiManager.getInstance().createZhihuService();

    @Override
    public Observable<ZhihuNewsDetail> loadNewsDetailById(int id) {
        return zhiHuNewsAPI.loadNewsDetailById(id);
    }

    @Override
    public Observable<ZhihuStoryExtra> showNewsExtra(int id) {
        return zhiHuNewsAPI.getStotyExtra(id);
    }
}
