package com.yjq.knowledge.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yjq.knowledge.GlideApp;
import com.yjq.knowledge.R;
import com.yjq.knowledge.beans.zhihu.ZhihuDaily;
import com.yjq.knowledge.beans.zhihu.ZhihuThemeList;
import com.yjq.knowledge.beans.zhihu.ZhihuThemeListDetail;
import com.yjq.knowledge.zhihu.ZhihuThemeFragment;
import com.yjq.knowledge.zhihuNewsdetail.ZhihuNewsDetailActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 文件： ZhihuThemeListAdapter
 * 描述：
 * 作者： YangJunQuan   2018/1/5.
 */

public class ZhihuThemeListAdapter extends RecyclerView.Adapter<ZhihuThemeListAdapter.ContentViewHolder> {

    private ZhihuThemeListDetail mDataSet;

    private ZhihuThemeFragment zhihuThemeFragment;

    public ZhihuThemeListAdapter(ZhihuThemeFragment zhihuThemeFragment) {
        this.zhihuThemeFragment = zhihuThemeFragment;
    }


    @Override
    public ContentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_zhihu_recycleview, parent, false);

        return new ContentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ContentViewHolder holder, int position) {
        ZhihuThemeListDetail.StoriesBean storiesBean = mDataSet.getStories().get(position);

        holder.tvAuthorZhihu.setText(String.format("主题【%s】", mDataSet.getName()));
        holder.tvTitleZhihu.setText(storiesBean.getTitle());
        holder.itemView.setOnClickListener((view) -> {
            Intent i = new Intent(zhihuThemeFragment.getContext(), ZhihuNewsDetailActivity.class);
            i.putExtra("newsId", storiesBean.getId());
            zhihuThemeFragment.startActivity(i);
        });


        if (null != storiesBean.getImages()) {                    //某些新闻有可能出现没有图片的情况
            GlideApp.with(zhihuThemeFragment)
                    .load(storiesBean.getImages().get(0))
                    .into(holder.imageZhihu);
        }

    }

    @Override
    public int getItemCount() {
        return mDataSet != null
                ? mDataSet.getStories().size()
                : 0;
    }

    public void setmDataSet(ZhihuThemeListDetail mDataSet) {
        this.mDataSet = mDataSet;
        this.notifyDataSetChanged();
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

}
