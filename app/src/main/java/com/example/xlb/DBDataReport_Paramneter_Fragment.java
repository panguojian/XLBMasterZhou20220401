package com.example.xlb;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import Report.Report_View;
import Report.Report_Adapter;
import Report.Re_VH;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class DBDataReport_Paramneter_Fragment extends Fragment {

    private ListView lv;
    private View view;
    public TextView ed1;
    private Report_Adapter adapter;
    private ArrayList<HashMap<String, Object>> listitem = new ArrayList<HashMap<String, Object>>();
    private HashMap<Integer, String> hashMap;
    private int mYear,mMonth,mDay;
    private Spinner sp1;
    public String  LOG_TYPE="WAR";

    public DBDataReport_Paramneter_Fragment() {

    }

    public DBDataReport_Paramneter_Fragment(ArrayList<HashMap<String, Object>> listitem) {
        this.listitem = listitem;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       // view=inflater.inflate(R.layout.fragment_data_report,container,false);
        view=inflater.inflate(R.layout.fragment_data_report,container,false);
        lv = (Report_View)view.findViewById(R.id.list_view);
        ed1=(TextView) view.findViewById(R.id.edt1);
        sp1=(Spinner) view.findViewById(R.id.Spinnerlog);
        adapter = new Report_Adapter(getActivity(),listitem,LOG_TYPE);
        lv.setAdapter(adapter);
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-M-d HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String dates=simpleDateFormat.format(date);
        ed1.setText(dates.substring(0,dates.length()-8));
        ed1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取日历的一个实例，里面包含了当前的年月日
                Calendar calendar= null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    calendar = Calendar.getInstance();
                    mYear=calendar.get(Calendar.YEAR);
                    mMonth = calendar.get(Calendar.MONTH);
                    mDay = calendar.get(Calendar.DAY_OF_MONTH);
                }
                //构建一个日期对话框，该对话框已经集成了日期选择器
                //DatePickerDialog的第二个构造参数指定了日期监听器
                DatePickerDialog dialog=new DatePickerDialog(getActivity(),R.style.picker,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mYear = year;
                        mMonth = month;
                        mDay = dayOfMonth;
                        String data = year +"-"+ (month+1) + "-" + dayOfMonth + "";
                        ed1.setText(data);
                    }
                },
                        mYear, mMonth, mDay);
                //把日期对话框显示在界面上
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ed1.setText("");

            }
        });
                dialog.show();

            }


        });


        sp1.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {//选择item的选择点击监听事件
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                if(arg0.getItemAtPosition(arg2).toString().equals("报警记录"))
                {
                    LOG_TYPE="WAR";
                }
                else
                {
                    LOG_TYPE="SYSLOG";
                }
               // Toast.makeText(getActivity(),LOG_TYPE,Toast.LENGTH_LONG).show();
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                // myTextView.setText("Nothing");
            }
        });
        return  view;
    }

    private void dateDialog() {
        try {

            Calendar calendar= null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                calendar = Calendar.getInstance();
                calendar.get(Calendar.MONTH);
            }
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            mYear = year;
                            mMonth = month;
                            mDay = dayOfMonth;
                            final String data =  (month+1) + "月-" + dayOfMonth + "日 ";
                        }
                    },
                    2020, mMonth, mDay);
            datePickerDialog.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }



    public void SetAdapter(ArrayList<HashMap<String, Object>> listitem)
    {
        Message msg = new Message();
        msg.obj = listitem;
        handler.sendMessage(msg);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            listitem=(ArrayList<HashMap<String, Object>> )msg.obj;
            adapter = new Report_Adapter(getActivity(),listitem,LOG_TYPE);
            lv.setAdapter(adapter);
        }
    };
}

