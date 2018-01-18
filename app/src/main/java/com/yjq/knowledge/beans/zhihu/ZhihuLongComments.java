package com.yjq.knowledge.beans.zhihu;

import java.io.Serializable;
import java.util.List;

/**
 * 文件： ZhihuLongComments
 * 描述：
 * 作者： YangJunQuan   2018/1/8.
 */

public class ZhihuLongComments implements Serializable {

    private List<CommentsBean> comments;

    public List<CommentsBean> getComments() {
        return comments;
    }

    public void setComments(List<CommentsBean> comments) {
        this.comments = comments;
    }


}
