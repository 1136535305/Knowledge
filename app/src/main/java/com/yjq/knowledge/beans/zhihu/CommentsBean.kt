package com.yjq.knowledge.beans.zhihu

/**
 * 文件： CommentBeans
 * 描述：
 * 作者： YangJunQuan   2018/1/18.
 */

class CommentsBean {
    /**
     * author : 每一天都在混水摸鱼
     * content : 钱会让它变的好吃
     * avatar : http://pic3.zhimg.com/0ecf2216c2612b04592126adc16affa2_im.jpg
     * time : 1413987020
     * id : 556780
     * likes : 0
     * reply_to : {"content":"我每次都不假思索选了牛肉，然后就深深的后悔没有试过一次鸡肉，到下一次又情不自禁选了牛肉，周而复始循环往复-_-#","status":0,"id":551969,"author":"怒放的腋毛"}
     */

    var author: String? = null
    var content: String? = null
    var avatar: String? = null
    var time: String? = null
    var id: Int = 0
    var likes: String? = null
    var reply_to: ReplyToBean? = null

    class ReplyToBean {
        /**
         * content : 我每次都不假思索选了牛肉，然后就深深的后悔没有试过一次鸡肉，到下一次又情不自禁选了牛肉，周而复始循环往复-_-#
         * status : 0
         * id : 551969
         * author : 怒放的腋毛
         */

        var content: String? = null
        var status: Int = 0
        var id: Int = 0
        var author: String? = null
    }
}