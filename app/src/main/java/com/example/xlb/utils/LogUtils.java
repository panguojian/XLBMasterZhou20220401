package com.example.xlb.utils;

import android.util.Log;

public class LogUtils {
	public static final String LOGKEY = "zq";
	public static void Logs_e(String content){
		Log.e(LOGKEY, content);
	}
	
	public static void Logs_i(String content){
		Log.i(LOGKEY, content);
	}

}
