package edv_vav;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class EDV_VAV_PL_View extends ListView {

    public EDV_VAV_PL_View(Context context) {
        super(context);
    }

    public EDV_VAV_PL_View(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EDV_VAV_PL_View(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //int expendSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE, MeasureSpec.AT_MOST);
        int expendSpec=MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        //  System.out.println("===>start!"+String.valueOf(expendSpec));
        super.onMeasure(widthMeasureSpec, expendSpec);
    }
}
