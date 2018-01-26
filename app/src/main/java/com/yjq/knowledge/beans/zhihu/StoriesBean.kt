package com.yjq.knowledge.beans.zhihu

import java.io.Serializable

/**
 * 文件： StoriesBean
 * 描述：
 * 作者： YangJunQuan   2018/1/23.
 */

class StoriesBean : Serializable {
    /**
     * images : ["https://pic1.zhimg.com/v2-3dcc799527bb2c6e018ab5f14dbfdfb4.jpg"]
     * type : 0
     * id : 9661043
     * ga_prefix : 121511
     * title : 美妆博主的那些「试色」都好好看，到底是怎么拍出来的？
     */

    var type: Int = 0
    var id: Int = 0
    var ga_prefix: String? = null
    var title: String? = null
    var images: List<String>? = null
}