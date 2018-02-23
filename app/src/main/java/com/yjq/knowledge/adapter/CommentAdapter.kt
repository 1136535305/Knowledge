package com.yjq.knowledge.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yjq.knowledge.GlideApp
import com.yjq.knowledge.R
import com.yjq.knowledge.beans.zhihu.ZhihuLongComments
import com.yjq.knowledge.beans.zhihu.ZhihuShortComments
import com.yjq.knowledge.databinding.ItemCommentRecycleviewBinding
import com.yjq.knowledge.databinding.TopItemCommentRecycleviewBinding
import com.yjq.knowledge.util.GlideCircleTransform
import com.yjq.knowledge.util.date.DateTimeUtil
import rx.subjects.PublishSubject

/**
 * 文件： CommentAdapter
 * 描述：
 * 作者： YangJunQuan   2018/1/17.
 */

class CommentAdapter(private val mContext: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private val TYPE_COMMENT = 0           //显示评论内容的Item项
        private val TYPE_COMMENT_COUNT = 1     //显示评论数量的Item项
    }

    private var mLongCommentSet: ZhihuLongComments? = null           //长评论数据集
    private var mShortCommentSet: ZhihuShortComments? = null         //短评论数据集


    private var mShowShortComments = false          //是否显示段评论内容,默认不显示
    val onClicks = PublishSubject.create<Int>()

    fun ismShowShortComments(): Boolean {
        return mShowShortComments
    }

    fun setmShowShortComments(mShowShortComments: Boolean) {
        this.mShowShortComments = mShowShortComments

    }

    fun setDataSet(mLongCommentSet: ZhihuLongComments, mShortCommentSet: ZhihuShortComments) {
        this.mLongCommentSet = mLongCommentSet
        this.mShortCommentSet = mShortCommentSet
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            when (viewType) {
                TYPE_COMMENT -> parent.inflate<ItemCommentRecycleviewBinding>(R.layout.item_comment_recycleview)
                TYPE_COMMENT_COUNT -> parent.inflate<TopItemCommentRecycleviewBinding>(R.layout.top_item_comment_recycleview)
                else -> TODO()
            }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)
        when (viewType) {
            TYPE_COMMENT_COUNT -> initCommentCountView(holder, position)
            TYPE_COMMENT -> initCommentView(holder, position)
        }
    }

    private fun initCommentCountView(holder: RecyclerView.ViewHolder, position: Int) {

        val binding = DataBindingUtil.getBinding<TopItemCommentRecycleviewBinding>(holder.itemView)

        with(binding) {

            var commentCounts = 0
            if (position == 0) {                                                       //  item项  “x 条长评”
                commentCounts = mLongCommentSet!!.comments!!.size
                tvCommentCount.text = "$commentCounts 条长评"
                imageView.visibility = View.GONE

            } else {                                                                   //  item项  "x 条短评"
                commentCounts = mShortCommentSet!!.comments!!.size
                tvCommentCount.text = "$commentCounts 条短评"
                imageView.visibility = View.VISIBLE

                //根据是否显示短评论标志变量改变 图标
                val drawableId = if (mShowShortComments) R.drawable.chevron_double_down else R.drawable.chevron_double_up
                imageView.setImageDrawable(mContext.getDrawable(drawableId))
                root.setOnClickListener { onClicks.onNext(position) }
            }

        }


    }

    private fun initCommentView(holder: RecyclerView.ViewHolder, position: Int) {

        val binding = DataBindingUtil.getBinding<ItemCommentRecycleviewBinding>(holder.itemView)
        val commentsBean =
                if (position < mLongCommentSet!!.comments!!.size + 1)                             //长评论item项部分
                    mLongCommentSet!!.comments!![position - 1]
                else                                                                               //短评论item项部分
                    mShortCommentSet!!.comments!![position - mLongCommentSet!!.comments!!.size - 2]


        with(binding) {

            tvAuthor.text = commentsBean.author
            tvCommentContent.text = commentsBean.content
            tvThumbsUp.text = commentsBean.likes
            tvCommentTime.text = DateTimeUtil.parseCommentTime(commentsBean.time)

            if (commentsBean.reply_to != null) {
                expandTextView.visibility = View.VISIBLE
                expandTextView.text = "//@" + commentsBean.reply_to!!.author + ": " + commentsBean.reply_to!!.content
            } else {
                expandTextView.visibility = View.GONE
            }
            GlideApp.with(mContext)
                    .load(commentsBean.avatar)
                    .transform(GlideCircleTransform(mContext))
                    .into(ivCommentAvatar!!)
        }


    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0 || position == mLongCommentSet!!.comments!!.size + 1)
            TYPE_COMMENT_COUNT
        else
            TYPE_COMMENT

    }

    override fun getItemCount() =
            when (mShowShortComments) {    //是否显示短评论
                true ->
                    if (mLongCommentSet != null && mShortCommentSet != null)                             //长评论数据集  和   短评论数据集   都不能为空
                        mLongCommentSet!!.comments!!.size + mShortCommentSet!!.comments!!.size + 2       //+２是因为要显示　“ｘ条长评论”　和　“ｘ条短评论”　　两个Item项
                    else
                        0
                false ->
                    if (mLongCommentSet != null)
                        mLongCommentSet!!.comments!!.size + 2                                            //+２是因为要显示　“ｘ条长评论”　和　“ｘ条短评论”　　两个Item项
                    else
                        0
            }

}

