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
import com.yjq.knowledge.beans.zhihu.ZhihuThemeListDetail;
import com.yjq.knowledge.util.GlideCircleTransform;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.subjects.PublishSubject;

/**
 * 文件： EditorAdapter
 * 描述：
 * 作者： YangJunQuan   2018/1/9.
 */

public class EditorAdapter extends RecyclerView.Adapter<EditorAdapter.ContentViewHolder> {
    private PublishSubject<String> onClickSubject = PublishSubject.create();
    private Context mContext;
    private ArrayList<ZhihuThemeListDetail.EditorsBean> mDataSet;

    public EditorAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public ContentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_editor_theme_recycleview, parent, false);
        return new ContentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ContentViewHolder holder, int position) {

        ZhihuThemeListDetail.EditorsBean editorsBean = mDataSet.get(position);
        GlideApp.with(mContext)
                .load(editorsBean.getAvatar())
                .transform(new GlideCircleTransform(mContext))
                .into(holder.ivAtatar);

        holder.tvBio.setText(editorsBean.getBio());
        holder.tvEditorName.setText(editorsBean.getName());
        holder.itemView.setOnClickListener(view ->
                onClickSubject.onNext(editorsBean.getId() + "")
        );
    }

    @Override
    public int getItemCount() {
        return mDataSet != null
                ? mDataSet.size()
                : 0;
    }

    public void setmDataSet(ArrayList<ZhihuThemeListDetail.EditorsBean> mEditorList) {
        this.mDataSet = mEditorList;
        this.notifyDataSetChanged();
    }

    public PublishSubject<String> getOnClicks() {
        return onClickSubject;
    }


    static class ContentViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_atatar)
        ImageView ivAtatar;
        @BindView(R.id.tv_editor_name)
        TextView tvEditorName;
        @BindView(R.id.tv_bio)
        TextView tvBio;

        ContentViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
