package com.example.xlb;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.xlb.utils.ByteUtils;
import com.example.xlb.utils.LogUtils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import android_serialport_api.IDataDeal;
import android_serialport_api.ReceiveComUtils;
import android_serialport_api.SerialPort;

import static java.lang.Thread.*;

public class MyService extends Service {

    /**************service 命令*********/
    static final int CMD_STOP_SERVICE = 0x01;
    static final int CMD_SEND_DATA = 0x02;
    static final int CMD_SYSTEM_EXIT =0x03;
    static final int CMD_SHOW_TOAST =0x04;

    private android_serialport_api.SerialPort serialPort = null;
    private ReceiveComUtils utils = null;
    private Receive receive = new Receive();

    public MyService() {

    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int i=0;
                while (isRunning()){
                    Log.i("ser",String.valueOf(i++));
                    try{
                        Thread.sleep(1000);
                    }
                   catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public boolean isRunning(){

        ActivityManager myManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager.getRunningServices(1000);
        for (int i = 0; i < runningService.size(); i++) {
            String ok=runningService.get(i).service.getClassName().toString();
            if (runningService.get(i).service.getClassName().toString().equals("com.example.xlb.MyService")) {
                return true;
            }
        }
        return false;

    }


    /**
     * 打开串口
     */
    private  void open(){
        try {
            serialPort = new SerialPort(new File("/dev/ttyS1"),
                    9600, 0);
            utils = new ReceiveComUtils(serialPort,receive);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(this,"串口打开",Toast.LENGTH_SHORT).show();
    }

    /**
     * 关闭串口
     */
    private void closeCOM(){
        if (serialPort != null) {
            try {
                serialPort.getInputStream().close();
                serialPort.getOutputStream().close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            serialPort.close();
            serialPort = null;
        }
        if (utils!=null){
            if (utils.mReadThread != null && utils.mReadThread.isAlive()) {
                LogUtils.Logs_e("---ReceiveComUtils.mReadThread.interrupt()----");
                utils.mReadThread.interrupt();
                utils.mReadThread = null;
                utils = null;
            }
        }
    }

    // 向串口写出数据
    private void writeCom(android_serialport_api.SerialPort serialPort,
                          byte[] content) {
        OutputStream mOutputStream = serialPort.getOutputStream();
//        byte[] text = commandStr.getBytes();
        try {
            mOutputStream.write(content);
            mOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 卡串口接收数据
    public class Receive implements IDataDeal {

        @Override
        public synchronized void onDataReceived(byte[] buffer, int size) {
            byte[] back = new byte[size];
            System.arraycopy(buffer, 0, back, 0, size);
            String content =  ByteUtils.printHexString(back);
            LogUtils.Logs_i("串口返回=="+ content);
            Message msg = new Message();
            msg.what = 100;
            msg.obj = content;
            //handler.sendMessage(msg);
        }
    }
}
