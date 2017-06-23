package com.saic.visit.utils;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.saic.visit.R;
import com.saic.visit.model.CheckPointVo;
import com.saic.visit.model.ItemVo;
import com.saic.visit.widget.MTextView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by liuhui on 2016/5/13.
 */
public class PointContentAdapter extends BaseAdapter {
    private Context context;
    private List<ItemVo> data;
    Drawable drawableSelect;
    Drawable drawableUnSelect;
    private CheckClickListener listener;
    private int index = -1;
    public PointContentAdapter(Context context, List<ItemVo> data,CheckClickListener listener) {
        this.context = context;
        this.data = data;
        drawableSelect = context.getResources().getDrawable(R.drawable.check_box_on);
        /// 这一步必须要做,否则不会显示.
        drawableSelect.setBounds(0, 0, drawableSelect.getMinimumWidth(), drawableSelect.getMinimumHeight());
        drawableUnSelect = context.getResources().getDrawable(R.drawable.check_box_off);
        drawableUnSelect.setBounds(0, 0, drawableUnSelect.getMinimumWidth(), drawableUnSelect.getMinimumHeight());
        this.listener = listener;
        listener.afterPositive(data);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_point_content, null);
            viewHolder = new ViewHolder();
            viewHolder.taskName = (MTextView) convertView.findViewById(R.id.item_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(data.get(position).ischeck()){
            viewHolder.taskName.setCompoundDrawables(drawableSelect, null, null, null);
            data.get(position).setIscheck(true);
        }else{
            viewHolder.taskName.setCompoundDrawables(drawableUnSelect, null, null, null);
            data.get(position).setIscheck(false);
        }
        viewHolder.taskName.setText(data.get(position).getName());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.get(position).ischeck()) {
                    data.get(position).setIscheck(false);
                } else {
                    data.get(position).setIscheck(true);
                }
                listener.afterPositive(data);
                notifyDataSetChanged();

            }
        });

        return convertView;
    }

    class ViewHolder {
        public MTextView taskName;
    }

    public interface CheckClickListener{
        /**
         * 监听选中或没选中
         */
        void afterPositive(List<ItemVo> itemVos);
    }

}
