package com.yjq.knowledge.zhihu;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yjq.knowledge.R;
import com.yjq.knowledge.adapter.ZhihuThemeListAdapter;
import com.yjq.knowledge.beans.zhihu.ZhihuThemeList;
import com.yjq.knowledge.beans.zhihu.ZhihuThemeListDetail;
import com.yjq.knowledge.network.ApiManager;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 文件： ZhihuThemeFragment
 * 描述：
 * 作者： YangJunQuan   2018/1/5.
 */

public class ZhihuThemeFragment extends Fragment {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.container_ll)
    CoordinatorLayout containerLl;
    Unbinder unbinder;


    private ZhihuThemeListAdapter mAdapter;
    private ZhihuThemeList.OthersBean mThemeBean;
    private static ZhihuThemeFragment mFragment;

    public static ZhihuThemeFragment newsInstance(ZhihuThemeList.OthersBean themeBean) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("themeBean", themeBean);
        getInstance().setArguments(bundle);
        return getInstance();
    }

    public static ZhihuThemeFragment getInstance(){
        if(mFragment == null){
            synchronized (ZhihuThemeFragment.class){
                if(mFragment == null) {
                    mFragment = new ZhihuThemeFragment();
                }
            }
        }
        return mFragment;
    }

    Map<Integer, ZhihuThemeListAdapter> map = new HashMap();
    public void changeDataSet(ZhihuThemeList.OthersBean themeBean){
        mThemeBean = themeBean;

        if(map.containsKey(themeBean.getId())){
            mAdapter = map.get(themeBean.getId());
            recyclerView.setAdapter(mAdapter);
        }else {
            initData();
        }
    }

    public void clearDataCache(){
        map.clear();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment, container, false);
        unbinder = ButterKnife.bind(this, v);

//        initPara();
        initView();
//        initData();

        return v;
    }

    private void initPara() {
        mThemeBean = (ZhihuThemeList.OthersBean) getArguments().getSerializable("themeBean");
    }

    private void initView() {
        mAdapter = new ZhihuThemeListAdapter(this);
        map.put(mThemeBean.getId(), mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(mAdapter);
        refreshLayout.setEnableRefresh(false);//不启用下拉刷新功能
        refreshLayout.setEnableLoadmore(false);//关闭上拉加载更多功能
    }

    private void initData() {


        ApiManager.getInstance().createZhihuService().getThemeDetailById(mThemeBean.getId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ZhihuThemeListDetail>() {
                    @Override
                    public void onCompleted() {
                        Logger.i("加载知乎日报【%s】主题列表数据完成", mThemeBean.getName());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("加载知乎日报【%s】主题列表数据失败", mThemeBean.getName());
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ZhihuThemeListDetail zhihuThemeListDetail) {
                        mAdapter = new ZhihuThemeListAdapter(ZhihuThemeFragment.this);
                        map.put(mThemeBean.getId(), mAdapter);
                        recyclerView.setAdapter(mAdapter);
                        mAdapter.setmDataSet(zhihuThemeListDetail);
                    }
                });

    }

    @Override
    public void onResume() {
        super.onResume();
        clearDataCache();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
