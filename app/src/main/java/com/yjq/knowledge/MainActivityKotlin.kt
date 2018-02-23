package com.yjq.knowledge

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import com.yjq.knowledge.adapter.MenuAdapter
import com.yjq.knowledge.beans.zhihu.ZhihuThemeList
import com.yjq.knowledge.network.ApiManager
import com.yjq.knowledge.newsTheme.ThemeFragmentKotlin
import com.yjq.knowledge.newsToday.NewsTodayFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * 文件： MainActivityKotlin
 * 描述：
 * 作者： YangJunQuan   2018/1/24.
 */
class MainActivityKotlin : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var currentFragment: Fragment = NewsTodayFragment.instance
    private val menuAdapter = MenuAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initToolbar()
        initNavView()
        initData()
        initEvent()
        supportFragmentManager.beginTransaction().add(R.id.frag_container, currentFragment, currentFragment.hashCode().toString()).commit()

    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

    }

    /**
     * 侧边NavigationView的一些初始化操作
     */
    private fun initNavView() {

        nav_menu.setNavigationItemSelectedListener(this)
        val rcyMenu = nav_menu.getHeaderView(0).findViewById<RecyclerView>(R.id.rcy_menu)   //kotlin貌似无法直接引用NavigationView里的header指向的layout文件里的控件引用
        rcyMenu.layoutManager = LinearLayoutManager(this)
        rcyMenu.adapter = menuAdapter
    }

    /**
     * 加载侧边栏Menu所需要的知乎日报主题列表相关信息
     */
    private fun initData() {
        ApiManager.instance.createZhihuService().themeList
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe { menuAdapter.setmDataSet(it) }
    }

    private fun initEvent() {
        menuAdapter.clicks
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe {

                    if (it == null) {
                        switchFragment(NewsTodayFragment.instance, "-1").commit()                   //替换首页代表的Fragment
                        toolbar.title = "今日热闻"                                                                    //替换Toolbar上的标题
                    } else {
                        switchFragment(getThemeFragment(it), it.id.toString()).commit()                             //替换其它主题Fragment
                        toolbar.title = it.name                                                                      //替换Toolbar上的标题
                    }

                    drawerLayout.closeDrawer(GravityCompat.START)                                                     //关闭左侧滑Menu}

                }


    }

    /**
     * 替换ZhihuThemeFragment里的数据
     *
     * @param othersBean
     * @return
     */
    private fun getThemeFragment(othersBean: ZhihuThemeList.OthersBean): Fragment {
        ThemeFragmentKotlin.getInstance().setDataSet(othersBean)       //替换ZhihuThemeFragment里的数据
        return ThemeFragmentKotlin.getInstance()
    }

    private fun switchFragment(targetFragment: Fragment, identifiedId: String): FragmentTransaction {
        val transaction = supportFragmentManager.beginTransaction()
        if (!targetFragment.isAdded) {
            // let{ } 默认使用当前对象作为闭包的it参数,非空执行代码块
            currentFragment.let { transaction.hide(it) }
            transaction.add(R.id.frag_container, targetFragment, identifiedId)//通过主题Id标志特定主题的Fragment

        } else {
            transaction.hide(currentFragment).show(targetFragment)
        }

        currentFragment = targetFragment
        return transaction

    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        //   ThemeFragmentKotlin.getInstance().clearDataCache()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_action_daynight -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

}