package com.saic.visit.utils;


import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.saic.visit.R;
import com.saic.visit.model.ModuleVo;
import com.saic.visit.widget.MListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuhui on 2016/5/13.
 */

/**
 * 展示添加结果的适配器
 */
public class ModelAdapter extends BaseAdapter {
    private Context context;
    private List<ModuleVo> tasks;
    private ViewGroup.LayoutParams params;
    private List<ModuleVo> select = new ArrayList<ModuleVo>();
    private boolean flag;

    public ModelAdapter(Context context, List<ModuleVo> tasks, boolean flag) {
        this.context = context;
        this.tasks = tasks;
        this.flag = flag;
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
            convertView = View.inflate(context, R.layout.item_add_visit, null);
            viewHolder = new ViewHolder();
            viewHolder.titleName = (TextView) convertView.findViewById(R.id.task_name);
            viewHolder.content = (MListView) convertView.findViewById(R.id.list_content);
            viewHolder.layoutName = (LinearLayout) convertView.findViewById(R.id.layout_name);
            viewHolder.layout = (FrameLayout) convertView.findViewById(R.id.layout);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final ModuleVo vo = tasks.get(position);
        if (vo.getQuestionVos().size() > 0) {
            viewHolder.titleName.setText(Html.fromHtml(vo.getName() + "&#160;&#160;<font color = '#308df7' >" + "(" + vo.getQuestionVos().size() + ")" + "</font>"));
        } else {
            viewHolder.titleName.setText(Html.fromHtml(vo.getName() + "&#160;&#160;(" + vo.getQuestionVos().size() + ")"));
        }

        VisitAdapter mContentAdpter = new VisitAdapter(context, tasks.get(position), tasks.get(position).getQuestionVos(), new VisitAdapter.CheckClickListener() {
            @Override
            public void afterPositive(ModuleVo vo) {
                if (select.contains(vo)) {
                    select.remove(vo);
                    notifyDataSetChanged();
                }
            }

            @Override
            public void notifyParent() {
                notifyDataSetChanged();
            }
        }, flag);
        viewHolder.content.setAdapter(mContentAdpter);
        params = viewHolder.layout.getLayoutParams();
        if (tasks.get(position).getQuestionVos().size() > 0) {
            params.height = ViewHeightUtils.setListViewHeightBasedOnChildren1(viewHolder.content, context) + tasks.get(position).getQuestionVos().size() * ViewUtil.dpToPx(5, context);
        } else {
            params.height = 0;
        }
        viewHolder.layout.setLayoutParams(params);
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
        public MListView content;
        public FrameLayout layout;
        public LinearLayout layoutName;
    }

}
