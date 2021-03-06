package com.yjq.knowledge.util.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 文件： DateTimeUtil
 * 描述：
 * 作者： YangJunQuan   2018/1/2.
 */

public class DateTimeUtil {
    public static String getTime(String pTime) {

        String Week = " 星期";

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(pTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            Week += "天";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 2) {
            Week += "一";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 3) {
            Week += "二";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 4) {
            Week += "三";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 5) {
            Week += "四";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 6) {
            Week += "五";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 7) {
            Week += "六";
        }

        StringBuffer sb = new StringBuffer(pTime);

        return sb.substring(4, 6) + "月" + sb.substring(6, 8) + "日" + Week;
    }

    public static String parseCommentTime(String origin) {
        SimpleDateFormat format = new SimpleDateFormat("MM-dd   HH:mm");
        Long time = new Long(origin);
        String target = format.format(time);

        return target;
    }
}
