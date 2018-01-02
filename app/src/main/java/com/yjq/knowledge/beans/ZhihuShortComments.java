package com.yjq.knowledge.beans;

import java.io.Serializable;
import java.util.List;

/**
 * 文件： ZhihuShortComments
 * 描述： 知乎新闻对应短评论查看
 * 作者： YangJunQuan   2017/12/26.
 */

public class ZhihuShortComments implements Serializable{


    private List<CommentsBean> comments;

    public List<CommentsBean> getComments() {
        return comments;
    }

    public void setComments(List<CommentsBean> comments) {
        this.comments = comments;
    }

    public static class CommentsBean {
        /**
         * author : 每一天都在混水摸鱼
         * content : 钱会让它变的好吃
         * avatar : http://pic3.zhimg.com/0ecf2216c2612b04592126adc16affa2_im.jpg
         * time : 1413987020
         * id : 556780
         * likes : 0
         * reply_to : {"content":"我每次都不假思索选了牛肉，然后就深深的后悔没有试过一次鸡肉，到下一次又情不自禁选了牛肉，周而复始循环往复-_-#","status":0,"id":551969,"author":"怒放的腋毛"}
         */

        private String author;
        private String content;
        private String avatar;
        private int time;
        private int id;
        private int likes;
        private ReplyToBean reply_to;

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getLikes() {
            return likes;
        }

        public void setLikes(int likes) {
            this.likes = likes;
        }

        public ReplyToBean getReply_to() {
            return reply_to;
        }

        public void setReply_to(ReplyToBean reply_to) {
            this.reply_to = reply_to;
        }

        public static class ReplyToBean {
            /**
             * content : 我每次都不假思索选了牛肉，然后就深深的后悔没有试过一次鸡肉，到下一次又情不自禁选了牛肉，周而复始循环往复-_-#
             * status : 0
             * id : 551969
             * author : 怒放的腋毛
             */

            private String content;
            private int status;
            private int id;
            private String author;

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getAuthor() {
                return author;
            }

            public void setAuthor(String author) {
                this.author = author;
            }
        }
    }
}
