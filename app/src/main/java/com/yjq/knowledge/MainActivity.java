package com.yjq.knowledge;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.orhanobut.logger.Logger;
import com.yjq.knowledge.adapter.MainViewPagerAdapter;
import com.yjq.knowledge.adapter.MenuAdapter;
import com.yjq.knowledge.beans.zhihu.ZhihuThemeList;
import com.yjq.knowledge.network.ApiManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.nav_menu)
    NavigationView navMenu;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private View headerView;
    private RecyclerView rcyMenu;
    private MenuAdapter menuAdapter;
    //private String[] typeList={"keji","junshi","top","yule","guoji","caijing"};    //测试数据：聚合数据提供的类别
    private String[] typeList = {"zhihu", "top"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initToolbar();
        initNavView();
        initData();

        viewPager.setOffscreenPageLimit(typeList.length - 1);                   //一次性初始化typeList-1+1页，所以初始化时间比较久，但是随后的切换不会卡顿因为都已经初始化完毕了
        viewPager.setAdapter(new MainViewPagerAdapter(getSupportFragmentManager(), typeList));
        tabLayout.setupWithViewPager(viewPager);


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

                        ArrayList themeList = new ArrayList<String>();
                        for (ZhihuThemeList.OthersBean othersBean : zhihuThemeList.getOthers()) {
                            themeList.add(othersBean.getName());
                        }
                        menuAdapter.setmDataSet(themeList);
                        Logger.i(themeList.toString());

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
