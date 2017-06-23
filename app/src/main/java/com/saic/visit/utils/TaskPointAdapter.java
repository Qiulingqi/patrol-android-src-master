package com.saic.visit.utils;


import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.saic.visit.R;
import com.saic.visit.activity.AddShopVisitActivity;
import com.saic.visit.model.CheckPointVo;
import com.saic.visit.model.ItemVo;
import com.saic.visit.widget.MListView;
import com.saic.visit.widget.MTextViewTask;
import com.saic.visit.widget.NormalDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuhui on 2016/5/13.
 */
public class TaskPointAdapter extends BaseAdapter {
    private Context context;
    private List<CheckPointVo> tasks;
    private List<CheckPointVo> select = new ArrayList<CheckPointVo>();
    private List<ItemVo> items = new ArrayList<ItemVo>();
    private ViewGroup.LayoutParams params;
    CheckClickListener listener;
    List<CheckPointVo> list = new ArrayList<CheckPointVo>();
    private int index = -1;

    public TaskPointAdapter(Context context, List<CheckPointVo> tasks, CheckClickListener listener) {
        this.context = context;
        this.tasks = tasks;
        this.listener = listener;
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
            convertView = View.inflate(context, R.layout.item_point, null);
            viewHolder = new ViewHolder();
            viewHolder.titleName = (MTextViewTask) convertView.findViewById(R.id.item_name);
            viewHolder.txtDes = (TextView) convertView.findViewById(R.id.txt_des);
            viewHolder.imgCheck = (ImageView) convertView.findViewById(R.id.img_check);
            viewHolder.txtReason = (TextView) convertView.findViewById(R.id.txt_reason);
            viewHolder.content = (MListView) convertView.findViewById(R.id.list_content);
            viewHolder.layout = (FrameLayout) convertView.findViewById(R.id.layout);
            viewHolder.editRemark = (EditText) convertView.findViewById(R.id.edit_remark);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.titleName.setText(tasks.get(position).getName());
        if (select.contains(tasks.get(position))) {
            viewHolder.imgCheck.setImageResource(R.drawable.check_blue_small);
        } else {
            viewHolder.imgCheck.setImageResource(R.drawable.check_tran_small);
        }
        viewHolder.layout.setVisibility(select.contains(tasks.get(position)) ? View.VISIBLE : View.GONE);
        viewHolder.txtReason.setVisibility(select.contains(tasks.get(position)) ? View.VISIBLE : View.GONE);
        viewHolder.editRemark.setVisibility(select.contains(tasks.get(position)) ? View.VISIBLE : View.GONE);
        PointContentAdapter mContentAdpter = new PointContentAdapter(context, tasks.get(position).getItemVos(), new PointContentAdapter.CheckClickListener() {
            @Override
            public void afterPositive(List<ItemVo> itemVos) {
                items = itemVos;
                if (list.size() > 0) {
                    listener.afterPositive(list);
                    notifyDataSetChanged();
                }
            }
        });
        viewHolder.content.setAdapter(mContentAdpter);
        mContentAdpter.notifyDataSetChanged();
        params = viewHolder.layout.getLayoutParams();
        viewHolder.editRemark.setText(tasks.get(position).getRemark());
        params.height = ViewHeightUtils.setListViewHeightBasedOnChildren1(viewHolder.content, context);
        viewHolder.layout.setLayoutParams(params);
        viewHolder.editRemark.setTag(position);
        viewHolder.editRemark.addTextChangedListener(new MyTextWatcher(viewHolder));
        viewHolder.editRemark.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e("touch","touchevent:"+event.getAction());
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    index = (Integer) v.getTag();
                }
                if (v.getId() == viewHolder.editRemark.getId()) {
                    v.getParent().requestDisallowInterceptTouchEvent(event.getAction() == MotionEvent.ACTION_UP ? false : true);
                }
                return false;
            }
        });
        viewHolder.txtDes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NormalDialog nd = new NormalDialog(context, tasks.get(position).getDesc(), "说明");
                nd.show();
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (select.contains(tasks.get(position))) {
                    list.clear();
                    list.add(tasks.get(position));
                    select.clear();
                    select.add(tasks.get(position));
                } else {
                    list.clear();
                    list.add(tasks.get(position));
                    select.clear();
                    select.add(tasks.get(position));
                    for (int i = 0; i < tasks.size(); i++) {
                        //清空其他项
                        if (i != position) {
                            for (int j = 0; j < tasks.get(position).getItemVos().size(); j++) {
                                tasks.get(position).getItemVos().get(j).setIscheck(false);
                                tasks.get(position).setRemark("");
                            }
                        }

                    }
                }
                if (select.size() > 0) {
                    ((AddShopVisitActivity) context).setSelect(true);
                } else {
                    ((AddShopVisitActivity) context).setSelect(false);
                }
                if (select.size() > 0) {
                    listener.afterPositive(list);
                }
                notifyDataSetChanged();
                ((AddShopVisitActivity) context).setHeight();

            }
        });


        return convertView;
    }

    class ViewHolder {
        public MTextViewTask titleName;
        public TextView txtDes;
        public ImageView imgCheck;
        public TextView txtReason;
        public MListView content;
        public FrameLayout layout;
        public EditText editRemark;
    }

    public interface CheckClickListener {
        /**
         * 监听选中或没选中
         */
        void afterPositive(List<CheckPointVo> select);

        void afterPositive(String remark);
    }

    class MyTextWatcher implements TextWatcher {

        private ViewHolder mHolder;

        public MyTextWatcher(ViewHolder holder) {
            mHolder = holder;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start,
                                      int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start,
                                  int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s != null && !"".equals(s)) {
                int position = (Integer) mHolder.editRemark.getTag();
                tasks.get(position).setRemark(s.toString());
                listener.afterPositive(s.toString());
            }
        }


    }
}
