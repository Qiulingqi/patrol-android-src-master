package com.saic.visit.utils;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.saic.visit.R;
import com.saic.visit.activity.OtherConfirmActivity;
import com.saic.visit.activity.ScoreActivity;
import com.saic.visit.model.OtherConfirmRequest;
import com.saic.visit.model.TaskDetailResponse;

import java.util.List;


/**
 * Created by liuhui on 2016/5/13.
 */
public class PopWindowAdapter extends BaseAdapter {
    private Context context;
    private Object data;

    public PopWindowAdapter(Context context, Object data) {
        this.context = context;
        this.data = data;


    }

    @Override
    public int getCount() {
        if(data instanceof  TaskDetailResponse){
            return ((TaskDetailResponse)data).models.size();
        }else{
            return ((List<OtherConfirmRequest>)data).size();
        }
    }

    @Override
    public Object getItem(int position) {
        if(data instanceof  TaskDetailResponse){
            return ((TaskDetailResponse)data).models.get(position);
        }else{
            return ((String[])data)[position];
        }

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_model, null);
            viewHolder = new ViewHolder();
            viewHolder.taskName = (TextView) convertView.findViewById(R.id.model_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(data instanceof  TaskDetailResponse){
            viewHolder.taskName.setText(((TaskDetailResponse)data).models.get(position).getName());
        }else{
            viewHolder.taskName.setText(((List<OtherConfirmRequest>)data).get(position).getReason());
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof ScoreActivity||context instanceof OtherConfirmActivity) {
                    if(data instanceof  TaskDetailResponse){

                    }else{
                        ((OtherConfirmActivity) context).setTxtModel(((List<OtherConfirmRequest>)data).get(position));
                    }

                }

            }
        });
        return convertView;
    }

    class ViewHolder {
        public TextView taskName;
    }

}
