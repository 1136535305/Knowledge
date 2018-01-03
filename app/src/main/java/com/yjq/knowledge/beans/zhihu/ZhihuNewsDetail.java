package com.yjq.knowledge.beans.zhihu;

import java.io.Serializable;
import java.util.List;

/**
 * 文件： ZhihuNewsDetail
 * 描述：
 * 作者： YangJunQuan   2017/12/19.
 */

public class ZhihuNewsDetail  implements Serializable{

    /**
     * body : <div class="main-wrap content-wrap">
     <div class="headline">

     <div class="img-place-holder"></div>



     </div>

     <div class="content-inner">




     <div class="question">
     <h2 class="question-title">哪个瞬间让你特别希望拥有自己的房子？</h2>

     <div class="answer">

     <div class="meta">
     <img class="avatar" src="http://pic2.zhimg.com/v2-dce1754a95a29f7a1013e03810c4eb99_is.jpg">
     <span class="author">挖井的林喵喵，</span><span class="bio">喜欢书</span>
     </div>

     <div class="content">
     <p>房东不让我煮螺蛳粉吃。</p>
     </div>
     </div>


     <div class="view-more"><a href="http://www.zhihu.com/question/263707137">查看知乎讨论<span class="js-question-holder"></span></a></div>

     </div>





     <div class="question">
     <h2 class="question-title">如果可以选择，你想住进「同福客栈」还是「爱情公寓」？</h2>

     <div class="answer">

     <div class="meta">
     <img class="avatar" src="http://pic1.zhimg.com/da8e974dc_is.jpg">
     <span class="author">次元，</span><span class="bio">不容然后见君子</span>
     </div>

     <div class="content">
     <p>住爱情公寓。</p>
     <p>住这儿，我只要熟记《老友记》、《生活大爆炸》的剧情，就能做半个预言家。</p>
     </div>
     </div>


     <div class="view-more"><a href="http://www.zhihu.com/question/48559623">查看知乎讨论<span class="js-question-holder"></span></a></div>

     </div>





     <div class="question">
     <h2 class="question-title">大三学生手头有 6000 元，有什么好的理财投资建议？</h2>

     <div class="answer">

     <div class="meta">
     <img class="avatar" src="http://pic3.zhimg.com/v2-7a14caf60e8fb3c7337ae1703e6da03e_is.jpg">
     <span class="author">blockchain，</span><span class="bio">巴比特、比原链创始人</span>
     </div>

     <div class="content">
     <p>买比特币，保存好钱包文件，然后忘掉你有过 6000 元这回事。五年后再看看。</p>
     <hr><p>知乎日报注：这个回答发布于 2011 年，6 年后，比特币的价格超过了一万七千美元……</p>
     </div>
     </div>


     <div class="view-more"><a href="http://www.zhihu.com/question/19982269">查看知乎讨论<span class="js-question-holder"></span></a></div>

     </div>





     <div class="question">
     <h2 class="question-title">有哪些暂时没法用科学解释的灵异事件？</h2>

     <div class="answer">

     <div class="meta">
     <img class="avatar" src="http://pic2.zhimg.com/d2acac6303ddca9e8fdd0c13f8466271_is.jpg">
     <span class="author">想学打铁的剪辑师，</span><span class="bio">从明天起，喂马，劈柴，铸剑</span>
     </div>

     <div class="content">
     <p>写一个我同学的事吧，我大学同学，满族人，以前是旗人，大地主，在东北有一老大的宅子，同学的祖爷爷（爷爷的爷爷）找高人给宅子算了一卦，高人说了——</p>
     <p>你们家这宅子风水不一般，这是块要成百上千出状元的地方啊！</p>
     <p>老太爷一听我的天呐，成百上千出状元，那我们家将来得发达成什么样啊！</p>
     <p> </p>
     <p>然后新中国成立，打土豪分田地，他们家的宅子被政府征用，改造成了一座……高中。</p>
     <p>现在已经是他们市的重点中学了，果然是出状元啊。</p>
     </div>
     </div>


     <div class="view-more"><a href="http://www.zhihu.com/question/53675228">查看知乎讨论<span class="js-question-holder"></span></a></div>

     </div>


     </div>
     </div><script type=“text/javascript”>window.daily=true</script>
     * image_source : 《武林外传》
     * title : 瞎扯 · 如何正确地吐槽
     * image : https://pic3.zhimg.com/v2-5ed365007e0b9795529b22ef78144c86.jpg
     * share_url : http://daily.zhihu.com/story/9660723
     * js : []
     * id : 9660723
     * ga_prefix : 121306
     * images : ["https://pic3.zhimg.com/v2-f69d4dc9fcee245c940acb0dabe43f2a.jpg"]
     * type : 0
     * section : {"thumbnail":"https://pic3.zhimg.com/v2-e4dc66eab11073fa2178086b031d5dce.jpg","name":"瞎扯","id":2}
     * css : ["http://news-at.zhihu.com/css/news_qa.auto.css?v=4b3e3"]
     */

    private String body;
    private String image_source;
    private String title;
    private String image;
    private String share_url;
    private int id;
    private String ga_prefix;
    private int type;
    private SectionBean section;
    private List<?> js;
    private List<String> images;
    private List<String> css;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getImage_source() {
        return image_source;
    }

    public void setImage_source(String image_source) {
        this.image_source = image_source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGa_prefix() {
        return ga_prefix;
    }

    public void setGa_prefix(String ga_prefix) {
        this.ga_prefix = ga_prefix;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public SectionBean getSection() {
        return section;
    }

    public void setSection(SectionBean section) {
        this.section = section;
    }

    public List<?> getJs() {
        return js;
    }

    public void setJs(List<?> js) {
        this.js = js;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<String> getCss() {
        return css;
    }

    public void setCss(List<String> css) {
        this.css = css;
    }

    public static class SectionBean {
        /**
         * thumbnail : https://pic3.zhimg.com/v2-e4dc66eab11073fa2178086b031d5dce.jpg
         * name : 瞎扯
         * id : 2
         */

        private String thumbnail;
        private String name;
        private int id;

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
