package Report;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.xlb.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import sfListView.Room;

public class Report_Adapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Activity activity;
    private HashMap<Integer, String> hashMap;
    private ArrayList<HashMap<String, Object>> listitem = new ArrayList<HashMap<String, Object>>();
    private int index;
    private String LOG_TYPE;

    public Report_Adapter(Activity activity, ArrayList<HashMap<String, Object>> listitem,String log_type) {
        this.activity = activity;
        inflater = LayoutInflater.from(this.activity);
        this.listitem = listitem;
         this.LOG_TYPE=log_type;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return listitem.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return listitem.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {
        // TODO Auto-generated method stub
        Report.Re_VH vh = null;
        index = position;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.report, null);
            vh = new Report.Re_VH();
            vh.room = (TextView) convertView.findViewById(R.id.room);
            vh.index = (TextView) convertView.findViewById(R.id.index);
            vh.R_Value = (TextView) convertView.findViewById(R.id.tv1);
            vh.R_Status = (TextView) convertView.findViewById(R.id.tv2);
            vh.R_time = (TextView) convertView.findViewById(R.id.tv3);

            convertView.setTag(vh);
        } else {
            vh = (Report.Re_VH) convertView.getTag();
        }
        if(LOG_TYPE.equals("WAR"))
        {
            SetReportData_WAR(vh,position);
        }
        else {
            SetReportData_SYS(vh,position);
        }

        return convertView;
    }


    private void SetReportData_WAR(Re_VH vh, final int position)
    {
        try {
            if (listitem != null) {
                vh.index.setText(listitem.get(position).get("id").toString());
                vh.room.setText(listitem.get(position).get("room").toString());
                vh.R_Value.setText(listitem.get(position).get("val").toString());
                vh.R_Status.setText(listitem.get(position).get("warmes").toString());
                vh.R_time.setText(listitem.get(position).get("date_time").toString());
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void SetReportData_SYS(Re_VH vh, final int position)
    {
        try {
            if (listitem != null) {
                vh.index.setText(listitem.get(position).get("id").toString());
                vh.room.setText(listitem.get(position).get("title").toString());
                vh.R_Status.setText(listitem.get(position).get("message").toString());
                vh.R_time.setText(listitem.get(position).get("date_time").toString());
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


}
