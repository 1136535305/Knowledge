package com.yjq.knowledge.juhe;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yjq.knowledge.R;
import com.yjq.knowledge.adapter.JuheNewsAdapter;
import com.yjq.knowledge.beans.JuheTop;
import com.yjq.knowledge.contract.JuheContract;
import com.yjq.knowledge.util.ItemTouchHelperClass;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class JuheNewsFragment extends Fragment implements JuheContract.Iview {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    Unbinder unbinder;

    @BindView(R.id.container_ll)
    CoordinatorLayout containerLl;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private JuheNewsAdapter mAdapter;
    private JuheContract.Ipresenter mPresenter;
    private ItemTouchHelper itemTouchHelper;


    public static JuheNewsFragment newsInstance(String type) {
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        JuheNewsFragment newsFragment = new JuheNewsFragment();
        newsFragment.setArguments(bundle);
        return newsFragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_juhe, container, false);
        unbinder = ButterKnife.bind(this, v);


        mAdapter = new JuheNewsAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

        itemTouchHelper = new ItemTouchHelper(new ItemTouchHelperClass((mAdapter)));
        itemTouchHelper.attachToRecyclerView(mRecyclerView);


        refreshLayout.setOnRefreshListener((refreshlayout) -> {
            mPresenter.loadNews(getArguments().getString(("type")));
        });
        refreshLayout.setEnableLoadmore(false);


        mPresenter = new JuheNewsPresenter(this);
        mPresenter.loadNews(getArguments().getString("type"));
       /* ApiManager.getInstance().createJuheService().getTop("yule")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<JuheTop>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(JuheTop juheTop) {
                       Toast.makeText(getContext(),juheTop.getReason(),Toast.LENGTH_LONG).show();
            }
        });
*/
        return v;
    }


    @Override
    public void showNews(JuheTop top) {

        refreshLayout.finishRefresh(0);
        mAdapter.resetAnimationState();
        mAdapter.setmData(top);
    }

    @Override
    public void showErrorMessage(String message) {

        refreshLayout.finishRefresh(0);
        Snackbar.make(containerLl, message, Snackbar.LENGTH_INDEFINITE)
                .setAction("查看請求情況", v -> {
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.juhe.cn/apireport/index/id/548745"));
                    startActivity(i);

                })
                .show();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
