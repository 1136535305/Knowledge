package com.yjq.knowledge.newsToday

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yjq.knowledge.R
import com.yjq.knowledge.adapter.TodayListAdapterKotlin
import com.yjq.knowledge.beans.zhihu.ZhihuDaily
import com.yjq.knowledge.contract.ZhihuContract
import com.yjq.knowledge.newsTheme.ThemeFragment
import com.yjq.knowledge.util.date.DateTimeUtil
import kotlinx.android.synthetic.main.fragment.*

class NewsTodayFragment : Fragment(), ZhihuContract.Iview, View.OnClickListener {
    private var mPresenter: ZhihuContract.Ipresenter? = null
    private var mAdapter: TodayListAdapterKotlin? = null


    private var lastNewsData: String? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment, container, false)!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAdapter = TodayListAdapterKotlin(this)
        recycler_view!!.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        recycler_view.itemAnimator = DefaultItemAnimator()
        recycler_view.adapter = mAdapter
        fab!!.setOnClickListener(this)


        refreshLayout!!.setOnRefreshListener { mPresenter!!.loadZhihuNews() }
        refreshLayout!!.setOnLoadmoreListener { mPresenter!!.loadMoreZhihuNews(lastNewsData) }
        mPresenter = NewsTodayPresenter(this)
        mPresenter!!.loadZhihuNews()
    }


    override fun showNews(zhihuDaily: ZhihuDaily) {
        refreshLayout!!.finishRefresh(0)
        lastNewsData = zhihuDaily.date
        mAdapter!!.resetDataList("今日热闻", zhihuDaily)
    }

    override fun showMoreNews(zhihuDaily: ZhihuDaily) {
        refreshLayout!!.finishLoadmore(0)
        lastNewsData = zhihuDaily.date
        mAdapter!!.setmDataList(DateTimeUtil.getTime(lastNewsData), zhihuDaily)
    }


    override fun onClick(view: View) {
        val id = view.id
        when (id) {
            R.id.fab -> recycler_view!!.smoothScrollToPosition(0)   //快速回到頂部的FAB按鈕响应事件
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
