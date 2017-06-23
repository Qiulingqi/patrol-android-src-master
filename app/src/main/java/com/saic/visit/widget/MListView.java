package com.saic.visit.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * Created by Administrator on 2016/5/23.
 */
public class MListView extends ListView {
    public MListView(Context context) {
        // TODO Auto-generated method stub
        super(context);
        setMaxHeight(25000);
    }

    public MListView(Context context, AttributeSet attrs) {
        // TODO Auto-generated method stub
        super(context, attrs);
        setMaxHeight(25000);
    }

    public MListView(Context context, AttributeSet attrs, int defStyle) {
        // TODO Auto-generated method stub
        super(context, attrs, defStyle);
        setMaxHeight(25000);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
//        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
//                MeasureSpec.AT_MOST);
        if (maxHeight > -1) {
            heightMeasureSpec = Integer.MAX_VALUE;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    private int maxHeight;

    public int getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }
    // 禁止滑动
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev){
        if(ev.getAction()==MotionEvent.ACTION_MOVE)
            return true;
        return super.dispatchTouchEvent(ev);
    }


}
