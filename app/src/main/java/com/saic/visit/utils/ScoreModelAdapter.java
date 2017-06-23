package com.saic.visit.utils;


import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.saic.visit.R;
import com.saic.visit.model.ModuleVo;
import com.saic.visit.widget.MListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuhui on 2016/5/13.
 */
public class ScoreModelAdapter extends BaseAdapter {
    private Context context;
    private List<ModuleVo> tasks;
    private ViewGroup.LayoutParams params;
    private List<ModuleVo> select = new ArrayList<ModuleVo>();

    public ScoreModelAdapter(Context context, List<ModuleVo> tasks) {
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
            convertView = View.inflate(context, R.layout.item_score_model, null);
            viewHolder = new ViewHolder();
            viewHolder.titleName = (TextView) convertView.findViewById(R.id.task_name);
            viewHolder.content = (ListView) convertView.findViewById(R.id.list_content);
            viewHolder.layoutName = (LinearLayout) convertView.findViewById(R.id.layout_name);
            viewHolder.layout = (LinearLayout) convertView.findViewById(R.id.layout);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final ModuleVo vo = tasks.get(position);
        viewHolder.titleName.setText((position + 1) + "." + vo.getName());
        ScoreLinkAdapter mContentAdpter = new ScoreLinkAdapter(context,tasks.get(position).getCatalogVos(),(position+1)+".");
        viewHolder.content.setAdapter(mContentAdpter);
//        mContentAdpter.notifyDataSetChanged();
//        params = viewHolder.layout.getLayoutParams();
//        params.height = ViewHeightUtils.setListViewHeightBasedOnChildren1(viewHolder.content, context);
//        viewHolder.layout.setLayoutParams(params);
        viewHolder.layout.setVisibility(select.contains(vo) ? View.VISIBLE : View.GONE);
        viewHolder.layoutName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (select.contains(vo)) {
                    select.remove(vo);
                } else {
                    select.add(vo);
                }
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    class ViewHolder {
        public TextView titleName;
        public ListView content;
        public LinearLayout layout;
        public LinearLayout layoutName;
    }

}
