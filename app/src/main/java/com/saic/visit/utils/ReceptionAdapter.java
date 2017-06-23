package com.saic.visit.utils;


import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.saic.visit.R;
import com.saic.visit.activity.ReceptionActivity;
import com.saic.visit.activity.UserInfoActivity;
import com.saic.visit.constant.Constants;
import com.saic.visit.model.ReceptionistTypeVo;

import java.util.ArrayList;
import java.util.List;

public class ReceptionAdapter extends BaseAdapter {
    private Context context;
    private List<ReceptionistTypeVo> tasks;

    public ReceptionAdapter(Context context, List<ReceptionistTypeVo> tasks) {
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
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_reception, null);
            viewHolder = new ViewHolder();
            viewHolder.receptionTitle = (TextView) convertView.findViewById(R.id.reception_title);
            viewHolder.receptionName = (TextView) convertView.findViewById(R.id.reception_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ReceptionistTypeVo vo = tasks.get(position);
        viewHolder.receptionTitle.setText(vo.getName());
        if(!StringUtils.isEmpty(tasks.get(position).getReceptionistName())){
            viewHolder.receptionName.setText(tasks.get(position).getReceptionistName());
            viewHolder.receptionName.setTextColor(context.getResources().getColor(R.color.white));
            viewHolder.receptionName.setBackgroundResource(R.drawable.blue_button);
        }else{
            viewHolder.receptionName.setText(context.getResources().getText(R.string.reception_no_record));
            viewHolder.receptionName.setTextColor(context.getResources().getColor(R.color.black));
            viewHolder.receptionName.setBackgroundResource(R.drawable.gray_button);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserInfoActivity.class);
                intent.putExtra(Constants.USERINFO,tasks.get(position));
                intent.putExtra(Constants.USERINDEX,position);
                ((ReceptionActivity)context).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                ((ReceptionActivity)context).startActivityForResult(intent, 100);
                ((ReceptionActivity)context).overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
            }
        });
        return convertView;
    }

    class ViewHolder {
        public TextView receptionTitle;
        public TextView receptionName;

    }

}
