package com.yjq.knowledge.zhihu;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yjq.knowledge.R;
import com.yjq.knowledge.adapter.ZhihuNewsAdapter;
import com.yjq.knowledge.beans.ZhihuDaily;
import com.yjq.knowledge.contract.ZhihuContract;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ZhihuNewsFragment extends Fragment implements ZhihuContract.Iview {

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private ZhihuContract.Ipresenter mPresenter;
    private ZhihuNewsAdapter mAdapter;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.container_ll)
    CoordinatorLayout containerLl;
    Unbinder unbinder;


    private String lastNewsData;

    public ZhihuNewsFragment() {
    }

    public static ZhihuNewsFragment newInstance() {
        ZhihuNewsFragment fragment = new ZhihuNewsFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_juhe, container, false);
        unbinder = ButterKnife.bind(this, view);

        mAdapter = new ZhihuNewsAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        refreshLayout.setOnRefreshListener(refreshlayout -> {
            mPresenter.loadZhihuNews();
        });
        refreshLayout.setOnLoadmoreListener(refreshlayout -> {
            mPresenter.loadMoreZhihuNews(lastNewsData);
        });
        mPresenter = new ZhihuNewsPresenter(this);
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
        mAdapter.resetAnimationState();
        mAdapter.resetDataList(zhihuDaily.getStories());
    }

    @Override
    public void showMoreNews(ZhihuDaily zhihuDaily) {
        refreshLayout.finishLoadmore(0/*,false*/);//传入false表示加载失败
        lastNewsData = zhihuDaily.getDate();
        mAdapter.setmDataList(zhihuDaily.getStories());
    }
}
