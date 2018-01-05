package com.yjq.knowledge.adapter;

import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.yjq.knowledge.R;
import com.yjq.knowledge.beans.zhihu.ZhihuThemeList;

import java.util.ArrayList;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.subjects.PublishSubject;

/**
 * 文件： MenuAdapter
 * 描述：
 * 作者： YangJunQuan   2018/1/4.
 */

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
    private final PublishSubject<ZhihuThemeList.OthersBean> onClickSubject = PublishSubject.create();
    //PublishSubject，既是订阅者也是观察者，特点：订阅者只会接收到订阅之后被订阅者发送的消息，消息类型为Album相册
    //这和我们日常的手机订阅套餐服务很相似

    @BindDrawable(R.drawable.chevron_right)
    Drawable chevronRight;
    private ZhihuThemeList mDataSet;


    public PublishSubject<ZhihuThemeList.OthersBean> getClicks() {
        return onClickSubject;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_left_menu_recycleview, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ZhihuThemeList.OthersBean themeBean = mDataSet.getOthers().get(position);
        holder.tvTheme.setText(themeBean.getName());
        holder.itemMenuContainer.setOnClickListener(view -> {

            onClickSubject.onNext(themeBean);
        });

//            holder.btnFocus.setOnClickListener(view -> {
//            holder.btnFocus.setBackground(chevronRight);
//        });

    }

    @Override
    public int getItemCount() {
        return mDataSet != null
                ? mDataSet.getOthers().size()
                : 0;
    }

    public void setmDataSet(ZhihuThemeList mDataSet) {
        this.mDataSet = mDataSet;
        this.notifyDataSetChanged();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_theme)
        TextView tvTheme;
        @BindView(R.id.btn_focus)
        ImageButton btnFocus;
        @BindView(R.id.item_menu_container)
        ConstraintLayout itemMenuContainer;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
