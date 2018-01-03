package com.yjq.knowledge.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.yjq.knowledge.R;
import com.yjq.knowledge.beans.juhe.JuheTop;
import com.yjq.knowledge.util.animate.ItemTouchHelperClass;

import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 文件： JuheNewsAdapter
 * 描述：
 * 作者： YangJunQuan   2017/11/17.
 */

public class JuheNewsAdapter extends RecyclerView.Adapter<JuheNewsAdapter.MyViewHolder> implements ItemTouchHelperClass.ItemTouchHelperAdapter {


    private boolean animationsLocked = false;
    private int lastAnimatedPosition = -1;
    private JuheTop mData;
    private boolean delayAnimation = true;

    public void resetAnimationState() {
        lastAnimatedPosition = -1;
        animationsLocked = false;
    }

    public void setmData(JuheTop mData) {
        this.mData = mData;
        this.notifyDataSetChanged();
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_juhe_recycleview, parent, false);
        return new MyViewHolder(rootView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        JuheTop.ResultBean.DataBean dataBean = mData.getResult().getData().get(position);

        runEnterAnimations(holder.itemView, position);
        holder.authorName.setText(dataBean.getAuthor_name());
        holder.date.setText(dataBean.getDate());
        holder.floor.setText(position + 1 + "楼");
        holder.title.setText(dataBean.getTitle());
        holder.url.setText(dataBean.getUrl());
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
                    .setStartDelay(delayAnimation ? 20 * (position) : 0)
                    .setInterpolator(new DecelerateInterpolator(0.5f))
                    .setDuration(400)
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
        return mData != null
                ? mData.getResult().getData().size()
                :0;
    }

    @Override
    public void onItemMoved(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {                                  //上面移到下面
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mData.getResult().getData(), i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mData.getResult().getData(), i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemRemoved(int position) {

        notifyItemRemoved(position);
    }


    static class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageView)
        ImageView imageView;
        @BindView(R.id.author_name)
        TextView authorName;
        @BindView(R.id.floor)
        TextView floor;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.url)
        TextView url;
        @BindView(R.id.card_view)
        CardView cardView;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


}
