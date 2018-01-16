package com.yjq.knowledge.adapter;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.yjq.knowledge.R;
import com.yjq.knowledge.beans.zhihu.ZhihuThemeList;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.subjects.PublishSubject;

/**
 * 文件： MenuAdapter
 * 描述：
 * 作者： YangJunQuan   2018/1/4.
 */

public class MenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_TOP = 0;
    private static final int TYPE_NOT_TOP = 1;

    private final PublishSubject<ZhihuThemeList.OthersBean> onClickSubject = PublishSubject.create();
    //PublishSubject，既是订阅者也是观察者，特点：订阅者只会接收到订阅之后被订阅者发送的消息，消息类型为Album相册
    //这和我们日常的手机订阅套餐服务很相似

    @BindDrawable(R.drawable.chevron_right)
    Drawable chevronRight;


    private ZhihuThemeList mDataSet;


    private List<Boolean> isClicks;//控件是否被点击,默认为false，如果被点击，改变值，控件根据值改变自身颜色

    public PublishSubject<ZhihuThemeList.OthersBean> getClicks() {
        return onClickSubject;
    }

    private void setIsClicks(int position) {
        isClicks.clear();
        for (int i = 0; i < mDataSet.getOthers().size() + 1; i++) {
            if (i == position) {
                isClicks.add(true);
                continue;
            }

            isClicks.add(false);
        }
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        switch (viewType) {
            case TYPE_TOP:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.top_item_menu_recycleview, parent, false);
                return new TopViewHolder(itemView);
            case TYPE_NOT_TOP:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_left_menu_recycleview, parent, false);
                return new ViewHolder(itemView);
            default:
                return null;
        }


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        int item_type = getItemViewType(position);
        switch (item_type) {
            case TYPE_TOP:
                initTopItem((TopViewHolder) holder, position);
                break;
            case TYPE_NOT_TOP:
                initNotTopItem((ViewHolder) holder, position);
                break;
            default:
        }


        if (isClicks.get(position)) {
            holder.itemView.setBackgroundColor(Color.parseColor("#ECEFF1"));
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
        }


    }

    private void initTopItem(TopViewHolder holder, int position) {
        holder.itemView.setOnClickListener(view -> {
                    setIsClicks(position);
                    onClickSubject.onNext(null);
                }
        );

    }

    private void initNotTopItem(ViewHolder holder, int position) {
        ZhihuThemeList.OthersBean themeBean = mDataSet.getOthers().get(position - 1);
        holder.tvTheme.setText(themeBean.getName());
        holder.itemView.setOnClickListener(view -> {
            setIsClicks(position);
            onClickSubject.onNext(themeBean);
        });
    }


    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return TYPE_TOP;
            default:
                return TYPE_NOT_TOP;
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet != null
                ? mDataSet.getOthers().size() + 1  // + 1是因为 顶部项“首页”
                : 0;
    }

    public void setmDataSet(ZhihuThemeList mDataSet) {

        isClicks = new ArrayList<>();
        for (int i = 0; i < mDataSet.getOthers().size() + 1; i++) {
            if (i == 0) {
                isClicks.add(true);
                continue;
            }

            isClicks.add(false);
        }
        this.mDataSet = mDataSet;


        this.notifyDataSetChanged();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_theme)
        TextView tvTheme;
        @BindView(R.id.btn_focus)
        ImageButton btnFocus;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class TopViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.page_home)
        TextView pageHome;

        TopViewHolder(View view) {
            super(view);
        }
    }

}
