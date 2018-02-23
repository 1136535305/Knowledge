package com.yjq.knowledge.adapter

import android.content.Intent
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.yjq.knowledge.GlideApp
import com.yjq.knowledge.R
import com.yjq.knowledge.beans.zhihu.ZhihuThemeListDetail
import com.yjq.knowledge.databinding.EditorAvatorBinding
import com.yjq.knowledge.databinding.ItemNewsRecycleviewBinding
import com.yjq.knowledge.databinding.TopItemThemeRecycleviewBinding
import com.yjq.knowledge.editor.EditorListActivity
import com.yjq.knowledge.newsDetail.NewsDetailActivity
import com.yjq.knowledge.util.GlideCircleTransform
import java.util.*

/**
 * 文件： ThemeListAdapterKotlin
 * 描述：
 * 作者： YangJunQuan   2018/1/31.
 */
class ThemeListAdapterKotlin(fragment: Fragment) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private val TYPE_TOP_BACKGROUND = -1
        private val TYPE_AVATAR_LIST = 0
        private val TYPE_CONTENT = 1
    }

    private var lastAnimPosition = -1
    private var mDataSet: ZhihuThemeListDetail? = null
    private var mEditorList: ArrayList<ZhihuThemeListDetail.EditorsBean>? = null
    private val themeFragment = fragment
    private val mContext = fragment.context


    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> TYPE_TOP_BACKGROUND   //顶部视图
            1 -> TYPE_AVATAR_LIST     //主编横向列表
            else -> TYPE_CONTENT       //内容
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CommonViewHolder {
        val inflater = LayoutInflater.from(parent!!.context)
        val binding =
                when (viewType) {
                    TYPE_TOP_BACKGROUND ->
                        DataBindingUtil.inflate<TopItemThemeRecycleviewBinding>(inflater, R.layout.top_item_theme_recycleview, parent, false)
                    TYPE_AVATAR_LIST ->
                        DataBindingUtil.inflate<EditorAvatorBinding>(inflater, R.layout.editor_avator, parent, false)
                    TYPE_CONTENT ->
                        DataBindingUtil.inflate<ItemNewsRecycleviewBinding>(inflater, R.layout.item_news_recycleview, parent, false)
                    else -> TODO()
                }
        return CommonViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val viewType = getItemViewType(position)

        when (viewType) {
            TYPE_TOP_BACKGROUND -> initThemeDescriptionView(holder)
            TYPE_AVATAR_LIST -> initEditorAvatarView(holder)
            TYPE_CONTENT -> initContentView(holder, position)
        }
    }

    /**
     * 初始化RecyclerView  Item  第 0 项 视图  ： 该主题日报的背景图片和主题介绍
     */
    private fun initThemeDescriptionView(holder: RecyclerView.ViewHolder?) {
        val binding = DataBindingUtil.getBinding<TopItemThemeRecycleviewBinding>(holder!!.itemView)

        binding.tvThemeDescri.text = mDataSet!!.description   //日报主题的相应介绍
        GlideApp.with(themeFragment)                          //日报主题的相应背景图片
                .load(mDataSet!!.background)
                .centerCrop()
                .into(binding.ivThemeBg)

    }

    /**
     * 初始化RecyclerView  Item  第 1 项 视图  ：  该主题日报的主编头像列表视图
     */
    private fun initEditorAvatarView(holder: RecyclerView.ViewHolder?) {
        val binding = DataBindingUtil.getBinding<EditorAvatorBinding>(holder!!.itemView)

        holder.itemView.setOnClickListener {
            val intent = Intent(mContext, EditorListActivity::class.java)
            intent.putExtra(EditorListActivity.EDITOR_LIST_DATA, mEditorList)
            mContext!!.startActivity(intent)
        }
        val imageContainer = binding.llAvatar
        imageContainer.removeAllViews()
        for (i in mEditorList!!.indices) {
            val v = ImageView(mContext)
            GlideApp.with(themeFragment)
                    .load(mEditorList!![i].avatar)
                    .override(110, 110)
                    .transform(GlideCircleTransform(mContext))
                    .into(v)
            imageContainer.addView(v)
        }
    }

    /**
     * 初始化RecyclerView  Item  其余项 视图   :   真正的新闻内容视图
     */
    private fun initContentView(holder: RecyclerView.ViewHolder?, position: Int) {
        val binding = DataBindingUtil.getBinding<ItemNewsRecycleviewBinding>(holder!!.itemView)

        val storiesBean = mDataSet!!.stories!![position - 2]

        if (null != storiesBean.images) {

            binding.imageZhihu.visibility = View.VISIBLE
            GlideApp.with(themeFragment)
                    .load(storiesBean.images!![0])
                    .centerCrop()
                    .into(binding.imageZhihu)

        } else {
            binding.imageZhihu.visibility = View.GONE //某些新闻有可能出现没有图片的情况,这时候隐藏ImageView,适应没有图片的情况
        }

        binding.tvTitleZhihu.text = storiesBean.title
        holder.itemView.setOnClickListener({
            val i = Intent(themeFragment.context, NewsDetailActivity::class.java)
            i.putExtra("newsId", storiesBean.id)
            themeFragment.startActivity(i)
        })

        startAnimator(holder.itemView, position)
    }

    override fun getItemCount(): Int {
        return if (mDataSet != null)
            mDataSet!!.stories!!.size + 2
        else
            0
    }

    fun initDataSet(mDataSet: ZhihuThemeListDetail) {
        if (this.mDataSet == null) {                                   //null,代表是初次设置数据集
            this.mDataSet = mDataSet
            this.mEditorList = mDataSet.editors
            this.notifyDataSetChanged()
        }

        val position = mDataSet.stories!!.size - 1              //非null，代表是上拉加载更多
        this.mDataSet!!.stories!!.addAll(mDataSet.stories!!)
        this.mEditorList = mDataSet.editors
        this.notifyItemChanged(position)
    }

    fun resetDataSet(mDataSet: ZhihuThemeListDetail) {         //这个方法当相应的主题被切换的时候重新设置相应的主题数据源
        this.mDataSet = mDataSet
        this.mEditorList = mDataSet.editors
        this.notifyDataSetChanged()
    }

    private fun startAnimator(view: View, position: Int) {
        if (position > lastAnimPosition) {
            view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.item_bottom_in))
            lastAnimPosition = position
        }
    }

    fun setLastAnimPosition(position: Int) {
        lastAnimPosition = position
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        holder.itemView.clearAnimation()
    }


}