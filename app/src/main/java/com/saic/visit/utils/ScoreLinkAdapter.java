package com.saic.visit.utils;


import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.saic.visit.R;
import com.saic.visit.model.CatalogVo;
import com.saic.visit.model.ItemVo;
import com.saic.visit.model.QuestionVo;
import com.saic.visit.widget.MListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuhui on 2016/5/13.
 */
public class ScoreLinkAdapter extends BaseAdapter {
    private Context context;
    private List<CatalogVo> tasks;
    private List<CatalogVo> select = new ArrayList<CatalogVo>();
    private ViewGroup.LayoutParams params;
    private String index ;
    public ScoreLinkAdapter(Context context, List<CatalogVo> tasks,String index) {
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
            convertView = View.inflate(context, R.layout.item_score_link, null);
            viewHolder = new ViewHolder();
            viewHolder.visitLink = (TextView) convertView.findViewById(R.id.visit_link);
            viewHolder.content = (ListView) convertView.findViewById(R.id.list_content);
            viewHolder.layout = (LinearLayout) convertView.findViewById(R.id.layout);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final CatalogVo vo = tasks.get(position);
        viewHolder.visitLink.setText(index + (position + 1) + vo.getName());
        ScoreItemAdapter mContentAdpter = new ScoreItemAdapter(context,vo.getCatalogVos(),index + (position + 1)+".");
        viewHolder.content.setAdapter(mContentAdpter);
//        mContentAdpter.notifyDataSetChanged();
//        params = viewHolder.layout.getLayoutParams();
//        params.height = ViewHeightUtils.setListViewHeightBasedOnChildren1(viewHolder.content, context);
//        viewHolder.layout.setLayoutParams(params);
        return convertView;
    }

    class ViewHolder {
        public TextView visitLink;
        public ListView content;
        public LinearLayout layout;
    }



}
