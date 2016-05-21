package simit.org.jobbole.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import simit.org.jobbole.config.JobboleConstants;

/**
 * Created by liuchun on 2016/4/2.
 */
public class JobUtil {

    /** 转换成系统所在时区时间 */
    public static Date toSystemTime(String src, String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        try {
            Date date = sdf.parse(src);
            Date curDate = new Date();
            curDate.setTime(date.getTime());
            return curDate;
        }catch (ParseException e){
            e.printStackTrace();
        }

        return new Date();
    }

    /** 转换成特定的格式 */
    public static String format(Date date, String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());

        return sdf.format(date);
    }

    /** 转成成显示时间 */
    public static String toDisplayTime(String src){
        Date date = toSystemTime(src, JobboleConstants.DEFAULT_TIME_FORMAT);

        return format(date, JobboleConstants.DISPLAY_TIME_FORMAT);
    }

    /** 把Bitmap裁剪成正方形,转化成Drawable */
    public static Drawable createCircleDrawable(Context context, int resId){
        Bitmap src = BitmapFactory.decodeResource(context.getResources(), resId);
        Bitmap dst;
        if(src.getWidth() >= src.getHeight()){
            dst = Bitmap.createBitmap(src, src.getWidth()/2 - src.getHeight()/2, 0, src.getHeight(), src.getHeight());
        }else{
            dst = Bitmap.createBitmap(src, 0, src.getHeight()/2 - src.getWidth()/2, src.getWidth(), src.getWidth());
        }
        //
        Log.d("JobUtil", "width: " + dst.getWidth() + ",height: " + dst.getHeight());
        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(context.getResources(), dst);
        drawable.setCircular(true);

        return drawable;
    }
}
