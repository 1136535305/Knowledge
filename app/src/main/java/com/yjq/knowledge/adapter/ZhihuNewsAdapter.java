package com.yjq.knowledge.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yjq.knowledge.R;
import com.yjq.knowledge.beans.zhihu.ZhihuDaily;
import com.yjq.knowledge.zhihu.ZhihuNewsFragment;
import com.yjq.knowledge.zhihuNewsdetail.ZhihuNewsDetailActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 文件： ZhihuNewsAdapter
 * 描述：
 * 作者： YangJunQuan   2017/12/15.
 */


public class ZhihuNewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private static final int TYPE_DATE = 0;
    private static final int TYPE_CONTENT = 1;
    private List mDataList = new ArrayList<>();        //存放Item具体内容的列表以及日期的数据集
    private ZhihuNewsFragment mFragment;
    private boolean animationsLocked = false;
    private int lastAnimatedPosition = -1;
    private boolean delayAnimation = true;


    public void setmDataList(String date, List<ZhihuDaily.StoriesBean> mDataList) {
        int position = mDataList.size() - 1;
        this.mDataList.add(date);
        this.mDataList.addAll(mDataList);
        notifyItemChanged(position);               //位置从0开始
    }

    public void resetDataList(String date, List<ZhihuDaily.StoriesBean> mDataList) {
        this.mDataList.clear();
        this.mDataList.add(date);
        this.mDataList.addAll(mDataList);
        notifyDataSetChanged();
    }


    public void resetAnimationState() {
        lastAnimatedPosition = -1;
        animationsLocked = false;
    }


    public ZhihuNewsAdapter(ZhihuNewsFragment fragment) {
        mFragment = fragment;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_CONTENT) {              //内容Item
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_zhihu_recycleview, parent, false);
            return new ContentViewHolder(view);
        } else {                                     //日期Item
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.interval_item_zhihu_recycleview, parent, false);
            return new DateTopViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        runEnterAnimations(holder.itemView, position);//刷新动画


        if (holder instanceof ContentViewHolder) {     //内容Item项

            ZhihuDaily.StoriesBean storiesBean = (ZhihuDaily.StoriesBean) mDataList.get(position);
            ((ContentViewHolder) holder).tvTitleZhihu.setText(storiesBean.getTitle());
            holder.itemView.setOnClickListener((view) -> {
                Intent i = new Intent(mFragment.getContext(), ZhihuNewsDetailActivity.class);
                i.putExtra("storiesBean", storiesBean);
                mFragment.startActivity(i);
            });

            Glide.with(mFragment)
                    .load(storiesBean.getImages().get(0))
                    .into(((ContentViewHolder) holder).imageZhihu);

        } else {                                      //日期Item项
            ((DateTopViewHolder) holder).tvDate.setText((String) mDataList.get(position));
        }


    }

    @Override
    public int getItemViewType(int position) {
        if (mDataList.get(position) instanceof String)   //说明该位置是留给日期的
            return TYPE_DATE;
        else return TYPE_CONTENT;
    }


    private void runEnterAnimations(View view, int position) {
        if (animationsLocked) return;
        if (position > lastAnimatedPosition) {
            lastAnimatedPosition = position;
            view.setTranslationY(500);
            view.setAlpha(0.f);


            view.animate()
                    .alpha(1.f)
                    .translationY(0)
                    .setStartDelay(delayAnimation ? 30 * (position) : 0)
                    .setInterpolator(new DecelerateInterpolator(0.5f))
                    .setDuration(500)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            animationsLocked = true;
                        }
                    }).start();


        }

    }

    @Override
    public int getItemCount() {
        return mDataList != null
                ? mDataList.size()
                : 0;
    }


    /**
     * 具体的新闻Item项ViewHolder
     */
    static class ContentViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image_zhihu)
        ImageView imageZhihu;
        @BindView(R.id.tv_title_zhihu)
        TextView tvTitleZhihu;
        @BindView(R.id.tv_author_zhihu)
        TextView tvAuthorZhihu;

        ContentViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


    /**
     * 日期Item项ViewHolder
     */
    static class DateTopViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_date)
        TextView tvDate;

        public DateTopViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
