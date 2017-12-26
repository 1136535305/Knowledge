package com.yjq.knowledge.contract;


import com.yjq.knowledge.beans.ZhihuNewsDetail;
import com.yjq.knowledge.beans.ZhihuStoryExtra;

import rx.Observable;

/**
 * 文件： ZhihuNewsDetailContract
 * 描述：
 * 作者： YangJunQuan   2017/12/19.
 */

public interface ZhihuNewsDetailContract {
    interface Iview {

        void showNewsDetail(ZhihuNewsDetail zhihuNewsDetail);
        void showNewsExtra(ZhihuStoryExtra zhihuStoryExtra);
    }

    interface Ipresenter {
        void loadNewsDetailById(int id);
        void showNewsExtra(int id);
    }

    interface Imodel {


        Observable<ZhihuNewsDetail> loadNewsDetailById(int id);
        Observable<ZhihuStoryExtra> showNewsExtra(int id);
    }
}
