package com.saic.visit.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.saic.visit.R;
import com.saic.visit.model.CatalogVo;
import com.saic.visit.model.CodeItem;
import com.saic.visit.model.ModuleVo;
import com.saic.visit.model.TaskDetailResponse;
import com.saic.visit.utils.ViewUtil;

import java.util.ArrayList;
import java.util.List;

public class RadioButtonAndEditTextView extends LinearLayout {
    private RadioGroup mRadioGroup;
    private String radioValue="";
    private Context context;
    private List<RadioButton> mListRB = new ArrayList<RadioButton>();
    public RadioButtonAndEditTextView(Context context) {
        super(context);
        initialize(context, null);
    }

    public RadioButtonAndEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initialize(context, attrs);

    }
    String orientation = "";
    private void initialize(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.radio_button_orientation);
        orientation = typedArray.getString(R.styleable.radio_button_orientation_orientation);
        View.inflate(context, R.layout.radio_button_text, this);
        mRadioGroup = ViewUtil.findViewById(this, R.id.rgp);
        mRadioGroup.setOrientation("horizontal".equals(orientation) ? LinearLayout.HORIZONTAL : LinearLayout.VERTICAL);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton tempButton = (RadioButton) findViewById(checkedId); // 通过RadioGroup的findViewById方法，找到ID为checkedID的RadioButton
                // 以下就可以对这个RadioButton进行处理了
                radioValue = tempButton.getText().toString();

            }
        });

    }


    public void initData(final RadioButtonAndEditTextView view ,Object data,int size,boolean flag, final RadioButtonClickListener listener) {
        mListRB.clear();
        mRadioGroup.removeAllViews();
        for (int i=0;i<size;i++){
            final RadioButton tempButton;
            if(!flag){
                tempButton = (RadioButton) LayoutInflater.from(getContext()).inflate(R.layout.radiobutton_horizontal, null);
                tempButton.setGravity(Gravity.CENTER);
                tempButton.setPadding(0, ViewUtil.dpToPx(16, context), 0, ViewUtil.dpToPx(16, context));
                if(data instanceof TaskDetailResponse){
                    tempButton.setText(((TaskDetailResponse) data).models.get(i).getName());
                }else if(data instanceof ModuleVo){
                    tempButton.setText(((ModuleVo) data).getCatalogVos().get(i).getName());
                }
                tempButton.setTextSize(14);
                tempButton.setTag((flag && i == size - 1) ? true : false);
            }else{
                tempButton = (RadioButton) LayoutInflater.from(getContext()).inflate(R.layout.radiobutton_vertical, null);
                tempButton.setPadding(ViewUtil.dpToPx(64, context), ViewUtil.dpToPx(8, context), ViewUtil.dpToPx(32, context), ViewUtil.dpToPx(8, context));
                if(data instanceof TaskDetailResponse){
                    tempButton.setText(((TaskDetailResponse) data).models.get(i).getName());
                }else if(data instanceof ModuleVo){
                    tempButton.setText(((ModuleVo) data).getCatalogVos().get(i).getName());
                }else if(data instanceof CatalogVo ){
                    tempButton.setText(((CatalogVo) data).getCatalogVos().get(i).getName());
                }
                tempButton.setTextSize(18);
                tempButton.setTag((flag && i == size - 1) ? true : false);
                if(i == size-1){
                    tempButton.setBackgroundResource(R.drawable.view_line);
                }

            }
            tempButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = -1;
                    for (int i = 0; i<mListRB.size(); i++){
                        if(radioValue .equals( mListRB.get(i).getText().toString())){
                            index = i;
                            break;
                        }

                    }

                    listener.afterPositive(view,radioValue,index);
                }
            });
            WindowManager wm = (WindowManager) getContext()
                    .getSystemService(Context.WINDOW_SERVICE);
            int width = wm.getDefaultDisplay().getWidth();
            LayoutParams llp;
            float height;
            if(!flag){
                if(size>=3){
                    int line = (int) Math.ceil((tempButton.getPaint().measureText(tempButton.getText().toString()) / (width/3)));
                    if(line>1){
                        height = (tempButton.getPaint().getFontMetrics().descent - tempButton.getPaint().getFontMetrics().ascent) * line;
                        llp = new LayoutParams((int) (width/2.5), LayoutParams.WRAP_CONTENT);
                    }else {
                        llp = new LayoutParams(width/3, LayoutParams.WRAP_CONTENT);
                    }
//                    llp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                }else {
                    llp = new LayoutParams(width/(size), LayoutParams.WRAP_CONTENT);
                }
            }else{
                llp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            }
            llp.setMargins(0, 0, ViewUtil.dpToPx(40, context), 0);
            mListRB.add(tempButton);
            mRadioGroup.addView(tempButton, llp);

        }
    }


    public void setmRadioChecked(String data){
        if(data==null||data.length()<=0){
            return;
        }
        for (int i = 0;i<mListRB.size();i++){
            if(data.equals(mListRB.get(i).getText().toString())){
                mListRB.get(i).setChecked(true);
                return;
            }else{
                mListRB.get(i).setChecked(false);
            }
        }
    }

    public interface RadioButtonClickListener{
        /**
         * 监听选中或没选中
         */
        void afterPositive(RadioButtonAndEditTextView view ,String radioValue,int index);
    }

}
