package com.yjq.knowledge.beans.zhihu

import java.io.Serializable

/**
 * 文件： ZhihuStoryExtra
 * 描述：
 * 作者： YangJunQuan   2017/12/26.
 */

class ZhihuStoryExtra : Serializable {

    /**
     * long_comments : 长评论总数
     * popularity : 点赞总数
     * short_comments : 短评论总数
     * comments : 评论总数
     */

    var long_comments: String? = null
    var popularity: String? = null
    var short_comments: String? = null
    var comments: String? = null
}
