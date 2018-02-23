package com.yjq.knowledge.adapter

import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * 通用的ViewHolder类，由于使用了DataBinding框架所以无需使用ButterKnife框架进行@BindView的声明
 */
class CommonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
