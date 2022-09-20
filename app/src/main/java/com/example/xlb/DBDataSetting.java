package com.example.xlb;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.xlb.config.SetConfig;
import com.example.xlb.databinding.ActivityDbdataSettingBinding;
import com.example.xlb.utils.ByteUtils;
import com.example.xlb.utils.FunctionUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class DBDataSetting extends AppCompatActivity implements ServiceConnection {

    private ActivityDbdataSettingBinding acDbDataSettingBd;
    private Fragment f=null;
    private Fragment current=null;
    private Context context;
    private HashMap<Integer, String> hashMap=new HashMap<Integer, String>();

    String MODE="";
    String INDEXS="";
    int INDEX=0;
    String NATURAL_DATA="";

    // 模式一
    String ON_ANGLE="",OFF_ANGLE="";

    // 模式二 三
    String RANGE="",ADJUST="",UPPER_LIMIT="",LOWER_LIMIT="";

    // 模式变频器
    String FC_VALUE;

    // 模式四
    String ON_KT="",OFF_KT="",TEMP="",RH="";

    private int RECEIVE_DATA=0;

    private boolean bind = false;
    private boolean runStatus = true;
    private int TransforData;
    RWDBSerialPortService.MyBinder binder = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context=this;
        acDbDataSettingBd = ActivityDbdataSettingBinding.inflate(LayoutInflater.from(this));
        setContentView(acDbDataSettingBd.getRoot());


        acDbDataSettingBd.btSetting.setImageResource(R.drawable.xlb_setting);
        //acDbDataSettingBd.btPl.setImageResource(R.drawable.xlb_pl);
        acDbDataSettingBd.btKt.setImageResource(R.drawable.xlb_kt);
        acDbDataSettingBd.btRead.setImageResource(R.drawable.xlb_read);
        acDbDataSettingBd.btWrite.setImageResource(R.drawable.xlb_save_seri);
        acDbDataSettingBd.btSave.setImageResource(R.drawable.xlb_save);

        acDbDataSettingBd.btSetting.setOnClickListener(fr);
       // acDbDataSettingBd.btPl.setOnClickListener(fr);
        acDbDataSettingBd.btKt.setOnClickListener(fr);

        acDbDataSettingBd.btWrite.setOnClickListener(w);
        acDbDataSettingBd.btRead.setOnClickListener(fr);
        progressBarHide();



    }



    View.OnClickListener fr=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentManager fm=getSupportFragmentManager();
            FragmentTransaction ft=fm.beginTransaction();
            switch (v.getId())
            {
                case R.id.bt_read:
                    GetDataFrom_serialPort();
                    break;
                case R.id.bt_setting:
                    f=new DBData_Paramneter_Fragment();
                    ft.replace(R.id.fragment,f);
                    ft.commit();
                    break;
                case R.id.bt_kt:
                    f=new DBDataKT_Paramneter_Fragment();
                    ft.replace(R.id.fragment,f);
                    ft.commit();
                    break;
//                case R.id.bt_pl:
//                    f=new DBDataPL_Paramneter_Fragment();
//                    break;
                default:
                    f=new DBData_Paramneter_Fragment();
                    ft.replace(R.id.fragment,f);
                    ft.commit();
                    break;
            }


        }
    };


    private void  GetDataFrom_serialPort()
    {
        current =getSupportFragmentManager().findFragmentById(R.id.fragment);
        // hashMap.clear();
        if(current!=null){
            if(current  instanceof DBData_Paramneter_Fragment)
            {
                if(((DBData_Paramneter_Fragment) current).type.equals("EDV"))
                {
                    binder.setData(1,Integer.parseInt(((DBData_Paramneter_Fragment) current).model_index));
                    //FunctionUtils.showTextToast(DBDataSetting.this,((DBData_Paramneter_Fragment) current).model_index);
                    SetConfig.ALL_MODE_ADAPTER=true;
                }
                else {
                    binder.setData(2,Integer.parseInt(((DBData_Paramneter_Fragment) current).model_index));
                    //FunctionUtils.showTextToast(DBDataSetting.this,((DBData_Paramneter_Fragment) current).model_index);
                    SetConfig.ALL_MODE_ADAPTER=true;
                }
                FunctionUtils.showTextToast(DBDataSetting.this,"数据读取中，请稍后");
                progressBarShow();
            }

            else  if(current  instanceof DBDataPL_Paramneter_Fragment)
            {
                binder.setData(9,Integer.parseInt(((DBDataPL_Paramneter_Fragment) current).model_index));
                SetConfig.ALL_MODE_ADAPTER=true;
                progressBarShow();
            }

        }
    }



    View.OnClickListener w=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            current =getSupportFragmentManager().findFragmentById(R.id.fragment);
            if(current  instanceof DBData_Paramneter_Fragment || current  instanceof DBDataPL_Paramneter_Fragment) {
                if (hashMap == null || hashMap.size() < 1) {
                    FunctionUtils.showTextToast(DBDataSetting.this, "请先读取设备参数!");
                    return;
                }
            }
            if(current!=null){
                if(current  instanceof DBData_Paramneter_Fragment)
                {
                    binder.setData(8,hashMap,((DBData_Paramneter_Fragment) current).type);
                    progressBarShow();
                }
                else if(current  instanceof DBDataPL_Paramneter_Fragment)
                {
                    binder.setData(12,hashMap,Integer.parseInt(((DBDataPL_Paramneter_Fragment) current).model_index));
                    progressBarShow();
                }
                else if(current  instanceof DBDataKT_Paramneter_Fragment)
                {
                    String ktv=((DBDataKT_Paramneter_Fragment) current).ed1.getText()+"!"+((DBDataKT_Paramneter_Fragment) current).ed2.getText();
                    binder.setData(13,ktv);
                    progressBarShow();
                }
            }

        }
    };


    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        //获取binder对象
        binder = (RWDBSerialPortService.MyBinder) service;

        //向Service传递数据 TransforData
        binder.TransferData(TransforData);
        //获取从Service传递的MyService对象
        RWDBSerialPortService myService3 = binder.getService();
        //接口回调 监控Service中的数据变化 并在handler中更新UI
        myService3.setCallback(new RWDBSerialPortService.Callback() {
            @Override
            public void onDataChange(String data) {
                Message msg = new Message();
                msg.obj = data;
                handler.sendMessage(msg);
            }
        });
        //acDbDataSettingBd.btRead.performClick();
       // activityDataSettingBinding.btSetting1.callOnClick();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    @Override
    protected void onStop() {
        super.onStop();
        stopService();
    }

    @Override
    protected void onStart() {
        super.onStart();
        init();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            AnalysisData(msg.obj.toString());
        }
    };


    static Handler handlerTimer=new Handler();
    Runnable runnable =new Runnable() {
        @Override
        public void run() {
            FunctionUtils.showTextToast(DBDataSetting.this,"数据读取超时!");
            //handlerTimer.postDelayed(this, 2000);
        }
    };

    private void AnalysisData(String data)
    {
        current =getSupportFragmentManager().findFragmentById(R.id.fragment);
        if(data.equals("0700000"))
        {
            progressBarHide();
        }

        if(data.length()==14)//--0106001f001ff9c4
        {
            if(data.substring(0,2).equals("10"))
            {
                progressBarHide();
                FunctionUtils.showTextToast(DBDataSetting.this,"数据保存成功");
                binder.setStop();
               // acDbDataSettingBd.btRead.callOnClick();
            }
        }
        if(data.equals("0106001f001ff9c4"))
        {
            acDbDataSettingBd.btRead.performClick();
        }
        if(data.length()==16)
        {
            if(data.substring(2,4).equals("10"))
            {
                progressBarHide();
                FunctionUtils.showTextToast(DBDataSetting.this,"数据保存成功");
                binder.setStop();
               // if(((DBData_Paramneter_Fragment) current).type.equals("EDV"))
               // acDbDataSettingBd.btRead.callOnClick();
            }
        }
        if(data.length()<17)
            return;

        MODE=data.substring(6,8); //data.substring(0,2);
        INDEXS=data.substring(6,8);
        INDEX=Integer.parseInt(ByteUtils.hexToDec(INDEXS));
        if(data.length()!=50)
        {
            int setp=Integer.parseInt(ByteUtils.hexToDec(MODE));
            if(SetConfig.EDV_START_INDEX<=setp & setp<=SetConfig.EDV_END_INDEX)
            {  // EDV设备解析
                NATURAL_DATA=data.substring(6,data.length()-4);
                ON_ANGLE=ByteUtils.hexToDec(data.substring(14,16));
                OFF_ANGLE=ByteUtils.hexToDec(data.substring(16,18));
                hashMap.put(INDEX-SetConfig.EDV_START_INDEX,"V1:"+ON_ANGLE+"|V2:"+OFF_ANGLE+"|VS:"+NATURAL_DATA);
                if(setp== SetConfig.EDV_END_INDEX&&SetConfig.ALL_MODE_ADAPTER)
                {
                    SetConfig.ALL_MODE_ADAPTER=false;
                    binder.setStop();
                    if(current!=null){
                        if(current  instanceof DBData_Paramneter_Fragment)
                        {
                            ((DBData_Paramneter_Fragment) current).SetAdapter(hashMap);
                        }
                    }
                    progressBarHide();

                }
                // 超时判断
                RECEIVE_DATA=binder.getData();
                SetConfig.SYN_COUNT++;
                if(SetConfig.SYN_COUNT>5) {
                    if (RECEIVE_DATA == SetConfig.SYN) {
                        handlerTimer.postDelayed(runnable, 2000);
                        binder.setStop();
                        // setFragmentAdapter(1);
                        // SetConfig.MODE1_ADAPTER=false;
                        progressBarHide();
                    }
                    else {
                        handlerTimer.removeCallbacks(runnable);
                    }
                    SetConfig.SYN=RECEIVE_DATA;
                    SetConfig.SYN_COUNT=0;
                }
                // 超时判断
            }
            else if(setp<=SetConfig.VAV_END_INDEX)  // VAV设备解析
            {
                NATURAL_DATA=data.substring(6,data.length()-4);
                RANGE=data.substring(10,12);
                ADJUST=ByteUtils.hexToDec(data.substring(12,14));
                UPPER_LIMIT=ByteUtils.hexToDec(data.substring(14,16));
                LOWER_LIMIT=ByteUtils.hexToDec(data.substring(16,18));
                RANGE=RANGE.equals("01")?"false":"true";
                //030312 0a0a  03 14 0a05 0a00 0101 18e2
                hashMap.put(INDEX-1,"V1:"+ADJUST+"|V2:"+UPPER_LIMIT+"|V3:"+LOWER_LIMIT+"|V4:"+RANGE+"|VS:"+NATURAL_DATA);
                if(INDEX== SetConfig.VAV_END_INDEX &&SetConfig.ALL_MODE_ADAPTER)
                {
                    SetConfig.ALL_MODE_ADAPTER=false;
                    binder.setStop();
                    // setFragmentAdapter(1);
                    if(current!=null){
                        if(current  instanceof DBData_Paramneter_Fragment)
                        {
                            ((DBData_Paramneter_Fragment) current).SetAdapter(hashMap);
                        }
                    }
                    progressBarHide();
                }
                // 超时判断
                RECEIVE_DATA=binder.getData();
                SetConfig.SYN_COUNT++;
                if(SetConfig.SYN_COUNT>5) {
                    if (RECEIVE_DATA == SetConfig.SYN) {
                        handlerTimer.postDelayed(runnable, 2000);
                        binder.setStop();
                        // setFragmentAdapter(3);
                        progressBarHide();
                    }
                    else {
                        handlerTimer.removeCallbacks(runnable);
                    }
                    SetConfig.SYN=RECEIVE_DATA;
                    SetConfig.SYN_COUNT=0;
                }
                // 超时判断
            }
        }
        else
        {
            //频率读取解析
            int setp=Integer.parseInt(MODE);
            NATURAL_DATA=data.substring(6,data.length()-4);
            for(int i=0;i<SetConfig.MODE_SET_COUNT_ARR[setp-1];i++)
            {
                binder.setStop();
                FC_VALUE=ByteUtils.hexToDec(NATURAL_DATA.substring(2*i,2*i+2));
                hashMap.put(i,FC_VALUE+"|"+NATURAL_DATA);
            }
            if(current!=null){
                if(current  instanceof DBDataPL_Paramneter_Fragment)
                {
                    ((DBDataPL_Paramneter_Fragment) current).SetAdapter(hashMap);
                }
            }
            progressBarHide();

        }
    }

    private void init() {
        if (!bind) {
            bind = true;
            Intent bindIntent = new Intent(DBDataSetting.this, RWDBSerialPortService.class);
            bindService(bindIntent, DBDataSetting.this, BIND_AUTO_CREATE);
            //Toast.makeText(DataSetting.this, "Bind Service Success!", Toast.LENGTH_SHORT).show();
        }

    }

    private void stopService()
    {
        if (bind) {
            unbindService(DBDataSetting.this);
            bind = false;
        }
    }


    private void progressBarHide()
    {
        acDbDataSettingBd.progressBar1.setVisibility(View.GONE);
    }

    private void progressBarShow()
    {
        acDbDataSettingBd.progressBar1.setVisibility(View.VISIBLE);
    }

}

