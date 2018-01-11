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
import com.yjq.knowledge.util.BannerImageLoader;
import com.yjq.knowledge.zhihu.ZhihuNewsTodayFragment;
import com.yjq.knowledge.zhihuNewsdetail.ZhihuNewsDetailActivity;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 文件： ZhihuTodayListAdapter
 * 描述：
 * 作者： YangJunQuan   2017/12/15.
 */


public class ZhihuTodayListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_BANNER_TOP = -1;
    private static final int TYPE_DATE = 0;
    private static final int TYPE_CONTENT = 1;
    private List mDataList = new ArrayList<>();        //存放Item具体内容的列表以及日期的数据集
    private ArrayList<ZhihuDaily.TopStoriesBean> mTopStoriesList = new ArrayList<>();
    private ZhihuNewsTodayFragment mFragment;

    private boolean animationsLocked = false;
    private int lastAnimatedPosition = -1;
    private boolean delayAnimation = true;


    public void setmDataList(String date, ZhihuDaily zhihuDaily) {

        int position = zhihuDaily.getStories().size() - 1;
        this.mTopStoriesList = zhihuDaily.getTop_stories();
        this.mDataList.add(date);
        this.mDataList.addAll(zhihuDaily.getStories());
        notifyItemChanged(position);               //位置从0开始
    }

    public void resetDataList(String date, ZhihuDaily zhihuDaily) {
        this.mTopStoriesList = zhihuDaily.getTop_stories();
        this.mDataList.clear();
        this.mDataList.add(date);
        this.mDataList.addAll(zhihuDaily.getStories());
        notifyDataSetChanged();
    }


    public void resetAnimationState() {
        lastAnimatedPosition = -1;
        animationsLocked = false;
    }


    public ZhihuTodayListAdapter(ZhihuNewsTodayFragment fragment) {
        mFragment = fragment;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        switch (viewType) {
            case TYPE_BANNER_TOP:     //顶部轮播图
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.top_item_zhihu_today_recycleview, parent, false);
                return new BannerTopViewHolder(view);
            case TYPE_DATE:          //间隔显示的日期项
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.interval_item_zhihu_recycleview, parent, false);
                return new DateViewHolder(view);
            case TYPE_CONTENT:       //真正新闻内容项
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_zhihu_recycleview, parent, false);
                return new ContentViewHolder(view);
            default:
                break;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        //runEnterAnimations(holder.itemView, position);//刷新动画


        if (holder instanceof ContentViewHolder) {                                          //内容Item项

            initContentView(holder, position);

        } else if (holder instanceof DateViewHolder) {                                      //日期Item项

            initDateView((DateViewHolder) holder, position);

        } else if (holder instanceof BannerTopViewHolder) {

            initTopBannerView((BannerTopViewHolder) holder);                               //顶部Banner轮播图
        }


    }

    private void initDateView(DateViewHolder holder, int position) {
        holder.tvDate.setText((String) mDataList.get(position - 1));
    }

    private void initTopBannerView(BannerTopViewHolder holder) {

        List<String> imageList = new ArrayList<String>();
        List<String> titleList = new ArrayList<String>();

        for (ZhihuDaily.TopStoriesBean topStoriesBean : mTopStoriesList) {
            imageList.add(topStoriesBean.getImage());
            titleList.add(topStoriesBean.getTitle());
        }

        holder.banner.setImageLoader(new BannerImageLoader());
        holder.banner.setBannerStyle(3);
        holder.banner.setImages(imageList);
        holder.banner.setBannerTitles(titleList);
        holder.banner.setOnBannerListener(position ->
                startNewsDetailActivity(mTopStoriesList.get(position).getId())
        );
        holder.banner.start();
    }

    private void initContentView(RecyclerView.ViewHolder holder, int position) {
        ZhihuDaily.StoriesBean storiesBean = (ZhihuDaily.StoriesBean) mDataList.get(position - 1);
        ((ContentViewHolder) holder).tvTitleZhihu.setText(storiesBean.getTitle());
        holder.itemView.setOnClickListener((view) ->
                startNewsDetailActivity(storiesBean.getId())
        );

        Glide.with(mFragment)
                .load(storiesBean.getImages().get(0))
                .into(((ContentViewHolder) holder).imageZhihu);
    }

    private void startNewsDetailActivity(int newsId) {
        Intent i = new Intent(mFragment.getContext(), ZhihuNewsDetailActivity.class);
        i.putExtra("newsId", newsId);
        mFragment.startActivity(i);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return TYPE_BANNER_TOP;

        if (mDataList.get(position - 1) instanceof String)   //说明该位置是留给日期的
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
                ? mDataList.size() + 1    //  +1是因为Recycle的第一项是Banner轮播图
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
    static class DateViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_date)
        TextView tvDate;

        public DateViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    /**
     * 顶部轮播图项ViewHolder
     */
    static class BannerTopViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.banner)
        Banner banner;

        public BannerTopViewHolder(View itemView) {
            super((itemView));
            ButterKnife.bind(this, itemView);
        }
    }
}
