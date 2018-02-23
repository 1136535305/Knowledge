package com.yjq.knowledge.editor

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.MenuItem

import com.yjq.knowledge.R
import com.yjq.knowledge.adapter.EditorAdapter
import com.yjq.knowledge.beans.zhihu.ZhihuThemeListDetail

import java.util.ArrayList

import butterknife.BindView
import butterknife.ButterKnife
import kotlinx.android.synthetic.main.activity_editor_list.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class EditorListActivity : AppCompatActivity() {

    companion object {
        val EDITOR_LIST_DATA = "editorListData"
    }

    private lateinit var mEditorList: ArrayList<ZhihuThemeListDetail.EditorsBean>
    private lateinit var mAdapter: EditorAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor_list)
        ButterKnife.bind(this)
        initToolbar()
        initData()
        initView()
        initEvent()

    }


    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)          //显示toolbar的回退按钮
    }

    private fun initData() {
        mEditorList = intent.getSerializableExtra(EDITOR_LIST_DATA) as ArrayList<ZhihuThemeListDetail.EditorsBean>
    }

    private fun initView() {
        mAdapter = EditorAdapter(this)
        rcyEditor.adapter = mAdapter
        rcyEditor.layoutManager = LinearLayoutManager(this)
        mAdapter.setmDataSet(mEditorList)
    }


    private fun initEvent() {
        mAdapter.onClicks.observeOn(AndroidSchedulers.mainThread())    //左侧边栏菜单Item项的点击事件，具体是指点击某个主题日报时
                .subscribeOn(Schedulers.io())
                .subscribe {
                    val intent = Intent(this, EditorDetailActivity::class.java)
                    intent.putExtra(EditorDetailActivity.EDITOR_ID, it)
                    startActivity(intent)
                }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }


}
