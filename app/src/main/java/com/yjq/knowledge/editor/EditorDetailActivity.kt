package com.yjq.knowledge.editor

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.Glide
import com.orhanobut.logger.Logger
import com.yjq.knowledge.R
import com.yjq.knowledge.network.ApiManager

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

import java.io.IOException

import butterknife.BindView
import butterknife.ButterKnife
import com.yjq.knowledge.GlideApp
import com.yjq.knowledge.util.GlideCircleTransform
import kotlinx.android.synthetic.main.activity_editor_detail.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import okhttp3.ResponseBody
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class EditorDetailActivity : AppCompatActivity() {

    companion object {
        val EDITOR_ID = "editor_id"
    }

    private var mEditorId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor_detail)
        ButterKnife.bind(this)
        initToolbar()
        initPara()
        initData()
    }


    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)          //显示toolbar的回退按钮
    }

    private fun initPara() {
        mEditorId = Integer.valueOf(intent.getStringExtra(EDITOR_ID))!!
    }

    private fun initData() {
        ApiManager.instance.createZhihuService().getEditorMainPageHtml(mEditorId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : Subscriber<ResponseBody>() {
                    override fun onCompleted() {
                        Logger.i("请求某一主编Id为%s的Html主页数据完成！", mEditorId)
                    }

                    override fun onError(e: Throwable) {
                        Logger.e("请求某一主编Id为%s的Html主页数据失败！", mEditorId)
                        e.printStackTrace()
                    }

                    override fun onNext(responseBody: ResponseBody) {
                        try {
                            analyzeHtmlString(responseBody.string())
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }

                    }


                })
    }


    private fun analyzeHtmlString(htmlString: String) {
        val doc = Jsoup.parse(htmlString)

        val imgUrl = doc.select("img").first().attr("src")      //获取主编的头像Url地址
        val editorName = doc.select("h1").first().text()                    //获取主编的昵称
        val bio = doc.select("p").first().text()                            //获取主编的简要职位介绍

        val zhihuName = doc.select("span.content").eq(0).text()             //知乎账号名称
        val xinlangName = doc.select("span.content").eq(1).text()           //新浪账号名称
        val personalWebSite = doc.select("span.content").eq(2).text()       //个人网站地址
        val email = doc.select("span.content").eq(3).text()                 //邮箱地址


        tvBio.text = bio
        tvEditorName1.text = editorName
        tvZhihuName.text = zhihuName
        tvXinlangName.text = xinlangName
        tvEmail.text = email
        tvWebsite.text = personalWebSite
        GlideApp.with(this)
                .load(imgUrl)
                .transform(GlideCircleTransform(this))
                .into(ivAvatar)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }


}
