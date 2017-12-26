package com.yjq.knowledge.beans;

import java.io.Serializable;

/**
 * 文件： ZhihuStoryExtra
 * 描述：
 * 作者： YangJunQuan   2017/12/26.
 */

public class ZhihuStoryExtra implements Serializable {

    /**
     *  long_comments : 长评论总数
     *  popularity : 点赞总数
     *  short_comments : 短评论总数
     *  comments : 评论总数
     */

    private String long_comments;
    private String popularity;
    private String short_comments;
    private String comments;

    public String getLong_comments() {
        return long_comments;
    }

    public void setLong_comments(String long_comments) {
        this.long_comments = long_comments;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public String getShort_comments() {
        return short_comments;
    }

    public void setShort_comments(String short_comments) {
        this.short_comments = short_comments;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
