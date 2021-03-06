package com.yjq.knowledge.beans.zhihu

import java.io.Serializable
import java.util.ArrayList

/**
 * 文件： ZhihuDaily
 * 描述：
 * 作者： YangJunQuan   2017/12/14.
 */

class ZhihuDaily : Serializable {


    /**
     * date : 20171215
     * stories : [{"images":["https://pic1.zhimg.com/v2-3dcc799527bb2c6e018ab5f14dbfdfb4.jpg"],"type":0,"id":9661043,"ga_prefix":"121511","title":"美妆博主的那些「试色」都好好看，到底是怎么拍出来的？"},{"images":["https://pic4.zhimg.com/v2-533c971f67d0681479e9bef09daa572f.jpg"],"type":0,"id":9660466,"ga_prefix":"121510","title":"每次去医院挂号就犯浑，我到底该挂哪个科？"},{"images":["https://pic1.zhimg.com/v2-97584d6aa8153255ae6eb1efbb007f48.jpg"],"type":0,"id":9659915,"ga_prefix":"121509","title":"工资又高又喜欢招年轻人，终于知道金融行业为什么这样了"},{"images":["https://pic3.zhimg.com/v2-055efe8c2bd96b20205b9dacec2a3e1a.jpg"],"type":0,"id":9660363,"ga_prefix":"121508","title":"从默默无闻到当红 app，苹果的推荐怎么会有这么大作用？"},{"images":["https://pic3.zhimg.com/v2-e19b51477adcea4d8a5657ad6b3c0b2a.jpg"],"type":0,"id":9661126,"ga_prefix":"121507","title":"我拍片有一个怪癖，最厌恶别人告诉我，要提交采访问题"},{"images":["https://pic1.zhimg.com/v2-5a519b61bf9b0ec458b059242e82e7d0.jpg"],"type":0,"id":9661122,"ga_prefix":"121507","title":"翻开微信通讯录，找不到一个可以聊天的人"},{"images":["https://pic1.zhimg.com/v2-ea0ebb75026c1def35c1000b92b6fb88.jpg"],"type":0,"id":9660869,"ga_prefix":"121507","title":"遍地的吃鸡手游，只是国内山寨游戏产业化的一个开始"},{"images":["https://pic2.zhimg.com/v2-6b40bd834c6bbc88077e06926e957099.jpg"],"type":0,"id":9660960,"ga_prefix":"121506","title":"瞎扯 · 如何正确地吐槽"}]
     * top_stories : [{"image":"https://pic3.zhimg.com/v2-b1dc2b752e88f5b74776e266d51f290e.jpg","type":0,"id":9661122,"ga_prefix":"121507","title":"翻开微信通讯录，找不到一个可以聊天的人"},{"image":"https://pic3.zhimg.com/v2-387ebdffd902a9bbfdb21ecd75e3210e.jpg","type":0,"id":9661043,"ga_prefix":"121511","title":"美妆博主的那些「试色」都好好看，到底是怎么拍出来的？"},{"image":"https://pic1.zhimg.com/v2-9fceb1d6e2fec801f681dbe08d5b3278.jpg","type":0,"id":9660869,"ga_prefix":"121507","title":"遍地的吃鸡手游，只是国内山寨游戏产业化的一个开始"},{"image":"https://pic4.zhimg.com/v2-e05b5024b31554d6e6ca716a5d396ad3.jpg","type":0,"id":9660466,"ga_prefix":"121510","title":"每次去医院挂号就犯浑，我到底该挂哪个科？"},{"image":"https://pic3.zhimg.com/v2-a984f0641ea1eba8911c26af4ccf6db6.jpg","type":0,"id":9660363,"ga_prefix":"121508","title":"从默默无闻到当红 app，苹果的推荐怎么会有这么大作用？"}]
     */

    var date: String? = null
    var stories: List<StoriesBean>? = null
    var top_stories: ArrayList<TopStoriesBean>? = null


    class TopStoriesBean {
        /**
         * image : https://pic3.zhimg.com/v2-b1dc2b752e88f5b74776e266d51f290e.jpg
         * type : 0
         * id : 9661122
         * ga_prefix : 121507
         * title : 翻开微信通讯录，找不到一个可以聊天的人
         */

        var image: String? = null
        var type: Int = 0
        var id: Int = 0
        var ga_prefix: String? = null
        var title: String? = null
    }


}
