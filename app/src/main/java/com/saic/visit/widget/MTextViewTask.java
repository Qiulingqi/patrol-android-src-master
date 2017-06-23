package com.saic.visit.widget;

import android.content.Context;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.WindowManager;
import android.widget.TextView;

import com.saic.visit.utils.ViewUtil;

/**
 * Created by Administrator on 2016/5/23.
 */
public class MTextViewTask extends TextView {
    private Context context;
    public MTextViewTask(Context context) {
        super(context);
        this.context = context;
    }

    public MTextViewTask(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public MTextViewTask(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Layout layout = getLayout();
        if (layout != null) {
            int height = (int) Math.ceil(getMaxLineHeight(this.getText().toString()))
                    + ViewUtil.dpToPx(16,context) + ViewUtil.dpToPx(18, context);
            int width = getMeasuredWidth();
            setMeasuredDimension(width, height);

        }
    }

    private float getMaxLineHeight(String str) {
        float height = 0.0f;
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        float screenW = wm.getDefaultDisplay().getWidth();;
        float paddingLeft = ViewUtil.dpToPx(36, context) + 25;
        float paddingReft = ViewUtil.dpToPx(96, context);
      //这里具体this.getPaint()要注意使用，要看你的TextView在什么位置，这个是拿TextView父控件的Padding的，为了更准确的算出换行
        int line = (int) Math.ceil((this.getPaint().measureText(str) / (screenW - paddingLeft - paddingReft)));
        height = (this.getPaint().getFontMetrics().descent - this.getPaint().getFontMetrics().ascent) * line;
        return height;
    }

}
