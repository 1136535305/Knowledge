package com.yjq.knowledge.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.yjq.knowledge.GlideApp
import com.yjq.knowledge.R
import com.yjq.knowledge.beans.zhihu.ZhihuThemeListDetail
import com.yjq.knowledge.databinding.ItemEditorThemeRecycleviewBinding
import com.yjq.knowledge.util.GlideCircleTransform
import rx.subjects.PublishSubject
import java.util.*

/**
 * 文件： EditorAdapter
 * 描述：
 * 作者： YangJunQuan   2018/1/9.
 */

class EditorAdapter(private val mContext: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val onClicks = PublishSubject.create<String>()!!
    private var mDataSet: ArrayList<ZhihuThemeListDetail.EditorsBean>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder {
        val binding = DataBindingUtil.inflate<ItemEditorThemeRecycleviewBinding>(LayoutInflater.from(parent.context), R.layout.item_editor_theme_recycleview, parent, false)
        return CommonViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val binding = DataBindingUtil.getBinding<ItemEditorThemeRecycleviewBinding>(holder.itemView)
        val editorsBean = mDataSet!![position]

        with(binding) {
            tvBio.text = editorsBean.bio
            tvEditorName.text = editorsBean.name
            root.setOnClickListener { onClicks.onNext(editorsBean.id.toString() + "") }
            GlideApp.with(mContext)
                    .load(editorsBean.avatar)
                    .transform(GlideCircleTransform(mContext))
                    .into(ivAvatar)
        }


    }

    override fun getItemCount(): Int {
        return if (mDataSet != null)
            mDataSet!!.size
        else
            0
    }

    fun setmDataSet(mEditorList: ArrayList<ZhihuThemeListDetail.EditorsBean>) {
        this.mDataSet = mEditorList
        this.notifyDataSetChanged()
    }


}
