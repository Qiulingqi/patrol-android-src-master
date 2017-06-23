package com.saic.visit.utils;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.saic.visit.R;
import com.saic.visit.model.Node;
import com.saic.visit.widget.NormalDialog;

import java.util.List;
import java.util.Map;


public class SimpleTreeAdapter<T> extends TreeListViewAdapter<T> {
    private Context context;
    private ListView mTree;
    private Map<Integer, String> descs;

    public SimpleTreeAdapter(ListView mTree, Context context, List<T> datas,
                             int defaultExpandLevel) throws IllegalArgumentException,
            IllegalAccessException {
        super(mTree, context, datas, defaultExpandLevel);
        this.context = context;
        this.mTree = mTree;

    }

    @Override
    public View getConvertView(final Node node, int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_list, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.icon = (ImageView) convertView
                    .findViewById(R.id.id_treenode_icon);
            viewHolder.iconLeft = (ImageView) convertView
                    .findViewById(R.id.id_treenode_left);
            viewHolder.iconRight = (ImageView) convertView
                    .findViewById(R.id.id_treenode_right);
            viewHolder.label = (TextView) convertView
                    .findViewById(R.id.id_treenode_label);
            viewHolder.desc = (TextView) convertView.findViewById(R.id.id_treenode_desc);
            viewHolder.content = (TextView) convertView.findViewById(R.id.id_treenode_content);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
//        if (node.getIcon() == -1) {
//            viewHolder.icon.setVisibility(View.GONE);
//        } else {

        if (node.getType() == 4) {
            viewHolder.icon.setVisibility(View.VISIBLE);
            viewHolder.desc.setVisibility(View.VISIBLE);
            viewHolder.content.setVisibility(View.VISIBLE);
        } else {
            viewHolder.icon.setVisibility(View.GONE);
            viewHolder.desc.setVisibility(View.GONE);
            viewHolder.content.setVisibility(View.GONE);
        }
        if (node.getType() == 2) {
            convertView.setBackgroundResource(R.color.bg_gray);
        } else {
            convertView.setBackgroundResource(R.color.white);
        }
        viewHolder.icon.setImageResource(R.drawable.blue_circle);
        if (node.getType() == 1 || node.getType() == 2) {
            viewHolder.label.setTextColor(context.getResources().getColor(R.color.black));
            viewHolder.label.setTextSize(18);
        } else {
            viewHolder.label.setTextColor(context.getResources().getColor(R.color.main_item_time));
            viewHolder.label.setTextSize(16);
        }
        if (node.getType() == 1 && node.isExpand()) {
            viewHolder.iconLeft.setVisibility(View.VISIBLE);
            viewHolder.label.setTextColor(context.getResources().getColor(R.color.main_blue));
            viewHolder.iconRight.setImageResource(R.drawable.score_expand);
        } else {
            viewHolder.iconLeft.setVisibility(View.INVISIBLE);
            viewHolder.iconRight.setImageResource(R.drawable.score_close);
        }
        if (node.getType() == 1) {
            viewHolder.iconRight.setVisibility(View.VISIBLE);
        } else {
            viewHolder.iconRight.setVisibility(View.GONE);
        }
        if (node.getType() != 4) {
            viewHolder.label.setText(node.getName());
        } else {
            String item1 = "";
            String item2 = "";
            int length = node.getName().split("@").length;
            String[] split = node.getName().split("@");
            for (int i = 0; i < length; i++) {
                if (i == 0) {
                    item1 = split[i];
                } else {
                    if (i != length - 1) {
                        item2 = item2 + split[i] + "\n";
                    } else {
                        item2 = item2 + split[i];
                    }

                }
            }
            if (node.getDesc() == null) node.setDesc("描述为空");
            viewHolder.label.setText(item1);
            viewHolder.content.setText(item2);
            viewHolder.desc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("thisisfan","shuju"+node.toString());
                    NormalDialog nd = new NormalDialog(context, node.getDesc(), "说明");
                    nd.show();
                }
            });
//            viewHolder.label.setText(Html.fromHtml("<font color = '#8C8C8C' size = '16'>" + item1 + "</font>" + "<font color = '#AAAAAA' size = '8'>" + item2 + "</font>"));
        }


        return convertView;
    }

    private final class ViewHolder {
        ImageView icon;
        TextView label;
        ImageView iconLeft;
        ImageView iconRight;
        TextView desc;
        TextView content;
    }

}
