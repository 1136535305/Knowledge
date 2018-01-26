package com.yjq.knowledge.util;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.yjq.knowledge.GlideApp;
import com.youth.banner.loader.ImageLoader;

/**
 * 文件： BannerImageLoader
 * 描述： 顶部轮播图辅助类：决定后台图片加载框架
 * 作者： YangJunQuan   2018/1/11.
 */

public class BannerImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {

        //Glide 加载图片简单用法
        GlideApp.with(context).load(path).into(imageView);


    }


}