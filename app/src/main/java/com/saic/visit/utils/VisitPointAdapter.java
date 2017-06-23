package com.saic.visit.utils;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.saic.visit.R;
import com.saic.visit.model.ItemVo;

import java.util.List;


/**
 * Created by liuhui on 2016/5/13.
 */
public class VisitPointAdapter extends BaseAdapter {
    private Context context;
    private List<ItemVo> data;
    Drawable drawableSelect;
    Drawable drawableUnSelect;
    private CheckClickListener listener;
    private int index = -1;
    boolean flag;

    public VisitPointAdapter(Context context, List<ItemVo> data, CheckClickListener listener, boolean flag) {
        this.context = context;
        this.data = data;
        drawableSelect = context.getResources().getDrawable(R.drawable.check_box_on);
        /// 这一步必须要做,否则不会显示.
        drawableSelect.setBounds(0, 0, drawableSelect.getMinimumWidth(), drawableSelect.getMinimumHeight());
        drawableUnSelect = context.getResources().getDrawable(R.drawable.check_box_off);
        drawableUnSelect.setBounds(0, 0, drawableUnSelect.getMinimumWidth(), drawableUnSelect.getMinimumHeight());
        this.listener = listener;
        this.flag = flag;
//        listener.afterPositive(data);
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
            convertView = View.inflate(context, R.layout.item_visit_point, null);
            viewHolder = new ViewHolder();
            viewHolder.taskName = (TextView) convertView.findViewById(R.id.item_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
  /*      if (data.get(position).ischeck()) {
            viewHolder.taskName.setCompoundDrawables(drawableSelect, null, null, null);
            data.get(position).setIscheck(truke);
        } else {
            viewHolder.taskName.setCompoundDrawables(drawableUnSelect, null, null, null);
            data.get(position).setIscheck(false);
        }*/
        //  viewHolder.taskName.setText(data.get(position).getName());
 /*       convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag) {
                    int exsitCheckSize = 0;
                    int pos = -1;
                    for (int i = 0, len = data.size(); i < len; i++) {
                        if (data.get(i).ischeck()) {
                            exsitCheckSize = exsitCheckSize + 1;
                            pos = i;
                        }
                    }
                    if (exsitCheckSize == 1 && pos == position) {
                        ToastUtil.show(context, "检查点至少选择一项");
                    } else {
                        if (data.get(position).ischeck()) {
                            data.get(position).setIscheck(false);
                        } else {
                            data.get(position).setIscheck(true);
                        }
                        listener.afterPositive(data);
                        notifyDataSetChanged();
                    }

                }


            }
        });*/

        return convertView;
    }

    class ViewHolder {
        public TextView taskName;
    }

    public interface CheckClickListener {
        /**
         * 监听选中或没选中
         */
        void afterPositive(List<ItemVo> itemVos);
    }

}
