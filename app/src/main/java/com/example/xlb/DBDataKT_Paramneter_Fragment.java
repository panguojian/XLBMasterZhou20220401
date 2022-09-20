package com.example.xlb;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.HashMap;


public class DBDataKT_Paramneter_Fragment extends Fragment {

    private ListView lv;
    private View view;
    public EditText ed1,ed2,ed3;  //  温度 湿度
    public  String KTValue;


    public DBDataKT_Paramneter_Fragment() {

    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_datasetting_kt,container,false);
        ed1=(EditText) view.findViewById(R.id.ED01);
        ed2=(EditText) view.findViewById(R.id.ED02);
        ed3=(EditText) view.findViewById(R.id.ED03);
        return  view;
    }

    public void SetAdapter(HashMap<Integer, String> hashMap)
    {
        Message msg = new Message();
        msg.obj = hashMap;
        handler.sendMessage(msg);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    };
}
