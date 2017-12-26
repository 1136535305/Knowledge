package com.yjq.knowledge.network;


import com.yjq.knowledge.beans.JuheTop;

import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 文件： JuHeNewsAPI
 * 版权： 2017-2019  康佳集团股份有限公司
 * 保留多有版权
 * 描述：
 * 作者： Yang   2017/11/15.
 */

public interface JuHeNewsAPI {
    @POST("toutiao/index")
    Observable<JuheTop> getTop(@Query("type") String type);
}
