package com.yjq.knowledge.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yjq.knowledge.GlideApp;
import com.yjq.knowledge.R;
import com.yjq.knowledge.beans.zhihu.ZhihuThemeListDetail;
import com.yjq.knowledge.editor.EditorListActivity;
import com.yjq.knowledge.util.GlideCircleTransform;
import com.yjq.knowledge.zhihu.ZhihuThemeFragment;
import com.yjq.knowledge.zhihuNewsdetail.ZhihuNewsDetailActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 文件： ZhihuThemeListAdapter
 * 描述：
 * 作者： YangJunQuan   2018/1/5.
 */

public class ZhihuThemeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ZhihuThemeListDetail mDataSet;
    private ArrayList<ZhihuThemeListDetail.EditorsBean> mEditorList;
    private static final int TYPE_AVATAR_LIST = 0;
    private static final int TYPE_CONTENT = 1;
    private ZhihuThemeFragment zhihuThemeFragment;
    private Context mContext;

    public ZhihuThemeListAdapter(ZhihuThemeFragment zhihuThemeFragment) {
        this.zhihuThemeFragment = zhihuThemeFragment;
        this.mContext = zhihuThemeFragment.getContext();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (TYPE_AVATAR_LIST == viewType) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.editor_avator, parent, false);             //RecycleView的的顶部视图主编 主编的头像
            return new TopViewHolder(itemView);
        } else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_zhihu_recycleview, parent, false);
            return new ContentViewHolder(itemView);
        }
    }


    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return TYPE_AVATAR_LIST;   //顶部视图
            default:
                return TYPE_CONTENT;       //内容
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        if (getItemViewType(position) == TYPE_CONTENT) {

            ZhihuThemeListDetail.StoriesBean storiesBean = mDataSet.getStories().get(position - 1);


            if (null != storiesBean.getImages()) {                    //某些新闻有可能出现没有图片的情况

                GlideApp.with(zhihuThemeFragment)
                        .load(storiesBean.getImages().get(0))
                        .into(((ContentViewHolder) holder).imageZhihu);
            }

            ((ContentViewHolder) holder).tvAuthorZhihu.setText(String.format("主题【%s】", mDataSet.getName()));
            ((ContentViewHolder) holder).tvTitleZhihu.setText(storiesBean.getTitle());
            ((ContentViewHolder) holder).itemView.setOnClickListener((view) -> {
                Intent i = new Intent(zhihuThemeFragment.getContext(), ZhihuNewsDetailActivity.class);
                i.putExtra("newsId", storiesBean.getId());
                zhihuThemeFragment.startActivity(i);
            });


            return;
        }

        //RecycleView顶部视图的ListView的初始化操作

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, EditorListActivity.class);
            intent.putExtra(EditorListActivity.EDITOR_LIST_DATA, mEditorList);
            mContext.startActivity(intent);
        });
        LinearLayout imageContainer = ((TopViewHolder) holder).ll_avatar;
        imageContainer.removeAllViews();
        for (int i = 0; i < mEditorList.size(); i++) {
            ImageView v = new ImageView(mContext);

            GlideApp.with(zhihuThemeFragment)
                    .load(mEditorList.get(i).getAvatar())
                    .override(110, 110)
                    .transform(new GlideCircleTransform(mContext))
                    .into(v);
            imageContainer.addView(v);
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet != null
                ? mDataSet.getStories().size() + 1
                : 0;
    }

    public void setmDataSet(ZhihuThemeListDetail mDataSet) {
        this.mDataSet = mDataSet;
        this.mEditorList = mDataSet.getEditors();
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


    static class TopViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ll_avatar)
        LinearLayout ll_avatar;

        TopViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
