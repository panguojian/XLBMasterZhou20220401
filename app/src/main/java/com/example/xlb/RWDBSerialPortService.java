package com.example.xlb;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.example.xlb.config.SetConfig;
import com.example.xlb.utils.ByteUtils;
import com.example.xlb.utils.LogUtils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import android_serialport_api.IDataDeal;
import android_serialport_api.ReceiveComUtils;
import android_serialport_api.SerialPort;
import tool_helper.crc;

public class RWDBSerialPortService extends Service {


    //    /**************service 命令*********/


    public int  CMD_SET_STATUS =5;//1、2 读取     5 透传模式，6退出

    //  D 退出
    static final byte[] CMD_SYSTEM_OUT ={0x06,0x00,0x1F,0x00,0x00};

    //透传模式A  进入
    static final byte[] CMD_SYSTEM_MODEL ={0x06,0x00,0x1F,0x00,0x1F};

    // B 读取
    static final byte[]  CMD_SYSTEM_READ_HEAD ={0x01,0x03,0x01,0x00,0x00,0x05};
    static final byte[]  CMD_SYSTEM_READ_END ={0x00,0x00,0x05};
    static final byte[]  CMD_SYSTEM_WRITE ={ 0x01,0x03,0x01,0x00,0x00,0x05};
    //  C 设置
    public int ORDERY_INDEX=1;
    public String EDV_VAV_TYPE="";

    private HashMap<Integer, String> hashMap=new HashMap<Integer, String>();


    static final byte[] CMD_MODE_SEND_DATA_HEAD = {0x03};
    static final byte[] CMD_SEND_DATA_END_EDV = { 0x00,0x00,0x05};
    static final byte[] CMD_SEND_DATA_END_VAV = { 0x00,0x00,0x09};

    public byte[] INDEX;//要读取的设备序号
    public byte[] MODE_INDEX;
    public byte[] SDATA;  // 要发送的数据
    public int  CMD_SHOW_STATUS =1;//1 模块1读取，2 模块2读取。。。。。。，
    public int  CMD_SEND_FROM_TO =1;//第几次读取  不能超过下面的次数


    public byte[] HEAD_MODE;
    public byte[] HEAD_EDV_VAV;
    public byte[] HEAD;


    static final byte[] CMD_MODE_SETTING_DATA_HEAD = {0x10}; //{0X01,0x10,13};
    static final byte[] CMD_SETTING_DATA_END =        {0x00,0x00,0x05,0x0A};
    static final byte[] CMD_SETTING_DATA_END_EDV =        {0x00,0x00,0x05,0X0A};
    static final byte[] CMD_SETTING_DATA_END_VAV =        {0x00,0x00,0x09,18};

    //同步数据发送
    public byte[] SEND_SETTING_DATA={0x00};
    public String SEND_DATA_TAG="";
    public String RECEIVE_DATA_TAG="";



    //变频器
    static final byte[] CMD_FREQUENCY_READ1 = {0x03,0X00,0X0A,0X00,0X0A};
    static final byte[] CMD_FREQUENCY_READ2 = {0x03,0X00,0X14,0X00,0X04};
    static final byte[] CMD_FREQUENCY1_READ1 = {0X01,0x03,0X00,0X0A,0X00,0X0A};
    static final byte[] CMD_FREQUENCY1_READ2 ={0X01,0x03,0X00,0X14,0X00,0X04};
    static final byte[] CMD_FREQUENCY2_READ1 = {0X02,0x03,0X00,0X0A,0X00,0X0A};
    static final byte[] CMD_FREQUENCY2_READ2 ={0X02,0x03,0X00,0X14,0X00,0X04};
    static final byte[] CMD_FREQUENCY3_READ1 = {0X03,0x03,0X00,0X0A,0X00,0X0A};
    static final byte[] CMD_FREQUENCY3_READ2 ={0X03,0x03,0X00,0X14,0X00,0X04};
    static final byte[] CMD_FREQUENCY5_READ1 = {0X05,0x03,0X00,0X0A,0X00,0X0A};
    static final byte[] CMD_FREQUENCY5_READ2 ={0X05,0x03,0X00,0X14,0X00,0X04};

    static final byte[] CMD_FREQUENCY1_SETTING_HEAD = {0X01,0x03,0X00,0X0A,0X00,0X0A};
    static final byte[] CMD_FREQUENCY2_SETTING_HEAD = {0X02,0x03,0X00,0X14,0X00,0X04};
    static final byte[] CMD_FREQUENCY3_SETTING_HEAD = {0X03,0x03,0X00,0X14,0X00,0X04};

    static  String FREQUENCY_VALUE="";

    static final byte[] CMD_FREQUENCY_SETTING_HEAD = {0x10,0X00,0X0A,0X00};
    static final byte[] FREQUENCY_INDEX = {0X0A,0x14,0X02,0x04};

    static final byte[] KT_TP_RH = {0X02,0x10,0X00,0X02,0X00,0X01,0X02};  //空调温湿度设置
    String KT_CRH_VALUE;


    // 待发送数据


    // 模式一
    String ON_ANGLE="",OFF_ANGLE="";

    // 模式二 三
    String RANGE="",ADJUST="",UPPER_LIMIT="",LOWER_LIMIT="";

    // 模式四
    String ON_KT="",OFF_KT="",TEMP="",RH="";

    int EMPTY=0;





    private android_serialport_api.SerialPort serialPort = null;
    private ReceiveComUtils utils = null;
    private RWDBSerialPortService.Receive receive = new RWDBSerialPortService.Receive();
    /**************service 命令*********/

    private static final String TAG = "xxxx";
    private boolean connecting = false;
    private RWDBSerialPortService.Callback callback;
    public int data = 1;
    public int receivedData=1;
    public String messae="";

    @Override
    public IBinder onBind(Intent intent) {
        return new RWDBSerialPortService.MyBinder();
    }

    class MyBinder extends Binder {

        RWDBSerialPortService getService() {
            return RWDBSerialPortService.this;
        }

        void TransferData(int mData) {
            data = mData;
        }

        int getData() {
            return receivedData;
        }

        void setData(int status) {
            if(status==9 ||status==10 ||status==11||status==13)
            {
                CMD_SET_STATUS=status;
            }
            else
                CMD_SET_STATUS=5;
            CMD_SHOW_STATUS = status;
            CMD_SEND_FROM_TO=1;
        }

        void setData(int status,int mode_index) {
//            if(status==9||status==10 )
//            {  //变频器读取
//                CMD_SET_STATUS=status;
//            }
//            else
            CMD_SET_STATUS=status;
            CMD_SHOW_STATUS = status;
            CMD_SEND_FROM_TO=1;
        }

        void setData(int status,String KT_CRH_Value) {  //空调温湿度设置
            CMD_SET_STATUS=status;
            KT_CRH_VALUE=KT_CRH_Value;
        }

        void setData(int status,HashMap<Integer, String> settingData,int index) {
            CMD_SET_STATUS=status;
            CMD_SHOW_STATUS=index;
            hashMap=settingData;
            CMD_SEND_FROM_TO=1;
        }

        void setData(int status,HashMap<Integer, String> settingData,String Type) {
            CMD_SET_STATUS=status;
            CMD_SHOW_STATUS=1;  //地址开始
            hashMap=settingData;
            EDV_VAV_TYPE=Type;
            if(Type.equals("VAV")) {
                CMD_SEND_FROM_TO = 1;
            }
            else if(Type.equals("EDV")) {
                CMD_SEND_FROM_TO = SetConfig.EDV_START_INDEX;
            }
        }

        void setStop()
        {
            messae="0700000";
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        connecting = true;
        open();
        //开启一个线程，对数据进行处理
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (connecting) {
                    if (callback != null) {
                        switch (CMD_SET_STATUS)
                        {
                            case 1:  // EDV设备读取
                                if(CMD_SEND_FROM_TO==1)
                                    CMD_SEND_FROM_TO=SetConfig.EDV_START_INDEX;
                                INDEX = ByteUtils.intToBytes(CMD_SEND_FROM_TO, 1);
                                MODE_INDEX=ByteUtils.intToBytes(CMD_SHOW_STATUS, 1);
                                SDATA = ByteUtils.byteMerger(MODE_INDEX,CMD_MODE_SEND_DATA_HEAD, INDEX, CMD_SEND_DATA_END_EDV);
                                sendData(SDATA);
                                if (CMD_SEND_FROM_TO == SetConfig.EDV_END_INDEX) {
                                    CMD_SET_STATUS=7;
                                    CMD_SEND_FROM_TO=1;
                                }
                                else
                                    CMD_SEND_FROM_TO++;
                                break;
                            case 2:  //  VAV设备读取
                                CMD_SHOW_STATUS=1;
                                INDEX = ByteUtils.intToBytes(CMD_SEND_FROM_TO, 1);
                                MODE_INDEX=ByteUtils.intToBytes(CMD_SHOW_STATUS, 1);
                                SDATA = ByteUtils.byteMerger(MODE_INDEX,CMD_MODE_SEND_DATA_HEAD, INDEX, CMD_SEND_DATA_END_VAV);
                                sendData(SDATA);
                                if (CMD_SEND_FROM_TO == SetConfig.VAV_END_INDEX) {
                                    CMD_SET_STATUS=7;
                                    CMD_SEND_FROM_TO=1;
                                } else {
                                    CMD_SEND_FROM_TO++;
                                }
                                break;
                            case 5://进入透传模式
                                    sendData(ByteUtils.byteMerger(ByteUtils.intToBytes(CMD_SHOW_STATUS, 1), CMD_SYSTEM_MODEL));
                                     messae="0106001f001ff9c4";
                                    CMD_SET_STATUS=7;
                                break;
                            case 6:
                                sendData(ByteUtils.byteMerger(ByteUtils.intToBytes(CMD_SHOW_STATUS,1),CMD_SYSTEM_OUT));//退出透传模式
                                CMD_SET_STATUS=7;
                                break;
                            case 7:
                                CMD_SEND_FROM_TO=1;
                                break;
                            case 8:  //设置参数
                                if(hashMap.size()>0) {
                                    String SETTING_DATA="";
                                    SEND_DATA_TAG=ORDERY_INDEX+"";
                                    RECEIVE_DATA_TAG="";

                                    HEAD_MODE=ByteUtils.intToBytes(CMD_SHOW_STATUS,1);
                                    HEAD_EDV_VAV=ByteUtils.intToBytes(CMD_SEND_FROM_TO,1); //

                                    SETTING_DATA = CheckMPData(CMD_SEND_FROM_TO - 1, CMD_SHOW_STATUS);
                                    if(SETTING_DATA.length()!=1)
                                    {
                                        if (EDV_VAV_TYPE.equals("EDV")) {
                                            HEAD = ByteUtils.byteMerger(HEAD_MODE, CMD_MODE_SETTING_DATA_HEAD, HEAD_EDV_VAV, CMD_SETTING_DATA_END_EDV);
                                        } else {
                                            HEAD = ByteUtils.byteMerger(HEAD_MODE, CMD_MODE_SETTING_DATA_HEAD, HEAD_EDV_VAV, CMD_SETTING_DATA_END_VAV);
                                        }
                                        SEND_SETTING_DATA = ByteUtils.toBytes(SETTING_DATA);
                                        byte[] HLPER_DATA = ByteUtils.byteMerger(HEAD, SEND_SETTING_DATA);
                                        sendData(HLPER_DATA);
                                    }
                                    else
                                    {
                                        EMPTY++;  //临时这样写  后面要修改
                                    }
                                    if(EMPTY==13)
                                    {
                                        messae="01100000000000";
                                        EMPTY=0;
                                    }
                                    int temp=EDV_VAV_TYPE.equals("VAV")?SetConfig.VAV_END_INDEX:SetConfig.EDV_END_INDEX;
                                    if (CMD_SEND_FROM_TO == temp) {
                                        CMD_SET_STATUS=7;
                                        CMD_SEND_FROM_TO=1;
                                    } else {
                                        CMD_SEND_FROM_TO++;
                                    }

                                }
                                break;
                            case 9:  //变频器读取
                                MODE_INDEX=ByteUtils.intToBytes(CMD_SHOW_STATUS, 1);
                                SDATA =CMD_SEND_FROM_TO==1?CMD_FREQUENCY_READ1:CMD_FREQUENCY_READ2;
                                SDATA=ByteUtils.byteMerger(MODE_INDEX,SDATA);
                                if (CMD_SEND_FROM_TO == SetConfig.FREQUENCY_READ_SETTING_COUNT[CMD_SHOW_STATUS-1]) {
                                    CMD_SET_STATUS=7;
                                    CMD_SEND_FROM_TO=1;
                                } else {
                                    CMD_SEND_FROM_TO++;
                                }
                                sendData(SDATA);
                                break;
                            case 12:  //变频器设置取
                                if(hashMap.size()>0) {
                                    String SETTING_DATA= Check_FREMPData(CMD_SEND_FROM_TO-1,CMD_SHOW_STATUS);
                                    SEND_DATA_TAG=ORDERY_INDEX+"";
                                    RECEIVE_DATA_TAG="";
                                    byte[] HEAD_MODE=ByteUtils.intToBytes(CMD_SHOW_STATUS,1);
                                    byte[] HEAD_DEV=ByteUtils.subBytes(FREQUENCY_INDEX,(CMD_SEND_FROM_TO-1)*2,2);
                                    byte[] HEAD=ByteUtils.byteMerger(HEAD_MODE,CMD_FREQUENCY_SETTING_HEAD,HEAD_DEV);

                                    SEND_SETTING_DATA=ByteUtils.toBytes(SETTING_DATA);
                                    byte[] HLPER_DATA=ByteUtils.byteMerger(HEAD,SEND_SETTING_DATA);

                                    if (CMD_SEND_FROM_TO == SetConfig.FREQUENCY1_READ_SETTING_COUNT) {//hashMap.size()
                                        CMD_SET_STATUS=7;
                                        CMD_SEND_FROM_TO=1;
                                    } else {
                                        CMD_SEND_FROM_TO++;
                                    }
                                    sendData(HLPER_DATA);
                                }
                            case 13:
                                int tp=Integer.parseInt(KT_CRH_VALUE.split("!")[0]);
                                int rh=Integer.parseInt(KT_CRH_VALUE.split("!")[1]);
                                byte[] HEAD_MODE=ByteUtils.intToBytes(tp,1);
                                byte[] HEAD_DEV=ByteUtils.intToBytes(rh,1);
                                byte[] SDATA=ByteUtils.byteMerger(KT_TP_RH,HEAD_MODE,HEAD_DEV);
                                sendData(SDATA);
                                CMD_SET_STATUS=7;
                            default:
                                break;
                        }
                        callback.onDataChange(messae);
                        messae="";
                        data++;
                    }
                    try {
                        if(CMD_SET_STATUS==7)
                            Thread.sleep(2000);
                        else
                            Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();


    }

    public void setCallback(RWDBSerialPortService.Callback callback) {
        this.callback = callback;
    }

    public RWDBSerialPortService.Callback getCallback() {
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

    /**
     * 打开串口
     */
    private  void open(){
        try {
            serialPort = new SerialPort(new File("/dev/ttyS3"),
                    9600, 0);
            utils = new ReceiveComUtils(serialPort,receive);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Toast.makeText(this,"串口打开",Toast.LENGTH_SHORT).show();
    }


    private void sendData(byte[] sendData){
        byte[] crcData= crc.makefcs(sendData);
        byte[] sengContent = ByteUtils.byteMerger(sendData,crcData);
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

    // 卡串口接收数据
    public class Receive implements IDataDeal {

        @Override
        public synchronized void onDataReceived(byte[] buffer, int size) {
            byte[] back = new byte[size];
            System.arraycopy(buffer, 0, back, 0, size);
            String content =  ByteUtils.printHexString(back);
            // LogUtils.Logs_i("串口返回=="+ content);
            // byte[] bt=ByteUtils.toBytes(content);
            messae =content;
            receivedData++;
//            if(CMD_SET_STATUS==8)
//            {
//                if(content.length()>6)
//                RECEIVE_DATA_TAG=content.substring(4,6);
//            }
        }
    }


    public String CheckMPData(int i,int setting_mode_index)
    {
        String hashMPSTR="";
        String[] hashMP;
        if (hashMap.size() < 1) {
            return "";
        }
       // if(setting_mode_index<=SetConfig.EDV_COUNT)  // EDV设备设置
        try {
            if (EDV_VAV_TYPE.equals("EDV")) {
                i = i - SetConfig.VAV_END_INDEX;
                hashMP = hashMap.get(i).split("\\|");
                if (hashMP.length != 3) {
                    return "";
                }
                ON_ANGLE = hashMP[0].split(":")[1];
                ON_ANGLE = ON_ANGLE.length() == 1 ? "0" + ON_ANGLE : ON_ANGLE;
                ON_ANGLE = ByteUtils.decToHex(ON_ANGLE);
                ON_ANGLE = ON_ANGLE.length() == 1 ? "0" + ON_ANGLE : ON_ANGLE;

                OFF_ANGLE = hashMP[1].split(":")[1];
                OFF_ANGLE = OFF_ANGLE.length() == 1 ? "0" + OFF_ANGLE : OFF_ANGLE;
                OFF_ANGLE = ByteUtils.decToHex(OFF_ANGLE);
                OFF_ANGLE = OFF_ANGLE.length() == 1 ? "0" + OFF_ANGLE : OFF_ANGLE;
                hashMPSTR = hashMP[2].split(":")[1];
                if (hashMPSTR.substring(8, 12).equals(ON_ANGLE + OFF_ANGLE)) {
                    hashMPSTR = "0";
                } else {
                    hashMPSTR = hashMPSTR.substring(0, 8) + ON_ANGLE + OFF_ANGLE + hashMPSTR.substring(12, 20);
                }
            } else   // VAV设备设置
            {
                hashMP = hashMap.get(i).split("\\|");
                if (hashMP.length != 5) {
                    return "";
                }
                ADJUST = hashMP[0].split(":")[1];
                ADJUST = ADJUST.length() == 1 ? "0" + ADJUST : ADJUST;
                ADJUST = ByteUtils.decToHex(ADJUST);
                ADJUST = ADJUST.length() == 1 ? "0" + ADJUST : ADJUST;

                UPPER_LIMIT = hashMP[1].split(":")[1];
                UPPER_LIMIT = UPPER_LIMIT.length() == 1 ? "0" + UPPER_LIMIT : UPPER_LIMIT;
                UPPER_LIMIT = ByteUtils.decToHex(UPPER_LIMIT);
                UPPER_LIMIT = UPPER_LIMIT.length() == 1 ? "0" + UPPER_LIMIT : UPPER_LIMIT;

                LOWER_LIMIT = hashMP[2].split(":")[1];
                LOWER_LIMIT = LOWER_LIMIT.length() == 1 ? "0" + LOWER_LIMIT : LOWER_LIMIT;
                LOWER_LIMIT = ByteUtils.decToHex(LOWER_LIMIT);
                LOWER_LIMIT = LOWER_LIMIT.length() == 1 ? "0" + LOWER_LIMIT : LOWER_LIMIT;

                RANGE = hashMP[3].split(":")[1];
                RANGE = RANGE.equals("false") ? "01" : "00";
                RANGE = ByteUtils.decToHex(RANGE);
                RANGE = RANGE.length() == 1 ? "0" + RANGE : RANGE;

                hashMPSTR = hashMP[4].split(":")[1];
                hashMPSTR = hashMPSTR.substring(0, 4) + RANGE + ADJUST + UPPER_LIMIT + LOWER_LIMIT + hashMPSTR.substring(12);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return hashMPSTR;
    }

    public String Check_FREMPData(int i,int setting_mode_index)
    {
        if (hashMap.size() < 1) return "";
        String hashMPSTR="",tempSTR="";
        String[] hashMP;
        int SETTING_ROOM_COUNT=0;
        FREQUENCY_VALUE="";
        switch (setting_mode_index)
        {
            case 1:
                SETTING_ROOM_COUNT=SetConfig.MODE1_SET_COUNT;
                break;
            case 2:
                SETTING_ROOM_COUNT=SetConfig.MODE2_SET_COUNT;
                break;
            case 3:
                SETTING_ROOM_COUNT=SetConfig.MODE3_SET_COUNT;
                break;
            case 4:
                SETTING_ROOM_COUNT=SetConfig.MODE4_SET_COUNT;
                break;
            case 5:
                SETTING_ROOM_COUNT=SetConfig.MODE5_SET_COUNT;
                break;
        }
        //SETTING_ROOM_COUNT=setting_mode_index==1?SetConfig.MODE1_SET_COUNT:(setting_mode_index==2?SetConfig.MODE2_SET_COUNT:SetConfig.MODE3_SET_COUNT);
        if(i==0)  //第一次设置
        {
            SETTING_ROOM_COUNT=SETTING_ROOM_COUNT>20?20:SETTING_ROOM_COUNT;
        }
        else {  //第二次设置
            SETTING_ROOM_COUNT=SETTING_ROOM_COUNT-20;
        }

        for(int j=20*i;j<SETTING_ROOM_COUNT;j++)
        {
            tempSTR=hashMap.get(j).split("\\|")[0];
            tempSTR = tempSTR.length() == 1 ? "0" + tempSTR : tempSTR;
            tempSTR = ByteUtils.decToHex(tempSTR);
            tempSTR = tempSTR.length() == 1 ? "0" + tempSTR : tempSTR;
            FREQUENCY_VALUE+=tempSTR;
            if(hashMap.get(j).split("\\|").length==2)
            {
                hashMPSTR = hashMap.get(j).split("\\|")[1];
            }
        }

        hashMPSTR = FREQUENCY_VALUE + hashMPSTR.substring(FREQUENCY_VALUE.length());
        return hashMPSTR;
    }


}
