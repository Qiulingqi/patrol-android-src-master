package com.saic.visit.utils;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.saic.visit.R;
import com.saic.visit.model.CatalogVo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuhui on 2016/5/13.
 */
public class ScoreItemAdapter extends BaseAdapter {
    private Context context;
    private List<CatalogVo> tasks;
    private List<CatalogVo> select = new ArrayList<CatalogVo>();
    private ViewGroup.LayoutParams params;
    private String index ;
    public ScoreItemAdapter(Context context, List<CatalogVo> tasks, String index) {
        this.context = context;
        this.tasks = tasks;
        this.index = index;
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
            convertView = View.inflate(context, R.layout.item_score_item, null);
            viewHolder = new ViewHolder();
            viewHolder.visitItem = (TextView) convertView.findViewById(R.id.visit_item);
            viewHolder.content = (ListView) convertView.findViewById(R.id.list_content);
            viewHolder.layout = (LinearLayout) convertView.findViewById(R.id.layout);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final CatalogVo vo = tasks.get(position);
        viewHolder.visitItem.setText(index + (position + 1) + vo.getName());
        ScorePointAdapter mContentAdpter = new ScorePointAdapter(context,vo.getCheckPointVos());
        viewHolder.content.setAdapter(mContentAdpter);
//        mContentAdpter.notifyDataSetChanged();
//        params = viewHolder.layout.getLayoutParams();
//        params.height = ViewHeightUtils.setListViewHeightBasedOnChildren1(viewHolder.content, context);
//        viewHolder.layout.setLayoutParams(params);
        return convertView;
    }

    class ViewHolder {
        public TextView visitItem;
        public ListView content;
        public LinearLayout layout;
    }



}
