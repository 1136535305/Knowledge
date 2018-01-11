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

import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 文件： ZhihuThemeListAdapter
 * 描述：
 * 作者： YangJunQuan   2018/1/5.
 */

public class ZhihuThemeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private static final int TYPE_TOP_BACKGROUND = -1;
    private static final int TYPE_AVATAR_LIST = 0;
    private static final int TYPE_CONTENT = 1;
    private ZhihuThemeListDetail mDataSet;
    private ArrayList<ZhihuThemeListDetail.EditorsBean> mEditorList;
    private ZhihuThemeFragment zhihuThemeFragment;
    private Context mContext;

    public ZhihuThemeListAdapter(ZhihuThemeFragment zhihuThemeFragment) {
        this.zhihuThemeFragment = zhihuThemeFragment;
        this.mContext = zhihuThemeFragment.getContext();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        switch (viewType) {
            case TYPE_TOP_BACKGROUND:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.top_item_zhihu_theme_recycleview, parent, false);
                return new TopViewHolder(itemView);
            case TYPE_AVATAR_LIST:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.editor_avator, parent, false);             //RecycleView的的顶部视图主编 主编的头像
                return new EditorViewHolder(itemView);
            case TYPE_CONTENT:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_zhihu_recycleview, parent, false);
                return new ContentViewHolder(itemView);
            default:
                return null;
        }

    }


    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return TYPE_TOP_BACKGROUND;   //顶部视图
            case 1:
                return TYPE_AVATAR_LIST;     //主编横向列表
            default:
                return TYPE_CONTENT;       //内容
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);

        switch (viewType) {
            case TYPE_TOP_BACKGROUND:
                initThemeDescriptionView((TopViewHolder) holder);
                break;
            case TYPE_AVATAR_LIST:
                initEditorAvatarView(holder);
                break;
            case TYPE_CONTENT:
                initContentView((ContentViewHolder) holder, position);
                break;
            default:
                break;
        }
    }

    /**
     * 初始化RecyclerView  Item  第 0 项 视图  ： 该主题日报的背景图片和主题介绍
     */
    private void initThemeDescriptionView(TopViewHolder holder) {

        holder.tvThemeDescri.setText(mDataSet.getDescription());   //日报主题的相应介绍
        GlideApp.with(zhihuThemeFragment)                          //日报主题的相应背景图片
                .load(mDataSet.getBackground())
                .centerCrop()
                .into(holder.ivThemebg);

    }

    /**
     * 初始化RecyclerView  Item  第 1 项 视图  ：  该主题日报的主编头像列表视图
     */
    private void initEditorAvatarView(RecyclerView.ViewHolder holder) {
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, EditorListActivity.class);
            intent.putExtra(EditorListActivity.EDITOR_LIST_DATA, mEditorList);
            mContext.startActivity(intent);
        });
        LinearLayout imageContainer = ((EditorViewHolder) holder).ll_avatar;
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

    /**
     * 初始化RecyclerView  Item  其余项 视图   :   真正的新闻内容视图
     */
    private void initContentView(ContentViewHolder holder, int position) {
        ZhihuThemeListDetail.StoriesBean storiesBean = mDataSet.getStories().get(position - 2);

        if (null != storiesBean.getImages()) {

            holder.imageZhihu.setVisibility(View.VISIBLE);
            GlideApp.with(zhihuThemeFragment)
                    .load(storiesBean.getImages().get(0))
                    .centerCrop()
                    .into(holder.imageZhihu);

        } else {
            holder.imageZhihu.setVisibility(View.GONE); //某些新闻有可能出现没有图片的情况,这时候隐藏ImageView,适应没有图片的情况
        }

        holder.tvTitleZhihu.setText(storiesBean.getTitle());
        holder.itemView.setOnClickListener((view) -> {
            Intent i = new Intent(zhihuThemeFragment.getContext(), ZhihuNewsDetailActivity.class);
            i.putExtra("newsId", storiesBean.getId());
            zhihuThemeFragment.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return mDataSet != null
                ? mDataSet.getStories().size() + 2
                : 0;
    }

    public void initDataSet(ZhihuThemeListDetail mDataSet) {
        if (this.mDataSet == null) {                                   //null,代表是初次设置数据集
            this.mDataSet = mDataSet;
            this.mEditorList = mDataSet.getEditors();
            this.notifyDataSetChanged();
        }

        int position = mDataSet.getStories().size() - 1;              //非null，代表是上拉加载更多
        this.mDataSet.getStories().addAll(mDataSet.getStories());
        this.mEditorList = mDataSet.getEditors();
        this.notifyItemChanged(position);
    }

    public void resetDataSet(ZhihuThemeListDetail mDataSet) {         //这个方法当相应的主题被切换的时候重新设置相应的主题数据源
        this.mDataSet = mDataSet;
        this.mEditorList = mDataSet.getEditors();
        this.notifyDataSetChanged();
    }


    static class TopViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_theme_descri)
        TextView tvThemeDescri;
        @BindView(R.id.iv_theme_bg)
        ImageView ivThemebg;

        TopViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class EditorViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ll_avatar)
        LinearLayout ll_avatar;

        EditorViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    /**
     * 具体的新闻Item项ViewHolder
     */
    static class ContentViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image_zhihu)
        ImageView imageZhihu;
        @BindView(R.id.tv_title_zhihu)
        TextView tvTitleZhihu;

        ContentViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


}
