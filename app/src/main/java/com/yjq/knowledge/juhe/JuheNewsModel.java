package com.yjq.knowledge.juhe;


import com.yjq.knowledge.beans.juhe.JuheTop;
import com.yjq.knowledge.contract.JuheContract;
import com.yjq.knowledge.network.ApiManager;
import com.yjq.knowledge.network.JuHeNewsAPI;

import rx.Observable;

/**
 * 文件： JuheNewsModel
 * 描述：
 * 作者： Yang   2017/11/17.
 */

public class JuheNewsModel implements JuheContract.Imodel {

    //真正的网络请求方法接口类,NewsService接口类不能直接使用，需要通过代理工厂
    private JuHeNewsAPI service= ApiManager.getInstance().createJuheService();

    @Override
    public Observable<JuheTop> loadNews(String type) {
        return service.getTop(type);
    }
}
