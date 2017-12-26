package com.yjq.knowledge.contract;


import com.yjq.knowledge.beans.ZhihuDaily;

import rx.Observable;

/**
 * 文件： ZhihuContract
 * 版权： 2017-2019  康佳集团股份有限公司
 * 保留多有版权
 * 描述：
 * 作者： YangJunQuan   2017/12/15.
 */

public interface ZhihuContract {
    interface Ipresenter {
        void loadZhihuNews();

        void loadMoreZhihuNews(String data);
    }

    interface Iview {
        void showNews(ZhihuDaily zhihuDaily);

        void showMoreNews(ZhihuDaily zhihuDaily);

    }

    interface Imodel {

        Observable<ZhihuDaily> loadNews();

        Observable<ZhihuDaily> loadMore(String data);
    }
}
