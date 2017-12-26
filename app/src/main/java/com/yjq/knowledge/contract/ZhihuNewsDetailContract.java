package com.yjq.knowledge.contract;


import com.yjq.knowledge.beans.ZhihuNewsDetail;

import rx.Observable;

/**
 * 文件： ZhihuNewsDetailContract
 * 版权： 2017-2019  康佳集团股份有限公司
 * 保留多有版权
 * 描述：
 * 作者： YangJunQuan   2017/12/19.
 */

public interface ZhihuNewsDetailContract {
    interface Iview {

        void showNewsDetail(ZhihuNewsDetail zhihuNewsDetail);
    }

    interface Ipresenter {
        void loadNewsDetailById(int id);
    }

    interface Imodel {


        Observable<ZhihuNewsDetail> loadNewsDetailById(int id);
    }
}
