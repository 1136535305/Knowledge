package com.yjq.knowledge.newsDetail

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageButton
import android.widget.TextSwitcher
import android.widget.TextView
import butterknife.ButterKnife
import com.yjq.knowledge.GlideApp
import com.yjq.knowledge.R
import com.yjq.knowledge.beans.zhihu.ZhihuNewsDetail
import com.yjq.knowledge.beans.zhihu.ZhihuStoryExtra
import com.yjq.knowledge.comments.CommentsActivity
import com.yjq.knowledge.contract.NewsDetailContract
import com.yjq.knowledge.photo.PhotoViewActivity
import com.yjq.knowledge.util.HtmlUtil
import kotlinx.android.synthetic.main.activity_zhihu_news_detail.*
import kotlinx.android.synthetic.main.content_zhihu_news_detail.*
import kotlinx.android.synthetic.main.menu_toolbar.*

class NewsDetailActivity : AppCompatActivity(), NewsDetailContract.Iview, View.OnClickListener {


    private var mThumbsUp = false
    private lateinit var mPresenter: NewsDetailContract.Ipresenter
    private var mNewsId: Int = 0//知乎日报新闻唯一标志ID

    @SuppressLint("SetJavaScriptEnabled", "JavascriptInterface")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zhihu_news_detail)
        ButterKnife.bind(this)
        initPara()
        initToolbar()
        initWebView()
        initPresenter()
        initEvent()
    }


    private fun initPara() {
        mNewsId = intent.getIntExtra("newsId", 0)
    }


    private fun initToolbar() {
        setSupportActionBar(toolbar)                                     //用自带的Toolbar替换掉原来的状态栏
        layoutInflater.inflate(R.layout.menu_toolbar, toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)               //显示toolbar的回退按钮
        //toolbarLayout.setTitle("collapse标题");                         //由于我们已经替换掉了toolbar上的布局，故设置标题不再起作用


    }


    private fun initPresenter() {
        mPresenter = NewsDetailPresenter(this)
        mPresenter.loadNewsDetailById(mNewsId)
        mPresenter.showNewsExtra(mNewsId)
    }


    private fun initWebView() {


        with(webView.settings) {
            javaScriptEnabled = true
            domStorageEnabled = true
            setRenderPriority(WebSettings.RenderPriority.HIGH)
            cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK  //设置 缓存模式
            databaseEnabled = true
            setAppCacheEnabled(true)
            blockNetworkImage = false
            loadsImagesAutomatically = true //自动加载图片
            pluginState = WebSettings.PluginState.OFF
        }

        webView.addJavascriptInterface(MyJavascript(this), "imageListener")
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                // 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，
                //函数的功能是在图片点击的时候调用本地java接口并传递url过去
                webView!!.loadUrl("javascript:(function(){" +
                        "var objs = document.getElementsByTagName(\"img\"); " +
                        "for(var i=0;i<objs.length;i++)  " +
                        "{"
                        + "    objs[i].onclick=function()  " +
                        "    {  "
                        + "        window.imageListener.openImage(this.src);  " +      //单击图片查看图片详情

                        "    }  " +
                        "}" +
                        "})()")

            }

        }


        //长按图片查看图片详情
        webView.setOnLongClickListener {
            val result = webView!!.hitTestResult
            if (result.type == WebView.HitTestResult.IMAGE_TYPE) {
                val url = result.extra
                val photoIntent = Intent(this, PhotoViewActivity::class.java)
                photoIntent.putExtra("photoUrl", url)
                startActivity(photoIntent)
                return@setOnLongClickListener true
            }
            return@setOnLongClickListener false
        }


    }


    private fun initEvent() {
        btnThumbsUp.setOnClickListener(this)
        tvPopularity.setOnClickListener(this)
        tvComments.setOnClickListener(this)
        btnShare.setOnClickListener(this)
    }

    override fun showNewsDetail(zhihuNewsDetail: ZhihuNewsDetail) {


        tvTitle.text = zhihuNewsDetail.title
        tvSource.text = zhihuNewsDetail.image_source
        if (zhihuNewsDetail.images != null) {
            GlideApp.with(this)                                           //加载图片
                    .load(zhihuNewsDetail.images!![0])
                    .into(imageZhihu!!)
        }

        val htmlData = HtmlUtil.createHtmlData(zhihuNewsDetail, false)//根据Api接口返回的数据重新构造一个完整的Html页面并且用webView加载这个页面
        webView.loadData(htmlData, HtmlUtil.MIME_TYPE, HtmlUtil.ENCODING)
        webView.settings.blockNetworkImage = true
        webView.settings.cacheMode = WebSettings.LOAD_DEFAULT
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                view.settings.blockNetworkImage = false
            }
        }

    }

    override fun showNewsExtra(zhihuStoryExtra: ZhihuStoryExtra) {
        tvComments!!.text = zhihuStoryExtra.comments
        tvPopularity!!.setCurrentText(zhihuStoryExtra.popularity)       //不带动画效果的
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            android.R.id.home -> finish()  //点击回退按钮结束当前Activity
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onClick(view: View) {
        when (view.id) {
            R.id.tvPopularity -> dealThumbsUp()  //点赞的图标部分
            R.id.btnThumbsUp -> dealThumbsUp() //点赞的文字部分
            R.id.tvComments -> {
                val commentIntent = Intent(this, CommentsActivity::class.java)
                commentIntent.putExtra("newsId", mNewsId)
                startActivity(commentIntent)      //跳转评论页面
            }
            R.id.btnShare -> {
            }  //分享頁面
            else -> {
            }
        }
    }

    /**
     * 处理点赞的方法操作，仅仅涉及到UI上的效果
     */
    private fun dealThumbsUp() {

        val temp = tvPopularity.currentView as TextView
        val nextDrawable: Drawable
        var currentValue = Integer.valueOf(temp.text.toString())!!          //获取当前点赞数
        if (mThumbsUp) {                                                       //已经点赞过，再次点击会取消点赞，【赞】数 - 1
            nextDrawable = resources.getDrawable(R.drawable.thumb_up_outline)
            tvPopularity.inAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_in_down_thumbs_up)      //新的TextView向下滑进
            tvPopularity.outAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_out_down_thumbs_up)   //旧的TextView项下滑出
            currentValue--
        } else {                                                               //仍未点赞 ,  点击会点赞，【赞】数 + 1
            nextDrawable = resources.getDrawable(R.drawable.thumb_up)
            tvPopularity.inAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_in_forward_thumbs_up)  //新的TextView向上滑进
            tvPopularity.outAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_out_forward_thumbs_up) //旧的TextView向上滑出
            currentValue++
        }

        mThumbsUp = !mThumbsUp                                            //点赞的状态改变
        btnThumbsUp.background = nextDrawable
        tvPopularity.setText(currentValue.toString() + "")

    }


    /**
     * 自定义的用于实现注入JavaScript代码到WebView加载的Html页面中，从而调用自定义的Java方法代码
     */
    internal inner class MyJavascript(private val context: Context) {

        @JavascriptInterface
        fun openImage(imgUrl: String) {
            val photoIntent = Intent(context, PhotoViewActivity::class.java)
            photoIntent.putExtra("photoUrl", imgUrl)
            startActivity(photoIntent)
        }
    }


}
