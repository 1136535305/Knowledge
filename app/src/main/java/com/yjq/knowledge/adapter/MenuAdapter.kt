package com.yjq.knowledge.adapter

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.yjq.knowledge.R
import com.yjq.knowledge.beans.zhihu.ZhihuThemeList
import com.yjq.knowledge.databinding.ItemLeftMenuRecycleviewBinding
import com.yjq.knowledge.databinding.TopItemMenuRecycleviewBinding
import rx.subjects.PublishSubject
import java.util.*

/**
 * 文件： MenuAdapter
 * 描述：
 * 作者： YangJunQuan   2018/1/4.
 */

class MenuAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private val TYPE_TOP = 0
        private val TYPE_NOT_TOP = 1
    }

    val clicks: PublishSubject<ZhihuThemeList.OthersBean> = PublishSubject.create()
    private var mDataSet: ZhihuThemeList? = null
    private var isClicks = ArrayList<Boolean>()//控件是否被点击,默认为false，如果被点击，改变值，控件根据值改变自身颜色

    fun setmDataSet(data: ZhihuThemeList) {
        mDataSet = data
        isClicks.clear()
        for (i in 0..data.others!!.size) isClicks.add(i == 0)    //初始化Menu第0项是默认选中的
        notifyDataSetChanged()
    }

    private fun setIsClicks(position: Int) {
        isClicks.clear()
        for (i in 0..mDataSet!!.others!!.size) isClicks.add(i == position)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {

        val inflater = LayoutInflater.from(parent.context)
        val binding =
                when (viewType) {
                    TYPE_TOP ->
                        DataBindingUtil.inflate<TopItemMenuRecycleviewBinding>(inflater, R.layout.top_item_menu_recycleview, parent, false)
                    TYPE_NOT_TOP ->
                        DataBindingUtil.inflate<ItemLeftMenuRecycleviewBinding>(inflater, R.layout.item_left_menu_recycleview, parent, false)
                    else -> TODO()
                }
        return CommonViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val itemType = getItemViewType(position)
        when (itemType) {
            TYPE_TOP -> initTopItem(holder, position)
            TYPE_NOT_TOP -> initNotTopItem(holder, position)
        }

        //根据选中状态设置相应的背景颜色
        val bgColor = if (isClicks[position]) Color.parseColor("#ECEFF1") else Color.parseColor("#ffffff")
        holder.itemView.setBackgroundColor(bgColor)


    }

    private fun initTopItem(holder: RecyclerView.ViewHolder, position: Int) {

        val binding = DataBindingUtil.getBinding<TopItemMenuRecycleviewBinding>(holder.itemView)

        with(binding) {
            root.setOnClickListener {
                setIsClicks(position)
                clicks.onNext(null)
            }
        }

    }

    private fun initNotTopItem(holder: RecyclerView.ViewHolder, position: Int) {

        val binding = DataBindingUtil.getBinding<ItemLeftMenuRecycleviewBinding>(holder.itemView)
        val themeBean = mDataSet!!.others!![position - 1]

        with(binding) {
            root.setOnClickListener {
                setIsClicks(position)
                clicks.onNext(themeBean)
            }
            tvTheme.text = themeBean.name
        }

    }


    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> TYPE_TOP
            else -> TYPE_NOT_TOP
        }
    }

    override fun getItemCount(): Int {
        return if (mDataSet != null)
            mDataSet!!.others!!.size + 1  // + 1是因为 顶部项“首页”
        else
            0
    }


}
