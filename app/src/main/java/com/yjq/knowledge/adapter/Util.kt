package com.yjq.knowledge.adapter

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * 通用的ViewHolder类，由于使用了DataBinding框架所以无需使用ButterKnife框架进行@BindView的声明
 */
class CommonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


/**
 * 返回ViewHolder的简便的拓展方法
 */
fun <T : ViewDataBinding> ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToParent: Boolean = false): CommonViewHolder {
    return CommonViewHolder(DataBindingUtil.inflate<T>(LayoutInflater.from(context), layoutRes, this, attachToParent).root)
}