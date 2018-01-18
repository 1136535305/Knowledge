package com.yjq.knowledge.beans.zhihu;

import java.io.Serializable;
import java.util.List;

/**
 * 文件： ZhihuShortComments
 * 描述： 知乎新闻对应短评论查看
 * 作者： YangJunQuan   2017/12/26.
 */

public class ZhihuShortComments implements Serializable {


    private List<CommentsBean> comments;

    public List<CommentsBean> getComments() {
        return comments;
    }

    public void setComments(List<CommentsBean> comments) {
        this.comments = comments;
    }


}
