package com.yjq.knowledge.comments

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.yjq.knowledge.R
import com.yjq.knowledge.adapter.CommentAdapter
import com.yjq.knowledge.beans.zhihu.ZhihuLongComments
import com.yjq.knowledge.beans.zhihu.ZhihuShortComments
import com.yjq.knowledge.network.ApiManager
import com.yjq.knowledge.util.animate.MyLinearLayoutManager
import kotlinx.android.synthetic.main.activity_comments.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import kotlin.properties.Delegates

/**
 * 文件： CommentsActivity
 * 描述：
 * 作者： YangJunQuan   2018/1/26.
 */
class CommentsActivity : AppCompatActivity() {
    private var newsId: Int  by Delegates.notNull()
    private var mLongComments: ZhihuLongComments? = null
    private var mShortComments: ZhihuShortComments? = null
    lateinit private var mAdapter: CommentAdapter

    @Volatile private var loadLongCommentsFinish = false
    @Volatile private var loadShortCommentsFinish = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)
        initPara()
        initToolbar()
        initRecyclerView()
        initData()
        initEvent()
    }

    private fun initPara() {
        newsId = intent.getIntExtra("newsId", -1)
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)   //回退按钮
    }

    private fun initRecyclerView() {
        mAdapter = CommentAdapter(this)
        comments_rcy.layoutManager = MyLinearLayoutManager(this)
        comments_rcy.adapter = mAdapter
        comments_rcy.setHasFixedSize(false)
    }

    private fun initData() {
        //网络加载长评论信息
        ApiManager.instance.createZhihuService().getLongComments(newsId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe {
                    mLongComments = it
                    loadLongCommentsFinish = true
                    if (loadLongCommentsFinish && loadShortCommentsFinish)
                        mAdapter.setDataSet(mLongComments!!, mShortComments!!)      //仅当长评论和短评论信息都加载完才重新渲染RecyclerView
                }

        //网络加载短评论信息
        ApiManager.instance.createZhihuService().getShortCommnets(newsId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe {
                    mShortComments = it
                    loadShortCommentsFinish = true
                    if (loadLongCommentsFinish && loadShortCommentsFinish)
                        mAdapter.setDataSet(mLongComments!!, mShortComments!!)    //仅当长评论和短评论信息都加载完才重新渲染RecyclerView
                }

    }

    private fun initEvent() {
        mAdapter.onClicks                                                                 //点击“x条短评论” item项触发的事件
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe {
                    val isShowShortComment = !mAdapter.ismShowShortComments()

                    mAdapter.setmShowShortComments(isShowShortComment)
                    mAdapter.notifyDataSetChanged()
                    if (isShowShortComment)
                        comments_rcy.smoothScrollToPosition(it)                           //展开短评论，“x条短评论”item项置顶
                }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_comments, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()  //点击回退按钮结束当前Activity
            R.id.menu_action_edit_comment -> return true
        }
        return super.onOptionsItemSelected(item)
    }
}