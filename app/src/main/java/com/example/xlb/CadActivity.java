package com.example.xlb;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.example.xlb.databinding.ActivityCadBinding;

import sfListView.CADAdapter;
import sfListView.CADListView;


public class CadActivity extends AppCompatActivity {

    private ListView lv;
    private CADAdapter adapter;
    private ActivityCadBinding activityCadBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCadBinding = ActivityCadBinding.inflate(LayoutInflater.from(this));
        setContentView(activityCadBinding.getRoot());
        activityCadBinding.btLog1.setImageResource(R.drawable.xlb_log);
       // activityCadBinding.btLog1.setVisibility(View.GONE);
        lv = (CADListView)findViewById(R.id.cad_list_view);
        adapter = new CADAdapter(CadActivity.this);
        lv.setAdapter(adapter);
    }
}
