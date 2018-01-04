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

import java.util.ArrayList;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 文件： MenuAdapter
 * 描述：
 * 作者： YangJunQuan   2018/1/4.
 */

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

    @BindDrawable(R.drawable.chevron_right)
    Drawable chevronRight;
    private ArrayList<String> mDataSet;


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_left_menu_recycleview, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvTheme.setText(mDataSet.get(position));
        holder.itemMenuContainer.setOnClickListener(view -> {

        });

//            holder.btnFocus.setOnClickListener(view -> {
//            holder.btnFocus.setBackground(chevronRight);
//        });

    }

    @Override
    public int getItemCount() {
        return mDataSet != null
                ? mDataSet.size()
                : 0;
    }

    public void setmDataSet(ArrayList<String> mDataSet) {
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
