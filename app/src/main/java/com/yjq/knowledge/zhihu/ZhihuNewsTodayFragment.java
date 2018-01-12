package com.yjq.knowledge.zhihu;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yjq.knowledge.R;
import com.yjq.knowledge.adapter.ZhihuTodayListAdapter;
import com.yjq.knowledge.beans.zhihu.ZhihuDaily;
import com.yjq.knowledge.contract.ZhihuContract;
import com.yjq.knowledge.util.date.DateTimeUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ZhihuNewsTodayFragment extends Fragment implements ZhihuContract.Iview, View.OnClickListener {

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    private ZhihuContract.Ipresenter mPresenter;
    private ZhihuTodayListAdapter mAdapter;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.container_ll)
    CoordinatorLayout containerLl;
    Unbinder unbinder;


    private String lastNewsData;
    private static ZhihuNewsTodayFragment mFragment;

    public static ZhihuNewsTodayFragment getInstance() {
        if (mFragment == null) {
            synchronized (ZhihuThemeFragment.class) {
                if (mFragment == null) {
                    mFragment = new ZhihuNewsTodayFragment();
                }
            }
        }
        return mFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        mAdapter = new ZhihuTodayListAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        fab.setOnClickListener(this);


        refreshLayout.setOnRefreshListener(refreshlayout ->
                mPresenter.loadZhihuNews()
        );
        refreshLayout.setOnLoadmoreListener(refreshlayout ->
                mPresenter.loadMoreZhihuNews(lastNewsData)
        );
        mPresenter = new ZhihuNewsTodayPresenter(this);
        mPresenter.loadZhihuNews();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void showNews(ZhihuDaily zhihuDaily) {
        refreshLayout.finishRefresh(0/*,false*/);//传入false表示刷新失败
        lastNewsData = zhihuDaily.getDate();
        mAdapter.resetDataList("今日热闻", zhihuDaily);
    }

    @Override
    public void showMoreNews(ZhihuDaily zhihuDaily) {
        refreshLayout.finishLoadmore(0/*,false*/);//传入false表示加载失败
        lastNewsData = zhihuDaily.getDate();
        mAdapter.setmDataList(DateTimeUtil.getTime(lastNewsData), zhihuDaily);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.fab:
                recyclerView.smoothScrollToPosition(0);   //快速回到頂部的FAB按鈕响应事件
                break;
            default:
                break;
        }
    }
}
