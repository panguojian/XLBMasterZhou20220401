package com.example.xlb.utils;

import android.app.Activity;
import android.icu.util.Calendar;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.xlb.config.SetConfig;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class FunctionUtils {

    static int i=0;
    static TimerTask ttk;
    static Timer timer;
    //设置Toast对象
    private static Toast mToast = null;
    public static void showTextToast(Activity activity,String msg) {
        //判断队列中是否包含已经显示的Toast
        if (mToast == null) {
            mToast = Toast.makeText(activity, msg, Toast.LENGTH_SHORT);
            // mToast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        }else{
            mToast.setText(msg);
        }
        mToast.show();
    }


    public static String reverse(String str) {
        return new StringBuilder(str).reverse().toString();
    }


    public static void  delayed()
    {
        System.out.println("开始执行");
        timer = new Timer();// 实例化Timer类
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                SetConfig.LOAD_FIRST=true;
                i++;
                System.out.println("一直在执行"+i);
            }
        }, 20000);// 这里毫秒


//        if(ttk==null)
//        {
//            ttk=new TimerTask() {
//                @Override
//                public void run() {
//                    SetConfig.LOAD_FIRST=true;
//                    i++;
//                    System.out.println("一直在执行"+i);
//                }
//            };
//        }
//        if(timer==null)
//        {
//            timer = new Timer();// 实例化Timer类
//        }
//        else
//        {
//            timer.schedule(ttk, 20000);// 这里毫秒
//        }


    }


    //获取N天前的日期

   // @RequiresApi(api = Build.VERSION_CODES.N)
    public static String getOldDate(int distanceDay) {
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-M-d");
        Date beginDate = new Date();
        Date endDate = null;
        Calendar date=null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            date = Calendar.getInstance();
            date.setTime(beginDate);
            date.set(Calendar.DATE, date.get(Calendar.DATE) - distanceDay);    //加号为N天前
//        date.set(Calendar.DATE, date.get(Calendar.DATE) + distanceDay); //加号为N天后

            try {
                endDate = dft.parse(dft.format(date.getTime()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dft.format(endDate);
    }


}
