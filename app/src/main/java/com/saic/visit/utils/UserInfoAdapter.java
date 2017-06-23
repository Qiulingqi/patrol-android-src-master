package com.saic.visit.utils;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.saic.visit.R;
import com.saic.visit.activity.ScoreActivity;
import com.saic.visit.activity.UserInfoActivity;
import com.saic.visit.model.Receptionist;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by liuhui on 2016/5/13.
 */
public class UserInfoAdapter extends BaseAdapter implements Filterable {
    private Context context;
    private List<Receptionist> data;
    private List<Receptionist> mUnfilteredData;
    private ArrayFilter mFilter;
    public UserInfoAdapter(Context context, List<Receptionist> data) {
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
            convertView = View.inflate(context, R.layout.item_model, null);
            viewHolder = new ViewHolder();
            viewHolder.taskName = (TextView) convertView.findViewById(R.id.model_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.taskName.setText(data.get(position).getName());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((UserInfoActivity)context).initData(data.get(position));
                notifyDataSetInvalidated();
            }
        });
        return convertView;
    }

    @Override
    public android.widget.Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
    }
    private class ArrayFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (mUnfilteredData == null) {
                mUnfilteredData = new ArrayList<Receptionist>(data);
            }

            if (prefix == null || prefix.length() == 0) {
                List<Receptionist> list = mUnfilteredData;
                results.values = list;
                results.count = list.size();
            } else {
                String prefixString = prefix.toString().toLowerCase();

                List<Receptionist> unfilteredValues = mUnfilteredData;
                int count = unfilteredValues.size();

                ArrayList<Receptionist> newValues = new ArrayList<Receptionist>(count);

                for (int i = 0; i < count; i++) {
                    Receptionist pc = unfilteredValues.get(i);
                    if (pc != null) {
                        if(pc.getName()!=null && pc.getName().startsWith(prefixString)){
                            newValues.add(pc);
                        }
                    }
                }

                results.values = newValues;
                results.count = newValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            data = (ArrayList<Receptionist>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }

    class ViewHolder {
        public TextView taskName;
    }

}
