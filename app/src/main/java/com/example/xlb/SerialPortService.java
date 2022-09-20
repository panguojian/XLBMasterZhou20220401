package com.example.xlb;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

import com.example.xlb.config.SetConfig;
import com.example.xlb.utils.ByteUtils;
import com.example.xlb.utils.LogUtils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;

import android_serialport_api.IDataDeal;
import android_serialport_api.ReceiveComUtils;
import android_serialport_api.SerialPort;
import tool_helper.crc;

public class SerialPortService extends Service {


//    /**************service 命令开始*********/

    static final byte[] CMD_READ_PLC = {0x01,0x03,0x00,0x01,0x00,0x0A};

    static final byte[] CMD_START_LIGHT =  {0x01,0x06,0x00,0x32,0x00,0x01};
    static final byte[] CMD_STOP_LIGHT  = {0x01,0x06,0x00,0x32,0x00,0x00};

    static final byte[] CMD_READ_GUI1 = {0X01,0x03,0x00,-125,0x00,0x10};
    static final byte[] CMD_READ_STATUS1={0x01,0x03,0x00,-105,0x00,0x06};

    static final byte[] CMD_READ_GUI2 = {0x01,0x03,0X00,-25,0x00,0x10};
    static final byte[] CMD_READ_STATUS2  = {0x01,0x03,0x00,-5,0x00,0x06};

    static final byte[] CMD_START_SERVICE = {0x01,0x00,0x20,0x00,0x01};
    static final byte[] CMD_STOP_SERVICE =  {0x01,0x00,0x20,0x00,0x00};



    static final byte[] CMD_STOP_MODE1  =  {0x01,0x06,0x00,0x46,0x00,0x00};
    static final byte[] CMD_START_MODE1 =  {0x01,0x06,0x00,0x46,0x00,0x01};
    static final byte[] CMD_JINJI_MODE1 =  {0x01,0x06,0x00,0x46,0x00,0x02};
    static final byte[] CMD_JIENENG_MODE1  ={0x01,0x06,0x00,0x46,0x00,0x03};

    static final byte[] CMD_SD_MODE1  =  {0x01,0x06,0x00,0x47,0x00,0x00};
    static final byte[] CMD_BZD_MODE1 =  {0x01,0x06,0x00,0x47,0x00,0x01};
    static final byte[] CMD_ZD_MODE1 =  {0x01,0x06,0x00,0x47,0x00,0x02};

    static final byte[] CMD_STOP_SET1  =  {0x01,0x06,0x00,0x48,0x00,0x00};
    static final byte[] CMD_UP_SET1 =  {0x01,0x06,0x00,0x48,0x00,0x01};
    static final byte[] CMD_DOWN_SET1 =  {0x01,0x06,0x00,0x48,0x00,0x02};

    static final byte[] CMD_FAULT_CLEAR1 =  {0x01,0x06,0x00,0x49,0x00,0x00};

    static final byte[] CMD_START_LIGHT1 = {0x01,0x06,0x00,-100,0x00,0x01};
    static final byte[] CMD_STOP_LIGHT1  =  {0x01,0x06,0x00,-100,0x00,0x00};

    static final byte[] CMD_LOCK_OFF1 = {0x01,0x06,0x00,0x4B,0x00,0x00};
    static final byte[] CMD_LOCK_ON1 =  {0x01,0x06,0x00,0x4B,0x00,0x01};


    static final byte[] CMD_LD_HLSET1  =  {0x01,0x06,0x00,0x4C,0x00,0x00};
    static final byte[] CMD_STOP_HLSET1 =  {0x01,0x06,0x00,0x4C,0x00,0x01};
    static final byte[] CMD_START_HLSET1 =  {0x01,0x06,0x00,0x4C,0x00,0x02};



    static final byte[] CMD_STOP_MODE2  =  {0x02,0x06,0x00,0x46,0x00,0x00};
    static final byte[] CMD_START_MODE2 =  {0x02,0x06,0x00,0x46,0x00,0x01};
    static final byte[] CMD_JINJI_MODE2 =  {0x02,0x06,0x00,0x46,0x00,0x02};
    static final byte[] CMD_JIENENG_MODE2  =  {0x02,0x06,0x00,0x46,0x00,0x03};

    static final byte[] CMD_SD_MODE2  =  {0x02,0x06,0x00,0x47,0x00,0x00};
    static final byte[] CMD_BZD_MODE2 =  {0x02,0x06,0x00,0x47,0x00,0x01};
    static final byte[] CMD_ZD_MODE2 =  {0x02,0x06,0x00,0x47,0x00,0x02};

    static final byte[] CMD_STOP_SET2  =  {0x02,0x06,0x00,0x48,0x00,0x00};
    static final byte[] CMD_UP_SET2 =  {0x02,0x06,0x00,0x48,0x00,0x01};
    static final byte[] CMD_DOWN_SET2 =  {0x02,0x06,0x00,0x48,0x00,0x02};

    static final byte[] CMD_FAULT_CLEAR2 =  {0x02,0x06,0x00,0x49,0x00,0x00};

    static final byte[] CMD_START_LIGHT2 = {0x01,0x06,0X00,0x00,0x00,0x01};
    static final byte[] CMD_STOP_LIGHT2  =  {0x01,0x06,0X00,0x00,0x00,0x00};

    static final byte[] CMD_LOCK_OFF2 = {0x02,0x06,0x00,0x4B,0x00,0x00};
    static final byte[] CMD_LOCK_ON2  =  {0x02,0x06,0x00,0x4B,0x00,0x01};


    static final byte[] CMD_LD_HLSET2  =  {0x02,0x06,0x00,0x4C,0x00,0x00};
    static final byte[] CMD_STOP_HLSET2 =  {0x02,0x06,0x00,0x4C,0x00,0x01};
    static final byte[] CMD_START_HLSET2 =  {0x02,0x06,0x00,0x4C,0x00,0x02};


    static final byte[] CMD_START_ZIWAI3 = {0x03,0x06,0x00,0x24,0x00,0x01};
    static final byte[] CMD_STOP_ZIWAI3  =  {0x03,0x06,0x00,0x24,0x00,0x00};

    static final byte[] CMD_START_ZIWAI4 = {0x03,0x06,0x00,0x25,0x00,0x01};
    static final byte[] CMD_STOP_ZIWAI4  =  {0x03,0x06,0x00,0x25,0x00,0x00};



    static final byte[] CMD_MODE_SEND_DATA_HEAD =  {0x03,0x00};
    static final byte[] CMD_MODE1_SEND_DATA_HEAD = {0x01,0x03,0x00};
    static final byte[] CMD_MODE2_SEND_DATA_HEAD = {0x02,0x03,0x00};
    static final byte[] CMD_MODE3_SEND_DATA_HEAD = {0x03,0x03,0x00};
    static final byte[] CMD_MODE4_SEND_DATA_HEAD = {0x04,0x03,0x00};
    static final byte[] CMD_MODE5_SEND_DATA_HEAD = {0x05,0x03,0x00};
    static final byte[] CMD_SEND_DATA_END = { 0x00,0x0A};
    static final byte[] CMD_SEND_DATA_END1 = { 0x00,0x0C};
    //static final byte[] CMD_SEND_DATA_ADDR = { 0x28,0x2d,0x32};  //寄存器地址
    static final byte[] CMD_SEND_DATA_ADDR = { 0x01,0x2d,0x32};  //寄存器地址
    public byte[] ADDRHEAD;  //数据头序号地址
    public byte[] ADDR;  //寄存器地址
    public byte[] SDATA;  // 要发送的数据
    public byte[] ADDR1={0x07,0x03,0x00,0x27,0x00,0x10};//050300280022445F   04030028001E459F  070300270010F46B

    public int  CMD_SHOW_STATUS =9;
    public int  CMD_SEND_FROM_TO =1;
    public int  MODEL_ADDR_INDEX=1;


    //  退出透传模式 没包括数据头地址
    static final byte[] CMD_SYSTEM_OUT ={0x06,0x00,0x1F,0x00,0x00};

    /**************service 命令结束*********/



    private android_serialport_api.SerialPort serialPort = null;
    private ReceiveComUtils utils = null;
    private Receive receive = new Receive();



    private static final String TAG = "xxxx";
    private boolean connecting = false;
    private Callback callback;
    public int data = 1;
    public int receivedData=1;
    public String messae="";



    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    class MyBinder extends Binder {


        SerialPortService getService() {
            return SerialPortService.this;
        }


        void TransferData(int mData) {
            data = mData;
        }


        int getData() {
            return receivedData;
        }


        void setData(int status) {
            CMD_SHOW_STATUS = status;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        connecting = true;
        open();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (connecting) {
                    if (callback != null) {
                        switch (CMD_SHOW_STATUS)
                        {
                            //开灯
                            case -2:
                                sendData(CMD_START_LIGHT);
                                CMD_SHOW_STATUS=0;
                                break;
                            case -1:
                                sendData(CMD_STOP_LIGHT);
                                CMD_SHOW_STATUS=0;
                                break;
                            case 0:
                                CMD_SHOW_STATUS=0;
                                sendData(CMD_READ_STATUS1);
                                SetConfig.PreStr="00";
                                SetConfig.ReadCount++;
                                break;

                            case 1:
                                CMD_SHOW_STATUS=2;
                                sendData(CMD_READ_GUI2);
                                break;
                            case 2:
                                if(SetConfig.FirstRead)
                                {
                                    CMD_SHOW_STATUS=3;
                                    SetConfig.FirstRead=false;
                                }
                               else if(SetConfig.ReadCount==3)
                                {
                                    SetConfig.FirstRead=false;
                                    SetConfig.ReadCount=0;
                                    CMD_SHOW_STATUS=3;
                                }
                                else
                                {
                                    CMD_SHOW_STATUS=0;
                                }
                                sendData(CMD_READ_GUI1);
                                SetConfig.PreStr="02";
                                break;
                            case 3:
                                CMD_SHOW_STATUS=4;
                                sendData(CMD_READ_STATUS1);
                                SetConfig.PreStr="03";
                                break;
                            case 4:
                                SetConfig.ReadStatus=true;
                                CMD_SHOW_STATUS=0;
                                SetConfig.PreStr="04";
                                sendData(CMD_READ_STATUS2);

                                break;
                            case 5:
                                CMD_SHOW_STATUS=2;
                               // messae="G1"+messae;
                                break;
                            case 6:
                                CMD_SHOW_STATUS=3;
                                break;
                            case 7:
                                CMD_SHOW_STATUS=4;
                                break;
                            case 8:
                                CMD_SHOW_STATUS=1;
                                break;

                            case 9:
                                sendData(CMD_STOP_MODE1);
                                CMD_SHOW_STATUS=0;
                                break;
                            case 10:
                                sendData(CMD_START_MODE1);
                                CMD_SHOW_STATUS=0;
                                break;
                            case 11:
                                sendData(CMD_JINJI_MODE1);
                                CMD_SHOW_STATUS=0;
                                break;
                            case 12:
                                sendData(CMD_JIENENG_MODE1);
                                CMD_SHOW_STATUS=0;
                                break;


                            case 13:
                                sendData(CMD_SD_MODE1);
                                CMD_SHOW_STATUS=0;
                                break;
                            case 14:
                                sendData(CMD_BZD_MODE1);
                                CMD_SHOW_STATUS=0;
                                break;
                            case 15:
                                sendData(CMD_ZD_MODE1);
                                CMD_SHOW_STATUS=0;
                                break;


                            case 16:
                                sendData(CMD_STOP_SET1);
                                CMD_SHOW_STATUS=0;
                                break;
                            case 17:
                                sendData(CMD_UP_SET1);
                                CMD_SHOW_STATUS=0;
                                break;
                            case 18:
                                sendData(CMD_DOWN_SET1);
                                CMD_SHOW_STATUS=0;
                                break;


                            case 19:
                                sendData(CMD_FAULT_CLEAR1);
                                CMD_SHOW_STATUS=0;
                                break;

                            case 20:
                                sendData(CMD_START_LIGHT1);
                                CMD_SHOW_STATUS=0;
                                break;
                            case 21:
                                sendData(CMD_STOP_LIGHT1);
                                CMD_SHOW_STATUS=0;
                                break;

                            case 22:
                                sendData(CMD_LOCK_OFF1);
                                CMD_SHOW_STATUS=0;
                                break;
                            case 23:
                                sendData(CMD_LOCK_ON1);
                                CMD_SHOW_STATUS=0;
                                break;

                            case 24:
                                sendData(CMD_LD_HLSET1);
                                CMD_SHOW_STATUS=0;
                                break;
                            case 25:
                                sendData(CMD_STOP_HLSET1);
                                CMD_SHOW_STATUS=0;
                                break;
                            case 26:
                                sendData(CMD_START_HLSET1);
                                CMD_SHOW_STATUS=0;
                                break;



                            case 29:
                                sendData(CMD_STOP_MODE2);
                                CMD_SHOW_STATUS=0;
                                break;
                            case 30:
                                sendData(CMD_START_MODE2);
                                CMD_SHOW_STATUS=0;
                                break;
                            case 31:
                                sendData(CMD_JINJI_MODE2);
                                CMD_SHOW_STATUS=0;
                                break;
                            case 32:
                                sendData(CMD_JIENENG_MODE2);
                                CMD_SHOW_STATUS=0;
                                break;

                            case 33:
                                sendData(CMD_SD_MODE2);
                                CMD_SHOW_STATUS=0;
                                break;
                            case 34:
                                sendData(CMD_BZD_MODE2);
                                CMD_SHOW_STATUS=0;
                                break;
                            case 35:
                                sendData(CMD_ZD_MODE2);
                                CMD_SHOW_STATUS=0;
                                break;


                            case 36:
                                sendData(CMD_STOP_SET2);
                                CMD_SHOW_STATUS=0;
                                break;
                            case 37:
                                sendData(CMD_UP_SET2);
                                CMD_SHOW_STATUS=0;
                                break;
                            case 38:
                                sendData(CMD_DOWN_SET2);
                                CMD_SHOW_STATUS=0;
                                break;


                            case 39:
                                sendData(CMD_FAULT_CLEAR2);
                                CMD_SHOW_STATUS=0;
                                break;

                            case 40:
                                sendData(CMD_START_LIGHT2);
                                CMD_SHOW_STATUS=0;
                                break;
                            case 41:
                                sendData(CMD_STOP_LIGHT2);
                                CMD_SHOW_STATUS=0;
                                break;

                            case 42:
                                sendData(CMD_LOCK_OFF2);
                                CMD_SHOW_STATUS=0;
                                break;
                            case 43:
                                sendData(CMD_LOCK_ON2);
                                CMD_SHOW_STATUS=0;
                                break;

                            case 44:
                                sendData(CMD_LD_HLSET2);
                                CMD_SHOW_STATUS=0;
                                break;
                            case 45:
                                sendData(CMD_STOP_HLSET2);
                                CMD_SHOW_STATUS=0;
                                break;
                            case 46:
                                sendData(CMD_START_HLSET2);
                                CMD_SHOW_STATUS=0;
                                break;
                            default:
                                CMD_SHOW_STATUS=0;
                                break;
                        }
                            callback.onDataChange(messae);
                    }
                }
            }
        }).start();


    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public Callback getCallback() {
        return callback;
    }

    public interface Callback {
        void onDataChange(String data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cloas();
        connecting = false;
    }


    private  void open(){
        try {
            serialPort = new SerialPort(new File("/dev/ttyS4"),
                    9600, 0);
            utils = new ReceiveComUtils(serialPort,receive);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Toast.makeText(this,"串口打开",Toast.LENGTH_SHORT).show();
    }

    private void stopKTService(){

        for(int i=2;i<=SetConfig.MODE_READ_COUNT.length;i++)
        {
            byte[] StopData=ByteUtils.byteMerger(ByteUtils.intToBytes(i,1),CMD_STOP_SERVICE);
            byte[] crcData=crc.makefcs(StopData);
            byte[] sengContent =ByteUtils.byteMerger(StopData,crcData);
            writeCom(serialPort,sengContent);
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        CMD_SHOW_STATUS=1;
        CMD_SEND_FROM_TO=1;

    }

    private void stopService(){

       for(int i=1;i<=SetConfig.MODE_READ_COUNT.length;i++)
       // for(int i=1;i<=1;i++)
        {
            byte[] StopData=ByteUtils.byteMerger(ByteUtils.intToBytes(i,1),CMD_STOP_SERVICE);
            byte[] crcData=crc.makefcs(StopData);
            byte[] sengContent =ByteUtils.byteMerger(StopData,crcData);
            writeCom(serialPort,sengContent);
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        CMD_SHOW_STATUS=1;
        CMD_SEND_FROM_TO=1;

    }


    private void startService(){

        for(int i=1;i<=SetConfig.MODE_READ_COUNT.length;i++)
        {
            byte[] StartData=ByteUtils.byteMerger(ByteUtils.intToBytes(i,1),CMD_START_SERVICE);
            byte[] crcData=crc.makefcs(StartData);
            byte[] sengContent =ByteUtils.byteMerger(StartData,crcData);
            writeCom(serialPort,sengContent);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        CMD_SHOW_STATUS=1;
        CMD_SEND_FROM_TO=1;
    }

    private void sendData(byte[] data){
        byte[] crcData=crc.makefcs(data);
        byte[] sengContent = ByteUtils.byteMerger(data,crcData);

        writeCom(serialPort,sengContent);
    }



    private void cloas(){
        closeCOM();
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


    public class Receive implements IDataDeal {

        @Override
        public synchronized void onDataReceived(byte[] buffer, int size) {
            byte[] back = new byte[size];
            System.arraycopy(buffer, 0, back, 0, size);
            String content =  ByteUtils.printHexString(back);
            messae =content;
        }
    }

    public void Admin_Setting_Out()
    {
        try{
            for(int i=1;i<=SetConfig.MODE_READ_COUNT.length;i++)
          {
            byte[] StopData=ByteUtils.byteMerger(ByteUtils.intToBytes(i,1),CMD_SYSTEM_OUT);
            byte[] crcData=crc.makefcs(StopData);
            byte[] sengContent =ByteUtils.byteMerger(StopData,crcData);
            writeCom(serialPort,sengContent);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
          }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

}
