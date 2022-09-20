package com.example.xlb;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ServiceActivity2 extends AppCompatActivity implements View.OnClickListener, ServiceConnection {

    private static final String TAG = "xxxx";
    private TextView textView;
    private boolean bind = false;
    private int TransforData;
    SerialPortService.MyBinder binder = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service2);

        TransforData = 0;
        textView = (TextView) findViewById(R.id.textView);
        Button bindBtn = (Button) findViewById(R.id.bind_service2);
        Button unBindBtn = (Button) findViewById(R.id.unbind_service2);
        Button clearBt = (Button) findViewById(R.id.clear);
        bindBtn.setOnClickListener(this);
        unBindBtn.setOnClickListener(this);
        clearBt.setOnClickListener(this);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        //获取binder对象
        binder = (SerialPortService.MyBinder) service;

        //向Service传递数据 TransforData
        binder.TransferData(TransforData);
        //获取从Service传递的MyService对象
        SerialPortService myService2 = binder.getService();
        //接口回调 监控Service中的数据变化 并在handler中更新UI
        myService2.setCallback(new SerialPortService.Callback() {
            @Override
            public void onDataChange(String data) {
                Message msg = new Message();
                msg.obj = data;
                handler.sendMessage(msg);
            }
        });
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bind_service2:
                if (!bind) {
                    bind = true;
                    //服务绑定后，会调用 onServiceConnected
                    Intent bindIntent = new Intent(this, SerialPortService.class);
                    bindService(bindIntent, this, BIND_AUTO_CREATE);
                    Toast.makeText(this, "Bind Service Success!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.unbind_service2:
                if (bind) {
                    //从Service中获取data数值
                    TransforData = binder.getData();
                    unbindService(this);
                    bind = false;
                    Toast.makeText(this, "unBind Service Success!", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.clear:
                if (!bind) {
                    TransforData = 0;
                    textView.setText("0");
                }
                break;
            default:
                break;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //在此处更新UI
            textView.setText(msg.obj.toString());
        }
    };
}
