package com.yjq.knowledge.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.yjq.knowledge.juhe.JuheNewsFragment;
import com.yjq.knowledge.zhihu.ZhihuNewsTodayFragment;


/**
 * 文件： MainViewPagerAdapter
 * 描述：
 * 作者： Yang   2017/11/20.
 */

public class MainViewPagerAdapter extends FragmentPagerAdapter {
    private String[] typeList;

    public MainViewPagerAdapter(FragmentManager fm, String[] typeList) {
        super(fm);
        this.typeList = typeList;
    }

    @Override
    public Fragment getItem(int position) {
        return mIsZhihuFragment(position)
                ? ZhihuNewsTodayFragment.newInstance()
                : JuheNewsFragment.newsInstance(typeList[position]);
    }


    @Override
    public int getCount() {
        return typeList.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return transLateTitle(typeList[position]);
    }


    private boolean mIsZhihuFragment(int fragmentPosition) {
        return  "知乎日报".equals(getPageTitle(fragmentPosition));
    }

    private String transLateTitle(String target) {
        String result = "其它";
        switch (target) {
            case "junshi":
                return "军事";
            case "keji":
                return "科技";
            case "top":
                return "聚合数据";
            case "yule":
                return "娱乐";
            case "guoji":
                return "国际";
            case "caijing":
                return "财经";
            case "zhihu":
                return "知乎日报";
            default:
        }
        return result;
    }
}
