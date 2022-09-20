package com.example.xlb;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.HashMap;

import edv_vav.EDV_VAV_Adapter;
import edv_vav.EDV_VAV_View;


public class DBData_Paramneter_Fragment extends Fragment {

    private ListView lv;
    private View view;
    private Spinner sp1,sp2;
    private EDV_VAV_Adapter adapter;
    private HashMap<Integer, String> hashMap;
    public   String type="EDV",model_index="1";

    public DBData_Paramneter_Fragment() {

    }

    public DBData_Paramneter_Fragment(HashMap<Integer, String> hashMap) {
        this.hashMap = hashMap;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_datasetting_edv_vav,container,false);
        lv = (EDV_VAV_View)view.findViewById(R.id.list_view);
        sp1=(Spinner) view.findViewById(R.id.Spinner01);
        sp2=(Spinner) view.findViewById(R.id.Spinner02);
        adapter = new EDV_VAV_Adapter(getActivity(),hashMap,type,model_index);
        lv.setAdapter(adapter);

        //final String[] r=getResources().getStringArray(R.array.vavroom);;
        sp1.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {//选择item的选择点击监听事件
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                hashMap=null;
                type=arg0.getItemAtPosition(arg2).toString();
                adapter = new EDV_VAV_Adapter(getActivity(),hashMap,type,model_index);
                lv.setAdapter(adapter);

                if(type.equals("EDV")) {
                    String[] r=getResources().getStringArray(R.array.edvindex);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, r);
                    sp2.setAdapter(adapter);
                    //Toast.makeText(getActivity(),model_index,Toast.LENGTH_SHORT).show();
                }
                else if(type.equals("VAV")) {
                    String[] r=getResources().getStringArray(R.array.vavindex);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, r);
                    sp2.setAdapter(adapter);
                   // Toast.makeText(getActivity(),model_index,Toast.LENGTH_SHORT).show();
                }

            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                // myTextView.setText("Nothing");
            }
        });

        sp2.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {//选择item的选择点击监听事件
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                hashMap=null;
                model_index=arg0.getItemAtPosition(arg2).toString();
                adapter = new EDV_VAV_Adapter(getActivity(),hashMap,type,model_index);
                lv.setAdapter(adapter);
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                // myTextView.setText("Nothing");
            }
        });



        return  view;
    }

    public void SetAdapter(HashMap<Integer, String> hashMap)
    {
        Message msg = new Message();
        msg.obj = hashMap;
        handler.sendMessage(msg);
    }
//spinner.setSelection(2,true);
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            hashMap=(HashMap)msg.obj;
            adapter = new EDV_VAV_Adapter(getActivity(),hashMap,type,model_index);
            lv.setAdapter(adapter);
        }
    };
}

