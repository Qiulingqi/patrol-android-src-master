package com.saic.visit.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;


import com.saic.visit.R;
import com.saic.visit.model.ItemVo;
import com.saic.visit.utils.ViewUtil;

import java.util.ArrayList;
import java.util.List;

public class CheckBoxAndEditTextView extends LinearLayout {

    private Context context;
    private ArrayList<String> checkValue = new ArrayList<String>();

    private ArrayList<CheckBox> ckbs = new ArrayList<CheckBox>();
    private Boolean flag;
    public CheckBoxAndEditTextView(Context context) {
        super(context);
        initialize(context, null);
    }

    public CheckBoxAndEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initialize(context, attrs);

    }
    String orientation = "";
    private void initialize(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.radio_button_orientation);
        orientation = typedArray.getString(R.styleable.radio_button_orientation_orientation);
        setOrientation("vertical".equals(orientation)?LinearLayout.VERTICAL:LinearLayout.HORIZONTAL);
        flag = typedArray.getBoolean(R.styleable.radio_button_orientation_flag,false);
    }

    public void initData(final List<ItemVo> data,final CheckClickListener listener,boolean flag) {
        removeAllViews();
        int size = data.size();
        CheckBox checkBox = null;
        for (int i = 0; i < size; i++) { // 控制行
            RelativeLayout.LayoutParams relativeLayoutParams;
            if(!flag){
                checkBox = (CheckBox) LayoutInflater.from(getContext()).inflate(R.layout.checkbox, null);
                relativeLayoutParams = new RelativeLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                checkBox.setText(data.get(i).getName());
                checkBox.setPadding(ViewUtil.dpToPx(20, context), ViewUtil.dpToPx(5, context),ViewUtil.dpToPx(20, context), ViewUtil.dpToPx(0, context));
                checkBox.setLayoutParams(relativeLayoutParams);
            }else{
                checkBox = (CheckBox) LayoutInflater.from(getContext()).inflate(R.layout.checkbox_dash, null);
                relativeLayoutParams = new RelativeLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                checkBox.setText(String.valueOf(data.get(i).getName()));
                checkBox.setPadding(ViewUtil.dpToPx(20, context), ViewUtil.dpToPx(16, context),ViewUtil.dpToPx(8, context), ViewUtil.dpToPx(16, context));
                checkBox.setLayoutParams(relativeLayoutParams);
            }
            if(data.get(i).ischeck()){
                checkBox.setChecked(true);
            }else{
                checkBox.setChecked(false);
            }
            ckbs.add(checkBox);
            addView(checkBox);
            final int finalI = i;
            if(flag){
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked && !checkValue.contains(buttonView.getText().toString())) {
                            checkValue.add(buttonView.getText().toString());
                            data.get(finalI).setIscheck(true);
                        }else {
                            checkValue.remove(buttonView.getText().toString());
                            data.get(finalI).setIscheck(false);
                        }
                        listener.afterPositive(data);
                    }
                });
            }

        }
    }

    public interface CheckClickListener{
        /**
         * 监听选中或没选中
         */
        void afterPositive(List<ItemVo> data);
    }
}
