package com.yjq.knowledge;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.orhanobut.logger.Logger;
import com.yjq.knowledge.adapter.MenuAdapter;
import com.yjq.knowledge.beans.zhihu.ZhihuThemeList;
import com.yjq.knowledge.network.ApiManager;
import com.yjq.knowledge.zhihu.ZhihuNewsTodayFragment;
import com.yjq.knowledge.zhihu.ZhihuThemeFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    //@BindView(R.id.tabLayout)
    //TabLayout tabLayout;
    // @BindView(R.id.viewPager)
    //ViewPager viewPager;
    @BindView(R.id.nav_menu)
    NavigationView navMenu;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.frag_container)
    FrameLayout fragContainer;

    private View headerView;
    private RecyclerView rcyMenu;
    private MenuAdapter menuAdapter;
    private Fragment currentFragment = ZhihuNewsTodayFragment.newInstance();
    private HashMap<Integer, ZhihuThemeFragment> themefragmentsList = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initToolbar();
        initNavView();
        initData();
        initEvent();
        getSupportFragmentManager().beginTransaction().add(R.id.frag_container, currentFragment, currentFragment.getClass().getName()).commit();  //初始化知乎日报首页

    }

    /**
     * 各种事件处理
     */
    private void initEvent() {
        menuAdapter.getClicks().observeOn(AndroidSchedulers.mainThread())    //左侧边栏菜单Item项的点击事件，具体是指点击某个主题日报时
                .subscribeOn(Schedulers.io())
                .subscribe(othersBean -> {
                    switchFragment(getTargetFragment(othersBean), othersBean.getId() + "").commit();

                    drawerLayout.closeDrawer(GravityCompat.START);
                    toolbar.setTitle(othersBean.getName());
                });
    }

    private FragmentTransaction switchFragment(Fragment targetFragment, String identifiedId) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!targetFragment.isAdded()) {
            //第一次使用switchFragment()时currentFragment为null，所以要判断一下
            if (currentFragment != null) {
                transaction.hide(currentFragment);
            }
            transaction.add(R.id.frag_container, targetFragment, identifiedId);//通过主题Id标志特定主题的Fragment
        } else {
            transaction.hide(currentFragment)
                    .show(targetFragment);
        }
        currentFragment = targetFragment;
        return transaction;

    }

    /**
     * 从Fragment容器中是否包含特定主题的Fragment，包含则返回该Fragment，不包含则实例化一个新的Fragment并且返回该Fragment
     *
     * @param othersBean
     * @return
     */
    private ZhihuThemeFragment getTargetFragment(ZhihuThemeList.OthersBean othersBean) {
        ZhihuThemeFragment targetFragment;

        if (themefragmentsList.containsKey(othersBean.getId())) {
            targetFragment = themefragmentsList.get(othersBean.getId());
        } else {
            targetFragment = ZhihuThemeFragment.newsInstance(othersBean);
            themefragmentsList.put(othersBean.getId(), targetFragment);
        }

        return targetFragment;
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    /**
     * 侧边NavigationView的初始化操作
     */
    private void initNavView() {
        navMenu.setNavigationItemSelectedListener(this);
        headerView = navMenu.getHeaderView(0);
        rcyMenu = headerView.findViewById(R.id.rcy_menu);
        menuAdapter = new MenuAdapter();
        rcyMenu.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        rcyMenu.setAdapter(menuAdapter);
    }


    /**
     * 加载侧边栏Menu所需要的知乎日报主题列表相关信息
     */
    private void initData() {
        ApiManager.getInstance().createZhihuService().getThemeList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ZhihuThemeList>() {
                    @Override
                    public void onCompleted() {
                        Logger.i("加载【知乎日报主题列表】数据成功！");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("加载【知乎日报主题列表】数据失败！");
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ZhihuThemeList zhihuThemeList) {
                        menuAdapter.setmDataSet(zhihuThemeList);
                    }
                });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}
