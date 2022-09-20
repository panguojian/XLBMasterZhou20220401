package sfListView;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.xlb.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CADAdapter extends BaseAdapter {
    private List<Room> list = new ArrayList<Room>();
    private LayoutInflater inflater;
    private Activity activity;
    private int index;

    public CADAdapter(Activity activity){
        this.activity = activity;
        inflater = LayoutInflater.from(this.activity);
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
        ViewHolder vh = null;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.cad_list, null);

            vh = new ViewHolder();
            vh.bt1=(Button) convertView.findViewById(R.id.button_image_left);
            convertView.setTag(vh);
        }
        else {
            vh = (ViewHolder)convertView.getTag();
        }
        if (list.size() >0) {
            Drawable drawable = activity.getDrawable(list.get(position).getImage());
            drawable.setBounds(0, 0, drawable.getMinimumWidth(),drawable.getMinimumHeight());
            vh.bt1.setCompoundDrawables(null, drawable, null, null);
        }

        return convertView;
    }

    private List<Room> getRoom(){
        int[] imageArry =new int[]{R.drawable.crd1,R.drawable.crd2,R.drawable.crd3,R.drawable.crd4,R.drawable.crd5,R.drawable.crd6,R.drawable.crd7,R.drawable.crd8,R.drawable.crd9,R.drawable.crd10,R.drawable.crd11,R.drawable.crd12,R.drawable.crd13,R.drawable.crd14};
        String[] r=activity.getResources().getStringArray(R.array.roomcad);
        for(int i=0; i<r.length; i++){
            Room room = new Room();
            room.setIndex("("+i+1+")");
            room.setName(r[i]);
            room.setImage(imageArry[i]);
            list.add(room);
        }
        return list;
    }

}