package com.yjq.knowledge.newsTheme

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.orhanobut.logger.Logger
import com.yjq.knowledge.R
import com.yjq.knowledge.adapter.ThemeListAdapter
import com.yjq.knowledge.beans.zhihu.ZhihuThemeList
import com.yjq.knowledge.beans.zhihu.ZhihuThemeListDetail
import com.yjq.knowledge.network.ApiManager
import kotlinx.android.synthetic.main.fragment.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * 文件： ThemeFragment
 * 描述：
 * 作者： YangJunQuan   2018/1/25.
 */
class ThemeFragment : Fragment() {


    private var lastNewsId: Int? = null
    private lateinit var mAdapter: ThemeListAdapter
    private lateinit var mThemeBean: ZhihuThemeList.OthersBean
    private var mCurrentData: ZhihuThemeListDetail? = null
    private val mDataListBuffer = HashMap<Int?, ZhihuThemeListDetail?>()

    companion object {
        val mInstance = ThemeFragment()

    }


    fun setDataSet(themeBean: ZhihuThemeList.OthersBean) {
        mThemeBean = themeBean

        if (mDataListBuffer.containsKey(themeBean.id)) {               //查看相应的主题日报数据是否已缓存过

            mCurrentData = mDataListBuffer[themeBean.id]              //从缓存数据集中取出将要展示的数据集
            lastNewsId = mCurrentData?.stories?.get(mCurrentData!!.stories!!.size - 1)?.id
            mAdapter.setLastAnimPosition(-1)                         //复用fragment须复位
            mAdapter.resetDataSet(mCurrentData!!)                    //重设数据集
            recyclerView.scrollToPosition(0)                //复用fragment须复位

        } else {                                                    //没有缓存数据只能从网络申请
            initData()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        mAdapter = ThemeListAdapter(this)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = mAdapter
        refreshLayout.isEnableRefresh = false
        refreshLayout.setOnLoadmoreListener { initMoreData() }
    }


    private fun initData() {
        ApiManager.instance.createZhihuService().getThemeDetailById(mThemeBean.id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe {
                    mDataListBuffer.put(mThemeBean.id, it)
                    mAdapter.setLastAnimPosition(-1)                      //由于我们是复用同一个Fragment和Adapter故当切换时我们需要重新恢复Adapter的初始状态
                    mAdapter.resetDataSet(it)                             //设置新的数据集
                    recyclerView.scrollToPosition(0)              //切换时回到顶部
                    lastNewsId = it.stories!![it.stories!!.size - 1].id   //更新下一次加载更多URL所需的id参数
                }
    }

    private fun initMoreData() {
        ApiManager.instance.createZhihuService().loadMoreThemeNews(mThemeBean.id, lastNewsId ?: -1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe {
                    refreshLayout.finishLoadmore(0)                            //加载更多完成
                    mDataListBuffer[mThemeBean.id]!!.stories!!.addAll(it.stories!!)  //请求到的数据添加到缓存的数据集中
                    mAdapter.initDataSet(it)                                         //填充更多数据
                    lastNewsId = it.stories!![it.stories!!.size - 1].id                //更新下一次加载更多URL所需的id参数
                }
    }

    override fun onResume() {
        super.onResume()
        mDataListBuffer.clear()
    }

    fun clearDataCache() {
        mDataListBuffer.clear()
    }
}