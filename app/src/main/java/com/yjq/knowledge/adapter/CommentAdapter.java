package com.yjq.knowledge.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yjq.knowledge.GlideApp;
import com.yjq.knowledge.R;
import com.yjq.knowledge.beans.zhihu.ZhihuLongComments;
import com.yjq.knowledge.beans.zhihu.ZhihuShortComments;
import com.yjq.knowledge.util.GlideCircleTransform;
import com.yjq.knowledge.util.date.DateTimeUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.subjects.PublishSubject;

/**
 * 文件： CommentAdapter
 * 描述：
 * 作者： YangJunQuan   2018/1/17.
 */

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private ZhihuLongComments mLongCommentSet;           //长评论数据集
    private ZhihuShortComments mShortCommentSet;         //短评论数据集
    private static final int TYPE_COMMENT = 0;           //显示评论内容的Item项
    private static final int TYPE_COMMENT_COUNT = 1;     //显示评论数量的Item项


    public boolean mShowShortComments = false;          //是否显示段评论内容,默认不显示
    public PublishSubject<Integer> onClickSubject = PublishSubject.create();

    public PublishSubject<Integer> getOnClicks() {
        return onClickSubject;
    }

    public boolean ismShowShortComments() {
        return mShowShortComments;
    }

    public void setmShowShortComments(boolean mShowShortComments) {
        this.mShowShortComments = mShowShortComments;

    }

    public CommentAdapter(Context mContext) {
        this.mContext = mContext;
    }


    public void setDataSet(ZhihuLongComments mLongCommentSet, ZhihuShortComments mShortCommentSet) {
        this.mLongCommentSet = mLongCommentSet;
        this.mShortCommentSet = mShortCommentSet;
        notifyDataSetChanged();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        switch (viewType) {
            case TYPE_COMMENT_COUNT:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.top_item_comment_recycleview, parent, false);
                return new CommentCountViewHolder(itemView);
            case TYPE_COMMENT:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment_recycleview, parent, false);
                return new CommentViewHolder(itemView);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case TYPE_COMMENT_COUNT:
                initCommentCountView((CommentCountViewHolder) holder, position);
                break;
            case TYPE_COMMENT:
                initCommentView((CommentViewHolder) holder, position);
                break;
            default:
                break;
        }
    }

    private void initCommentCountView(CommentCountViewHolder holder, int position) {
        int commentCounts = 0;
        if (position == 0) {
            commentCounts = mLongCommentSet.getComments().size();
            holder.tvCommentCount.setText(commentCounts + " 条长评");
            holder.imageView.setVisibility(View.GONE);
        } else {
            commentCounts = mShortCommentSet.getComments().size();
            holder.tvCommentCount.setText(commentCounts + " 条短评");
            holder.imageView.setVisibility(View.VISIBLE);

            if (mShowShortComments)
                holder.imageView.setImageDrawable(mContext.getDrawable(R.drawable.chevron_double_down));
            else
                holder.imageView.setImageDrawable(mContext.getDrawable(R.drawable.chevron_double_up));
            holder.itemView.setOnClickListener(view ->

                    onClickSubject.onNext(position)
            );
        }


    }

    private void initCommentView(CommentViewHolder holder, int position) {


        if (position < mLongCommentSet.getComments().size() + 1) {     //长评论item项部分

            ZhihuLongComments.CommentsBean commentsBean = mLongCommentSet.getComments().get(position - 1);
            holder.tvAuthor.setText(commentsBean.getAuthor());
            holder.tvCommentContent.setText(commentsBean.getContent());
            holder.tvThumbsUp.setText(commentsBean.getLikes());
            holder.tvCommentTime.setText(DateTimeUtil.parseCommentTime(commentsBean.getTime()));
            GlideApp.with(mContext)
                    .load(commentsBean.getAvatar())
                    .transform(new GlideCircleTransform(mContext))
                    .into(holder.ivCommentAvatar);
        } else {


            ZhihuShortComments.CommentsBean commentsBean = mShortCommentSet.getComments().get(position - mLongCommentSet.getComments().size() - 2);
            holder.tvAuthor.setText(commentsBean.getAuthor());
            holder.tvCommentContent.setText(commentsBean.getContent());
            holder.tvThumbsUp.setText(commentsBean.getLikes());
            holder.tvCommentTime.setText(DateTimeUtil.parseCommentTime(commentsBean.getTime()));
            GlideApp.with(mContext)
                    .load(commentsBean.getAvatar())
                    .transform(new GlideCircleTransform(mContext))
                    .into(holder.ivCommentAvatar);

        }


    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 || position == mLongCommentSet.getComments().size() + 1)
            return TYPE_COMMENT_COUNT;
        else {
            return TYPE_COMMENT;
        }
    }

    @Override
    public int getItemCount() {
        if (mShowShortComments)
            return (mLongCommentSet != null && mShortCommentSet != null)                                     //长评论数据集  和   短评论数据集   都不能为空
                    ? mLongCommentSet.getComments().size() + mShortCommentSet.getComments().size() + 2       //+２是因为要显示　“ｘ条长评论”　和　“ｘ条短评论”　　两个Item项
                    : 0;
        else {
            return mLongCommentSet != null                                                                   //长评论数据集     不能为空
                    ? mLongCommentSet.getComments().size() + 2                                               //+２是因为要显示　“ｘ条长评论”　和　“ｘ条短评论”　　两个Item项
                    : 0;
        }

    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_comment_avatar)
        ImageView ivCommentAvatar;
        @BindView(R.id.tv_author)
        TextView tvAuthor;
        @BindView(R.id.tv_thumbs_up)
        TextView tvThumbsUp;
        @BindView(R.id.tv_comment_content)
        TextView tvCommentContent;
        @BindView(R.id.tv_comment_time)
        TextView tvCommentTime;

        CommentViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class CommentCountViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_comment_count)
        TextView tvCommentCount;
        @BindView(R.id.imageView)
        ImageView imageView;

        CommentCountViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
