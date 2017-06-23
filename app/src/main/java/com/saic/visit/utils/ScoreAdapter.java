package com.saic.visit.utils;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.saic.visit.R;
import com.saic.visit.common.BaseActivity;
import com.saic.visit.model.CheckPointVo;
import com.saic.visit.model.TaskInfoVo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuhui on 2016/5/13.
 */
public class ScoreAdapter extends BaseAdapter {
    private Context context;
    private List<CheckPointVo> tasks;
    private ViewGroup.LayoutParams params;

    public ScoreAdapter(Context context, List<CheckPointVo> tasks) {
        this.context = context;
        this.tasks = tasks;
    }

    @Override
    public int getCount() {
        return tasks.size();
    }

    @Override
    public Object getItem(int position) {
        return tasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_score, null);
            viewHolder = new ViewHolder();
            viewHolder.titleName = (TextView) convertView.findViewById(R.id.title);
            viewHolder.content = (ListView) convertView.findViewById(R.id.list_content);
            viewHolder.layout = (FrameLayout) convertView.findViewById(R.id.layout);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.titleName.setText(tasks.get(position).getName());

        ContentAdapter mContentAdpter = new ContentAdapter(context,tasks.get(position).getItemVos());
        viewHolder.content.setAdapter(mContentAdpter);
        params = viewHolder.layout.getLayoutParams();
        params.height = ViewHeightUtils.setListViewHeightBasedOnChildren1(viewHolder.content,context)+ViewUtil.dpToPx(12,context);
        viewHolder.layout.setLayoutParams(params);
        return convertView;
    }

    class ViewHolder {
        public TextView titleName;
        public ListView content;
        public FrameLayout layout;
    }

}
