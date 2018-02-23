package com.yjq.knowledge.newsToday

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yjq.knowledge.R
import com.yjq.knowledge.adapter.TodayListAdapter
import com.yjq.knowledge.beans.zhihu.ZhihuDaily
import com.yjq.knowledge.contract.ZhihuContract
import com.yjq.knowledge.newsTheme.ThemeFragment
import com.yjq.knowledge.util.date.DateTimeUtil
import kotlinx.android.synthetic.main.fragment.*

class NewsTodayFragment : Fragment(), ZhihuContract.Iview, View.OnClickListener {
    private lateinit var mPresenter: ZhihuContract.Ipresenter
    private lateinit var mAdapter: TodayListAdapter


    private var lastNewsData: String? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?)
            = inflater.inflate(R.layout.fragment, container, false)!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAdapter = TodayListAdapter(this)
        mPresenter = NewsTodayPresenter(this)
        mPresenter.loadZhihuNews()

        with(recyclerView) {
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
            itemAnimator = DefaultItemAnimator()
            adapter = mAdapter
        }

        with(refreshLayout) {
            setOnRefreshListener { mPresenter.loadZhihuNews() }
            setOnLoadmoreListener { mPresenter.loadMoreZhihuNews(lastNewsData!!) }
        }

        fab.setOnClickListener(this)

    }


    override fun showNews(zhihuDaily: ZhihuDaily) {
        refreshLayout!!.finishRefresh(0)
        lastNewsData = zhihuDaily.date
        mAdapter.resetDataList("今日热闻", zhihuDaily)
    }

    override fun showMoreNews(zhihuDaily: ZhihuDaily) {
        refreshLayout!!.finishLoadmore(0)
        lastNewsData = zhihuDaily.date
        mAdapter.setmDataList(DateTimeUtil.getTime(lastNewsData), zhihuDaily)
    }


    override fun onClick(view: View) {
        val id = view.id
        when (id) {
            R.id.fab -> recyclerView.smoothScrollToPosition(0)   //快速回到頂部的FAB按鈕响应事件
            else -> {
            }
        }
    }

    companion object {
        private var mFragment: NewsTodayFragment? = null

        val instance: NewsTodayFragment
            get() {
                if (mFragment == null) {
                    synchronized(ThemeFragment::class.java) {
                        if (mFragment == null) {
                            mFragment = NewsTodayFragment()
                        }
                    }
                }
                return mFragment!!
            }
    }
}
