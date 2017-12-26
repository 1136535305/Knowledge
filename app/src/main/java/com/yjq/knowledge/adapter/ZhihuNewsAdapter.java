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
import com.yjq.knowledge.beans.ZhihuDaily;
import com.yjq.knowledge.zhihu.ZhihuNewsFragment;
import com.yjq.knowledge.zhihuNewsdetail.ZhihuNewsDetailActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 文件： ZhihuNewsAdapter
 * 版权： 2017-2019  康佳集团股份有限公司
 * 保留多有版权
 * 描述：
 * 作者： YangJunQuan   2017/12/15.
 */


public class ZhihuNewsAdapter extends RecyclerView.Adapter<ZhihuNewsAdapter.ViewHolder> {



    private List<ZhihuDaily.StoriesBean> mDataList = new ArrayList<>();
    private ZhihuNewsFragment mFragment;
    private boolean animationsLocked = false;
    private int lastAnimatedPosition = -1;
    private boolean delayAnimation = true;

    public void setmDataList(List<ZhihuDaily.StoriesBean> mDataList) {
        int position = mDataList.size() - 1;
        this.mDataList.addAll(mDataList);
        notifyItemChanged(position);
    }

    public void resetDataList(List<ZhihuDaily.StoriesBean> mDataList) {
        this.mDataList = mDataList;
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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_zhihu_recycleview, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ZhihuDaily.StoriesBean storiesBean = mDataList.get(position);

        runEnterAnimations(holder.itemView, position);
        holder.tvTitleZhihu.setText(storiesBean.getTitle());
        holder.itemView.setOnClickListener((view) -> {
            Intent i = new Intent(mFragment.getContext(), ZhihuNewsDetailActivity.class);
            i.putExtra("storiesBean", storiesBean);
            mFragment.startActivity(i);

        });

        Glide.with(mFragment)
                .load(storiesBean.getImages().get(0))
                .into(holder.imageZhihu);


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

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image_zhihu)
        ImageView imageZhihu;
        @BindView(R.id.tv_title_zhihu)
        TextView tvTitleZhihu;
        @BindView(R.id.tv_author_zhihu)
        TextView tvAuthorZhihu;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
