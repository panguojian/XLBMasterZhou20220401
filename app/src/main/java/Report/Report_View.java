package Report;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class Report_View extends ListView {

    public Report_View(Context context) {
        super(context);
    }

    public Report_View(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Report_View(Context context, AttributeSet attrs, int defStyleAttr) {
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
