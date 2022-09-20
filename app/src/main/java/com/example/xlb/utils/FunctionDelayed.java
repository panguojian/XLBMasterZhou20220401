package com.example.xlb.utils;

import android.os.Handler;

import com.example.xlb.config.SetConfig;

public class FunctionDelayed {

    static int i;
    public static Handler handlerTimer=new Handler();
    public static Runnable runnable =new Runnable() {
        @Override
        public void run() {

            SetConfig.W_WAR=true;
            for(int i=0;i<9;i++)
            {
                SetConfig.W_WAR_ARRY[i]=true;
            }
            i++;
          //  System.out.println("一直在执行"+i);
            handlerTimer.postDelayed(this, 300000);//300000
        }
    };
}
