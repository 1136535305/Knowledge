package com.yjq.knowledge.newsTheme;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yjq.knowledge.R;
import com.yjq.knowledge.adapter.ThemeListAdapter;
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
 * 文件： ThemeFragment
 * 描述：
 * 作者： YangJunQuan   2018/1/5.
 */

public class ThemeFragment extends Fragment {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.container_ll)
    CoordinatorLayout containerLl;
    Unbinder unbinder;


    private ThemeListAdapter mAdapter;
    private ZhihuThemeList.OthersBean mThemeBean;
    private static ThemeFragment mFragment;
    private ZhihuThemeListDetail mCurrentData;
    private int lastNewsId;
    Map<Integer, ZhihuThemeListDetail> mDataListBuffer = new HashMap<Integer, ZhihuThemeListDetail>();


    public static ThemeFragment getInstance() {
        if (mFragment == null) {
            synchronized (ThemeFragment.class) {
                if (mFragment == null) {
                    mFragment = new ThemeFragment();
                }
            }
        }
        return mFragment;
    }

    public void setDataSet(ZhihuThemeList.OthersBean themeBean) {
        mThemeBean = themeBean;

        if (mDataListBuffer.containsKey(themeBean.getId())) {
            mCurrentData = mDataListBuffer.get(themeBean.getId());
            lastNewsId = mCurrentData.getStories().get(mCurrentData.getStories().size() - 1).getId();

            mAdapter.setLastAnimPosition(-1);
            mAdapter.resetDataSet(mCurrentData);
            recyclerView.scrollToPosition(0);

        } else {
            initData();
        }


    }

    public void clearDataCache() {
        mDataListBuffer.clear();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment, container, false);
        unbinder = ButterKnife.bind(this, v);

        initView();

        return v;
    }

    private void initView() {
        mAdapter = new ThemeListAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(mAdapter);
        refreshLayout.setEnableRefresh(false);//不启用下拉刷新功能
        refreshLayout.setOnLoadmoreListener(refreshlayout ->
                initMoreData()
        );
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
                        mDataListBuffer.put(mThemeBean.getId(), zhihuThemeListDetail);//请求到的数据添加到缓存的数据集中

                        mAdapter.setLastAnimPosition(-1);                             //由于我们是复用同一个Fragment和Adapter故当切换时我们需要重新恢复Adapter的初始状态
                        mAdapter.resetDataSet(zhihuThemeListDetail);                  //设置新的数据集
                        recyclerView.scrollToPosition(0);                             //切换时回到顶部


                        lastNewsId = zhihuThemeListDetail.getStories().get(zhihuThemeListDetail.getStories().size() - 1).getId();

                    }
                });

    }


    private void initMoreData() {
        ApiManager.getInstance().createZhihuService().loadMoreThemeNews(mThemeBean.getId(), lastNewsId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ZhihuThemeListDetail>() {
                    @Override
                    public void onCompleted() {
                        refreshLayout.finishLoadmore(0/*,false*/);//传入false表示加载失败
                        Logger.i("加载更多知乎日报【%s】主题列表数据完成", mThemeBean.getName());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("加载更多知乎日报【%s】主题列表数据失败", mThemeBean.getName());
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ZhihuThemeListDetail zhihuThemeListDetail) {
                        mDataListBuffer.get(mThemeBean.getId()).getStories().addAll(zhihuThemeListDetail.getStories());//请求到的数据添加到缓存的数据集中

                        mAdapter.initDataSet(zhihuThemeListDetail);

                        lastNewsId = zhihuThemeListDetail.getStories().get(zhihuThemeListDetail.getStories().size() - 1).getId();
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        mDataListBuffer.clear();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}