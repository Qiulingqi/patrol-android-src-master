package com.saic.visit.utils;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.saic.visit.R;
import com.saic.visit.activity.ScoreActivity;
import com.saic.visit.model.ItemVo;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by liuhui on 2016/5/13.
 */
public class ContentAdapter extends BaseAdapter {
    private Context context;
    private List<ItemVo> data;

    public ContentAdapter(Context context, List<ItemVo> data) {
        this.context = context;
        this.data = data;
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
            convertView = View.inflate(context, R.layout.item_content, null);
            viewHolder = new ViewHolder();
            viewHolder.taskName = (TextView) convertView.findViewById(R.id.model_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.taskName.setText(context.getResources().getText(R.string.tran)+data.get(position).getName());
        return convertView;
    }

    class ViewHolder {
        public TextView taskName;
    }

}
