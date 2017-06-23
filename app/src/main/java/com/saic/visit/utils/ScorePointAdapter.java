package com.saic.visit.utils;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.saic.visit.R;
import com.saic.visit.model.CatalogVo;
import com.saic.visit.model.CheckPointVo;
import com.saic.visit.model.ItemVo;
import com.saic.visit.widget.MListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuhui on 2016/5/13.
 */
public class ScorePointAdapter extends BaseAdapter {
    private Context context;
    private List<CheckPointVo> tasks;
    private ViewGroup.LayoutParams params;
    public ScorePointAdapter(Context context, List<CheckPointVo> tasks) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_score_point, null);
            viewHolder = new ViewHolder();
            viewHolder.visitPoint = (TextView) convertView.findViewById(R.id.visit_point);
            viewHolder.content = (ListView) convertView.findViewById(R.id.list_content);
            viewHolder.layout = (LinearLayout) convertView.findViewById(R.id.layout);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final CheckPointVo vo = tasks.get(position);
        viewHolder.visitPoint.setText(vo.getName());
        ContentAdapter mContentAdpter = new ContentAdapter(context,vo.getItemVos());
        viewHolder.content.setAdapter(mContentAdpter);
//        mContentAdpter.notifyDataSetChanged();
//        params = viewHolder.layout.getLayoutParams();
//        params.height = ViewHeightUtils.setListViewHeightBasedOnChildren1(viewHolder.content, context);
//        viewHolder.layout.setLayoutParams(params);
        return convertView;
    }

    class ViewHolder {
        public TextView visitPoint;
        public ListView content;
        public LinearLayout layout;
    }



}
