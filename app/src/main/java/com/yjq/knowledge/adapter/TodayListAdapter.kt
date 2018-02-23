package com.yjq.knowledge.adapter

import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.yjq.knowledge.GlideApp
import com.yjq.knowledge.R
import com.yjq.knowledge.beans.zhihu.StoriesBean
import com.yjq.knowledge.beans.zhihu.ZhihuDaily
import com.yjq.knowledge.databinding.IntervalItemZhihuRecycleviewBinding
import com.yjq.knowledge.databinding.ItemNewsRecycleviewBinding
import com.yjq.knowledge.databinding.TopItemTodayRecycleviewBinding
import com.yjq.knowledge.newsDetail.NewsDetailActivity
import com.yjq.knowledge.util.BannerImageLoader

/**
 * 文件： TodayListAdapter
 * 描述：
 * 作者： YangJunQuan   2018/1/25.
 */
class TodayListAdapter(fragment: Fragment) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private val TYPE_BANNER_TOP = -1
        private val TYPE_DATE = 0
        private val TYPE_CONTENT = 1
    }

    private var lastPos = -1
    private var mDataList = ArrayList<Any>()
    private var mTopStoriesList = ArrayList<ZhihuDaily.TopStoriesBean>()
    private var mFragment: Fragment? = fragment

    fun setmDataList(data: String, zhihuDaily: ZhihuDaily) {
        val position = zhihuDaily.stories!!.size

        if (zhihuDaily.top_stories != null) {
            this.mTopStoriesList = zhihuDaily.top_stories!!
        }
        mDataList.add(data)
        mDataList.addAll(zhihuDaily.stories!!)
        notifyItemChanged(position)

    }

    fun resetDataList(date: String, zhihuDaily: ZhihuDaily) {
        mTopStoriesList = zhihuDaily.top_stories!!
        mDataList.clear()
        mDataList.add(date)                            //日期
        mDataList.addAll(zhihuDaily.stories!!)         //该日期代表的数据集
        notifyDataSetChanged()
    }

    override fun getItemCount() = mDataList.size + 1    //  +1是因为Recycle的第一项是Banner轮播图

    override fun getItemViewType(position: Int) = when {
        position == 0 -> TYPE_BANNER_TOP
        mDataList[position - 1] is String -> TYPE_DATE     //说明该位置是留给日期的
        else -> TYPE_CONTENT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            when (viewType) {
                TYPE_CONTENT -> parent.inflate<ItemNewsRecycleviewBinding>(R.layout.item_news_recycleview)                   //真正的新闻列表内容
                TYPE_DATE -> parent.inflate<IntervalItemZhihuRecycleviewBinding>(R.layout.interval_item_zhihu_recycleview)   //日期
                else -> parent.inflate<TopItemTodayRecycleviewBinding>(R.layout.top_item_today_recycleview)                 //RecyclerView顶部的轮播图
            }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)

        when (viewType) {
            TYPE_BANNER_TOP -> initTopBannerView(holder)
            TYPE_CONTENT -> initContentView(holder, position)
            TYPE_DATE -> initDateView(holder, position)
        }

    }

    private fun initTopBannerView(holder: RecyclerView.ViewHolder) {

        val binding = DataBindingUtil.getBinding<TopItemTodayRecycleviewBinding>(holder.itemView)

        val imageList = ArrayList<String>()
        val titleList = ArrayList<String>()

        mTopStoriesList.forEach {
            imageList.add(it.image!!)
            titleList.add(it.title!!)
        }

        with(binding.banner) {
            setImageLoader(BannerImageLoader())
            setBannerStyle(3)
            setImages(imageList)
            setBannerTitles(titleList)
            setOnBannerListener { startNewsDetailActivity(mTopStoriesList[it].id) }
            start()
        }
    }

    private fun initDateView(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = DataBindingUtil.getBinding<IntervalItemZhihuRecycleviewBinding>(holder.itemView)

        binding.tvDate.text = mDataList[position - 1].toString()
        startAnimator(binding.root, position)

    }

    private fun initContentView(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = DataBindingUtil.getBinding<ItemNewsRecycleviewBinding>(holder.itemView)
        val storiesBean = mDataList[position - 1] as StoriesBean

        with(binding) {
            startAnimator(root, position) //动画效果
            storyBean = storiesBean       //为布局文件注入数据集，具体的赋值操作已经在相应的布局文件中完成了
            root.setOnClickListener { startNewsDetailActivity(storiesBean.id) }
            GlideApp.with(mFragment)
                    .load(storiesBean.images!![0])
                    .into(imageZhihu)
        }


    }

    private fun startNewsDetailActivity(newsId: Int) {
        val i = Intent(mFragment!!.context, NewsDetailActivity::class.java)
        i.putExtra("newsId", newsId)
        mFragment!!.startActivity(i)
    }

    /**
     * 简单的动画效果
     */
    private fun startAnimator(view: View, position: Int) {
        if (position > lastPos) {
            view.startAnimation(AnimationUtils.loadAnimation(mFragment!!.context, R.anim.item_bottom_in))
            lastPos = position
        }
    }


    /**
     * 不显示的时候移除动画效果
     */
    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        holder.itemView.clearAnimation()
    }


}