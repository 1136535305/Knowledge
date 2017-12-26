package com.yjq.knowledge.contract;


import com.yjq.knowledge.beans.JuheTop;

import rx.Observable;

/**
 * 文件： JuheContract
 * 版权： 2017-2019  康佳集团股份有限公司
 * 保留多有版权
 * 描述：
 * 作者： YangJunQuan   2017/12/15.
 */

public interface JuheContract {

    interface Iview {

        void showNews(JuheTop top);

        void showErrorMessage(String message);
    }

    interface Ipresenter {
        void loadNews(String type);
    }

    interface Imodel {
        Observable<JuheTop> loadNews(String type);
    }
}
