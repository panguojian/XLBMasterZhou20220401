package com.example.xlb;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class MySimpleAdapter extends SimpleAdapter {

//重写listview适配器


    private int[] colors1 = new int[] {0x44FF0000,0x88FF0000};

   // private int[] Colors2 = new int[] { 0xFFcd211e, 0xFF3e9e0f};


    private ArrayList<HashMap<String, Object>> listitem;


    public MySimpleAdapter(Context context, ArrayList<HashMap<String, Object>> data,int resource,  String[] from, int[] to)
    {

        super(context, data, resource, from, to);

// TODO Auto-generated constructor stub

        this.listitem = data;

    }

    @Override

    public View getView(int position, View convertView, ViewGroup parent) {

        View view = super.getView(position, convertView, parent);


       // int colorPos = position % colors1.length;
       // view.setBackgroundColor(colors1[colorPos]);


        //下面设置背景色效果。

       // TextView dataP = (TextView)view.findViewById(R.id.dataPa);
       // TextView pa = (TextView)view.findViewById(R.id.pa);

        //String strTmpH = listitem.get(position).get("dataHWAR").toString();  //高压报警
      //  String strTmpL = listitem.get(position).get("dataLWAR").toString();  //低压报警


//        if(Boolean.parseBoolean(strTmpH))
//        {
//            dataP.setBackgroundColor(colors1[1]);
//            pa.setBackgroundColor(colors1[1]);
////            dataP.setTextColor(colors1[0]);
////            pa.setTextColor(colors1[0]);
//        }
//        else if(Boolean.parseBoolean(strTmpL)){
//
//            dataP.setBackgroundColor(colors1[0]);
//            pa.setBackgroundColor(colors1[0]);
////            dataP.setTextColor(colors1[0]);
////            pa.setTextColor(colors1[0]);
//
//        }
        return view;

    }





}
