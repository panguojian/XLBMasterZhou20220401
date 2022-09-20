package edv_vav;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.example.xlb.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import sfListView.Room;

public class EDV_VAV_PL_Adapter extends BaseAdapter {
    private List<Room> list = new ArrayList<Room>();
    private LayoutInflater inflater;
    private HashMap<Integer, String> hashMap;
    private Activity activity;
    private int index;
    private EDV_VAV_PL_Adapter.myWatcher mWatcher;
    private String type,model_index;

    public EDV_VAV_PL_Adapter(Activity activity, HashMap hashMap,String type,String mode_index){
        this.activity = activity;
        inflater = LayoutInflater.from(this.activity);
        this.hashMap=hashMap;
        this.type=type;
        this.model_index=mode_index;
        getRoom();
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if(list.size()%2>0) {
            return list.size()/2+1;
        } else {
            return list.size()/2;
        }
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
        //Toast.makeText(activity, ":"+position, Toast.LENGTH_SHORT).show();
        if(convertView == null){
            convertView = inflater.inflate(R.layout.edv_vav_pl, null);

            vh = new VHolder();
            vh.tv1=(TextView)convertView.findViewById(R.id.index1);
            vh.tv2=(TextView)convertView.findViewById(R.id.index2);
            vh.edt1=(EditText) convertView.findViewById(R.id.edt1);
            vh.edt2=(EditText) convertView.findViewById(R.id.edt2);
            convertView.setTag(vh);
        }
        else {
            vh = (VHolder)convertView.getTag();
        }

        int distance =  list.size() - position*2;
        int cellCount = distance >= 2? 2:distance;
        final List<Room> itemList = list.subList(position*2,position*2+cellCount);
        if (itemList.size() >0) {
            vh.tv1.setText(itemList.get(0).getIndex());

            if(hashMap!=null)
            {
                if (hashMap.get(position * 2) != "0") {
                    vh.edt1.setText(hashMap.get(position * 2).split("\\|")[0]);
                }
            }
            vh.edt1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    EditText et=(EditText) v;
                    if(mWatcher==null){
                        mWatcher=new myWatcher();
                    }
                    if(hasFocus){
                        index=position*2;
                        et.addTextChangedListener(mWatcher);
                    }else {
                        et.removeTextChangedListener(mWatcher);
                    }

                }

            });
            if (itemList.size() >1){
                vh.tv2.setVisibility(View.VISIBLE);
                vh.tv2.setText(itemList.get(1).getIndex());
                {
                    if(hashMap!=null) {
                        vh.edt2.setText(hashMap.get(position * 2 + 1).split("\\|")[0]);
                    }
                }
                vh.edt2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        EditText et=(EditText) v;
                        if(mWatcher==null){
                            mWatcher=new myWatcher();
                        }
                        if(hasFocus){
                            index=position*2+1;
                            et.addTextChangedListener(mWatcher);
                        }else {
                            et.removeTextChangedListener(mWatcher);
                        }

                    }
                });
            }else{
                vh.tv2.setVisibility(View.INVISIBLE);
                // vh.edt2.setVisibility(View.INVISIBLE);
            }
        }

        return convertView;
    }

    private List<Room> getRoom(){

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
                case "5":
                    r = activity.getResources().getStringArray(R.array.vavroom1);
                    break;
            }
        }

        for(int i=0; i<r.length; i++){
            Room room = new Room();
            room.setIndex(i+1+"台");
            list.add(room);
        }
        return list;
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
            String VS=hashMap.get(index).split("\\|")[1];
            hashMap.put(index, s.toString() +"|"+VS);
            // hashMap.put(index, s.toString());
        }

    }
}
