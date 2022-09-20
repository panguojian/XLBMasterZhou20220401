package edv_vav;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.xlb.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import sfListView.Room;

public class EDV_VAV_Adapter extends BaseAdapter {
    private List<Room> list = new ArrayList<Room>();
    private LayoutInflater inflater;
    private Activity activity;
    private HashMap<Integer, String> hashMap;
    private int index;
    private EDV_VAV_Adapter.myWatcher mWatcher;
    private String vindex,type,model_index;

    public EDV_VAV_Adapter(Activity activity, HashMap hashMap,String type,String mode_index) {
        this.activity = activity;
        inflater = LayoutInflater.from(this.activity);
        this.hashMap = hashMap;
        this.type=type;
        this.model_index=mode_index;
        getRoom();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return list.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {
        // TODO Auto-generated method stub
        VHolder vh = null;
        index = position;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.edv_vav, null);
            vh = new VHolder();
            vh.room = (TextView) convertView.findViewById(R.id.room);
            vh.index = (TextView) convertView.findViewById(R.id.index);
            vh.room.setText(list.get(position).getName());
            vh.index.setText(list.get(position).getIndex());
            vh.tv1 = (TextView) convertView.findViewById(R.id.tv1);
            vh.tv2 = (TextView) convertView.findViewById(R.id.tv2);
            vh.tv3 = (TextView) convertView.findViewById(R.id.tv3);
            vh.tv4 = (TextView) convertView.findViewById(R.id.tv4);
            vh.tv5 = (TextView) convertView.findViewById(R.id.tv5);
            vh.tv6 = (TextView) convertView.findViewById(R.id.tv6);
            vh.edt1 = (EditText) convertView.findViewById(R.id.edt1);
            vh.edt2 = (EditText) convertView.findViewById(R.id.edt2);
            vh.edt3 = (EditText) convertView.findViewById(R.id.edt3);
            vh.edt4 = (EditText) convertView.findViewById(R.id.edt4);
            vh.edt5 = (EditText) convertView.findViewById(R.id.edt5);
            vh.aswitch = (Switch) convertView.findViewById(R.id.switchbt);
            vh.imageView = (ImageView) convertView.findViewById(R.id.right_wrong);
            convertView.setTag(vh);
        } else {
            vh = (VHolder) convertView.getTag();
        }

        if(type.equals("VAV")) {
            SetEDV_GONE(vh);
            SetVAV(vh,position);
        }
        else if(type.equals("EDV")) {
            SetVAV_GONE(vh);
            SetEDV(vh,position);
        }
        return convertView;
    }

    private List<Room> getRoom() {
        String[] r=activity.getResources().getStringArray(R.array.vavroom);
        if(type.equals("EDV"))
        {
            switch (model_index) {
                case "1":
                    r = activity.getResources().getStringArray(R.array.edvroom1);
                    break;
                case "2":
                    r = activity.getResources().getStringArray(R.array.edvroom2);
                    break;
                case "3":
                    r = activity.getResources().getStringArray(R.array.edvroom3);
                    break;
                case "4":
                    r = activity.getResources().getStringArray(R.array.edvroom4);
                    break;
            }

        }
        else if(type.equals("VAV"))
        {
            switch (model_index) {
                case "1":
                    r = activity.getResources().getStringArray(R.array.vavroom1);
                    break;
                case "2":
                    r = activity.getResources().getStringArray(R.array.vavroom2);
                    break;
            }
        }

        for (int i = 0; i < r.length; i++) {
            Room room = new Room();
            room.setName(r[i]);
            room.setIndex(i + 1 + ")");
            list.add(room);
        }
        return list;
    }

    private void SetVAV(VHolder vh,final int position)
    {
        try {
            if (hashMap != null) {
                String V1 = hashMap.get(position).split("\\|")[0].split(":")[1];
                String V2 = hashMap.get(position).split("\\|")[1].split(":")[1];
                String V3 = hashMap.get(position).split("\\|")[2].split(":")[1];
                String VS = hashMap.get(position).split("\\|")[4].split(":")[1];

                if (V1 != "0") {
                    vh.edt3.setText(V1);
                }
                if (V2 != "0") {
                    vh.edt4.setText(V2);
                }
                if (V3 != "0") {
                    vh.edt5.setText(V3);
                }
                if (VS.length() == 36) {
                    vh.imageView.setImageResource(R.drawable.right);
                } else {
                    vh.imageView.setImageResource(R.drawable.wrong);
                }
            }

            vh.aswitch.setTag(position);
            vh.aswitch.setOnCheckedChangeListener(null);
            if (hashMap != null) {
                String V4 = hashMap.get(position).split("\\|")[3].split(":")[1];
                vh.aswitch.setChecked(Boolean.parseBoolean(V4));
            }
            vh.aswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(hashMap==null)
                    {
                        return;
                    }
                    index = position;
                    if (isChecked) {
                        String V1 = hashMap.get(index).split("\\|")[0];
                        String V2 = hashMap.get(index).split("\\|")[1];
                        String V3 = hashMap.get(index).split("\\|")[2];
                        String VS = hashMap.get(index).split("\\|")[4];
                        hashMap.put(index, V1 + "|" + V2 + "|" + V3 + "|" + "V4:true" + "|" + VS);
                    } else {
                        String V1 = hashMap.get(index).split("\\|")[0];
                        String V2 = hashMap.get(index).split("\\|")[1];
                        String V3 = hashMap.get(index).split("\\|")[2];
                        String VS = hashMap.get(index).split("\\|")[4];
                        hashMap.put(index, V1 + "|" + V2 + "|" + V3 + "|" + "V4:false" + "|" + VS);
                    }
                }
            });

            vh.edt3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    EditText et = (EditText) v;
                    if (mWatcher == null) {
                        mWatcher = new EDV_VAV_Adapter.myWatcher();
                    }
                    if (hasFocus) {
                        index = position;
                        vindex = "V1";
                        et.addTextChangedListener(mWatcher);
                    } else {
                        et.removeTextChangedListener(mWatcher);
                    }

                }

            });
            vh.edt4.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    EditText et = (EditText) v;
                    if (mWatcher == null) {
                        mWatcher = new EDV_VAV_Adapter.myWatcher();
                    }
                    if (hasFocus) {
                        index = position;
                        vindex = "V2";
                        et.addTextChangedListener(mWatcher);
                    } else {
                        et.removeTextChangedListener(mWatcher);
                    }

                }

            });
            vh.edt5.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    EditText et = (EditText) v;
                    if (mWatcher == null) {
                        mWatcher = new EDV_VAV_Adapter.myWatcher();
                    }
                    if (hasFocus) {
                        index = position;
                        vindex = "V3";
                        et.addTextChangedListener(mWatcher);
                    } else {
                        et.removeTextChangedListener(mWatcher);
                    }

                }

            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void SetEDV(VHolder vh,final int position)
    {
        try {
            if (hashMap != null) {
                String V1 = hashMap.get(position).split("\\|")[0].split(":")[1];
                String V2 = hashMap.get(position).split("\\|")[1].split(":")[1];
                String VS = hashMap.get(position).split("\\|")[2].split(":")[1];
                if (V1 != "0") {
                    vh.edt1.setText(V1);
                }
                if (V2 != "0") {
                    vh.edt2.setText(V2);
                }
                if (VS.length() == 20) {
                    vh.imageView.setImageResource(R.drawable.right);
                } else {
                    vh.imageView.setImageResource(R.drawable.wrong);
                }
            }
            vh.edt1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    EditText et = (EditText) v;
                    if (mWatcher == null) {
                        mWatcher = new myWatcher();
                    }
                    if (hasFocus) {
                        index = position;
                        vindex = "V1";
                        et.addTextChangedListener(mWatcher);
                    } else {
                        et.removeTextChangedListener(mWatcher);
                    }

                }

            });
            vh.edt2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    EditText et = (EditText) v;
                    if (mWatcher == null) {
                        mWatcher = new myWatcher();
                    }
                    if (hasFocus) {
                        index = position;
                        vindex = "V2";
                        et.addTextChangedListener(mWatcher);
                    } else {
                        et.removeTextChangedListener(mWatcher);
                    }

                }

            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void SetVAV_GONE(VHolder vh)
    {
        vh.tv3.setVisibility(View.GONE);
        vh.tv4.setVisibility(View.GONE);
        vh.tv5.setVisibility(View.GONE);
        vh.tv6.setVisibility(View.GONE);
        vh.edt3.setVisibility(View.GONE);
        vh.edt4.setVisibility(View.GONE);
        vh.edt5.setVisibility(View.GONE);
        vh.aswitch.setVisibility(View.GONE);
    }

    private void SetEDV_GONE(VHolder vh)
    {
        vh.tv1.setVisibility(View.GONE);
        vh.tv2.setVisibility(View.GONE);
        vh.edt1.setVisibility(View.GONE);
        vh.edt2.setVisibility(View.GONE);
    }

    class myWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            // TODO Auto-generated method stub


        }

        @Override
        public void afterTextChanged(Editable s) {
            if(hashMap==null)
            {
                return;
            }
            String val = "";
            val = s.toString().length() > 0 ? s.toString() : "0";

            if(type.equals("VAV")) {
                if (vindex == "V1") {
                    String V2 = hashMap.get(index).split("\\|")[1];
                    String V3 = hashMap.get(index).split("\\|")[2];
                    String V4 = hashMap.get(index).split("\\|")[3];
                    String VS = hashMap.get(index).split("\\|")[4];
                    hashMap.put(index, "V1:" + val + "|" + V2 + "|" + V3 + "|" + V4 + "|" + VS);
                    // Toast.makeText(activity,hashMap.get(index),Toast.LENGTH_SHORT).show();
                } else if (vindex == "V2") {
                    String V1 = hashMap.get(index).split("\\|")[0];
                    String V3 = hashMap.get(index).split("\\|")[2];
                    String V4 = hashMap.get(index).split("\\|")[3];
                    String VS = hashMap.get(index).split("\\|")[4];
                    hashMap.put(index, V1 + "|" + "v2:" + val + "|" + V3 + "|" + V4 + "|" + VS);
                } else if (vindex == "V3") {
                    String V1 = hashMap.get(index).split("\\|")[0];
                    String V2 = hashMap.get(index).split("\\|")[1];
                    String V4 = hashMap.get(index).split("\\|")[3];
                    String VS = hashMap.get(index).split("\\|")[4];
                    hashMap.put(index, V1 + "|" + V2 + "|" + "v3:" + val + "|" + V4 + "|" + VS);
                }
            }
            else if(type.equals("EDV"))
            {
                if(vindex=="V1"){
                    String V2=hashMap.get(index).split("\\|")[1];
                    String VS=hashMap.get(index).split("\\|")[2];
                    hashMap.put(index, "V1:"+val+"|"+V2+"|"+VS);
                    // Toast.makeText(activity,hashMap.get(index),Toast.LENGTH_SHORT).show();
                }
                else if(vindex=="V2"){
                    String VS=hashMap.get(index).split("\\|")[2];
                    String V1=hashMap.get(index).split("\\|")[0];
                    hashMap.put(index, V1+"|"+"v2:"+val+"|"+VS);
                    //Toast.makeText(activity,hashMap.get(index),Toast.LENGTH_SHORT).show();
                }
            }

        }

    }

}
