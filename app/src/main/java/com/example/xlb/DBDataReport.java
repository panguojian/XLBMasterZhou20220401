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
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.xlb.config.SetConfig;
import com.example.xlb.databinding.ActivityDataReportBinding;
import com.example.xlb.databinding.ActivityDbdataSettingBinding;
import com.example.xlb.utils.ByteUtils;
import com.example.xlb.utils.FunctionUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class DBDataReport extends AppCompatActivity{

    private ActivityDataReportBinding acDataReportBd;
    private Fragment f=null;
    private Fragment current=null;
    private Context context;
    private String LOG_TYPE="WAR";
    private ArrayList<HashMap<String, Object>> listitem = new ArrayList<HashMap<String, Object>>();
    private HashMap<Integer, String> hashMap=new HashMap<Integer, String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context=this;
        acDataReportBd = ActivityDataReportBinding.inflate(LayoutInflater.from(this));
        setContentView(acDataReportBd.getRoot());

        acDataReportBd.btLog1.setImageResource(R.drawable.xlb_log);
        //acDataReportBd.btLog1.setVisibility(View.GONE);
        acDataReportBd.btRead.setImageResource(R.drawable.xlb_read);
        progressBarHide();
    }






    private void progressBarHide()
    {
        acDataReportBd.progressBar1.setVisibility(View.GONE);
    }

    private void progressBarShow()
    {
        acDataReportBd.progressBar1.setVisibility(View.VISIBLE);
    }

}
