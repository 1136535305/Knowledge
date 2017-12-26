package com.yjq.knowledge.util;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

/**
 * 文件： AnimateUtil
 * 版权： 2017-2019  康佳集团股份有限公司
 * 保留多有版权
 * 描述：
 * 作者： YangJunQuan   2017/12/25.
 */

public class AnimateUtil {

    public static  final String TARGET_SCREENLOCATION_LEFT ="X_LOCATION_ON_SCREEN";
    public  static  final String TARGET_SCREENLOCATION_TOP="Y_LOCATION_ON_SCREEN";
    public  static  final String TARGET_WIDTH="TARGET_WIDTH";
    public  static  final String TARGET_HEIGHT="TARGET_HEIGHT";
    /**
     * 用于获取两个Activity之间展示过渡效果的View的起始初始值(包括 X、Y屏幕坐标，view的起始宽度和高度)
     * @param view
     * @return
     */
    public static Bundle captureValues(@NonNull View view){
        Bundle b =new Bundle();
        int[] screenLocation=new int[2];
        view.getLocationOnScreen(screenLocation);
        b.putInt(TARGET_SCREENLOCATION_LEFT,screenLocation[0]);
        b.putInt(TARGET_SCREENLOCATION_TOP,screenLocation[1]);
        b.putInt(TARGET_WIDTH,view.getWidth());
        b.putInt(TARGET_HEIGHT,view.getHeight());
        return  b;
    }
}
