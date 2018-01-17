package com.yjq.knowledge.util.animate;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

/**
 * 文件： A
 * 描述：
 * 作者： YangJunQuan   2018/1/17.
 */

public class MyLinearLayoutManager extends LinearLayoutManager {
    public MyLinearLayoutManager(Context context) {
        super(context);
    }

    public MyLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public MyLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        MyLinearSmoothScroller linearSmoothScroller =
                new MyLinearSmoothScroller(recyclerView.getContext());
        linearSmoothScroller.setTargetPosition(position);
        startSmoothScroll(linearSmoothScroller);
    }
}

class MyLinearSmoothScroller extends LinearSmoothScroller {

    public MyLinearSmoothScroller(Context context) {
        super(context);
    }

    /**
     * @param viewStart      RecyclerView的top位置
     * @param viewEnd        RecyclerView的Bottom位置
     * @param boxStart       item的top位置
     * @param boxEnd         item的bottom位置
     * @param snapPreference 滑动方向的识别
     * @return
     */
    @Override
    public int calculateDtToFit(int viewStart, int viewEnd, int boxStart, int boxEnd, int snapPreference) {
        return boxStart - viewStart;//返回的就是我们item置顶需要的偏移量
    }

    /**
     * 此方法返回滚动每1px需要的时间,可以用来控制滚动速度
     * 即如果返回2ms，则每滚动1000px，需要2秒钟
     *
     * @param displayMetrics
     * @return
     */
    @Override
    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
        return super.calculateSpeedPerPixel(displayMetrics);
    }
}