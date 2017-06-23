package com.saic.visit.utils;

import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.saic.visit.R;
import com.saic.visit.model.CheckPointVo;
import com.saic.visit.model.ItemVo;
import com.saic.visit.model.Node;
import com.saic.visit.widget.ClearWatcherEditText;
import com.saic.visit.widget.MyGridView;
import com.saic.visit.widget.NormalDialog;

import java.util.List;
import java.util.Map;


public class VisitTreeAdapter<T> extends TreeListViewAdapter<T> {
    private Context context;
    private ListView mTree;
    private Map<Integer, String> descs;
    private TextView tv;
    private List<ItemVo> itemVos;
    private MyGridView firstView;
    private int i = 0;
    private AddPhoto addPhoto;
    private ItemVo itemVo;
    private CheckPointVo checkPointVo;
    private String nameCode;
    private String pointCode;
    private ViewHolder viewHolder;
    private String login="0";


    public VisitTreeAdapter(ListView mTree, Context context, List<T> datas,
                            int defaultExpandLevel, AddPhoto addPhoto) throws IllegalArgumentException,
            IllegalAccessException {
        super(mTree, context, datas, defaultExpandLevel);
        this.context = context;
        this.mTree = mTree;
        this.addPhoto = addPhoto;
    }

    @Override
    public View getConvertView(final Node node, final int position, View convertView, ViewGroup parent) {

        switch (getItemViewType(position)) {
            case 0:
                viewHolder = null;
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
                    viewHolder.addflag = (TextView) convertView.findViewById(R.id.addflag);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }



                if (node.getType() == 4) {
                    viewHolder.icon.setVisibility(View.VISIBLE);
                    viewHolder.desc.setVisibility(View.VISIBLE);
                    viewHolder.content.setVisibility(View.VISIBLE);

                    if (null != node.getChildren()){
//                   if ("1".equals(node.getChildren().get(0).getAddFlag())&&"1".equals(login)){
//                        viewHolder.addflag.setVisibility(View.VISIBLE);
//                    }

                        if (node.getChildren().get(0).getCheckPointVo().getItemVos().get(0).getImageURLIst().size() != 0) {
                            viewHolder.addflag.setVisibility(View.VISIBLE);
                        }else{
                            viewHolder.addflag.setVisibility(View.GONE);
                        }

                    }
                } else {
                    viewHolder.icon.setVisibility(View.GONE);
                    viewHolder.desc.setVisibility(View.GONE);
                    viewHolder.content.setVisibility(View.GONE);
                    viewHolder.addflag.setVisibility(View.GONE);
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
                            NormalDialog nd = new NormalDialog(context, node.getDesc(), "说明");
                            nd.show();
                        }
                    });
//            viewHolder.label.setText(Html.fromHtml("<font color = '#8C8C8C' size = '16'>" + item1 + "</font>" + "<font color = '#AAAAAA' size = '8'>" + item2 + "</font>"));
                }
                break;
            case 1:
                PointHolder pointHolder = null;
                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.item_visit_checkpoint, parent, false);
                    pointHolder = new PointHolder();
                    pointHolder.ll = (LinearLayout) convertView.findViewById(R.id.ll_itemvos);
                    pointHolder.remark = (ClearWatcherEditText) convertView.findViewById(R.id.edit_remark);
                    convertView.setTag(pointHolder);
                } else {
                    pointHolder = (PointHolder) convertView.getTag();
                }
                pointHolder.remark.clearTextChangedListener();
                pointHolder.remark.setText(node.getCheckPointVo().getRemark());
                pointHolder.remark.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        node.getCheckPointVo().setRemark(charSequence.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                itemVos = node.getCheckPointVo().getItemVos();
                itemVo = itemVos.get(0);
                /**
                 * 如果有图片  就显示已添加
                 */
                /*if (itemVo.getImageURLIst().size() > 0){
                    viewHolder.addflag.setVisibility(View.VISIBLE);
                }*/
                // nameCode = itemVo.getName();
                checkPointVo = node.getCheckPointVo();
                pointCode = checkPointVo.getPointCode();


                // String name = checkPointVo.getName();
                pointHolder.ll.removeAllViewsInLayout();
                //for (final ItemVo itemVo : itemVos) {
                View v = mInflater.inflate(R.layout.item_visit_point_content, pointHolder.ll, false);
                firstView = (MyGridView) v.findViewById(R.id.firstView);
                GridViewAdapter gridViewAdapter = new GridViewAdapter(itemVo.getImageURLIst(), context);
                firstView.setAdapter(gridViewAdapter);
                gridViewAdapter.notifyDataSetChanged();

                final TextView cb = (TextView) v.findViewById(R.id.cb_item);

                cb.setOnClickListener(new View.OnClickListener() {

                    private Toast toast;

                    @Override
                    public void onClick(View v) {
                        if (pointCode == node.getCheckPointVo().getPointCode()) {
                            addPhoto.getPhote(pointCode);
                             node.setAddFlag("1");
                        } else {
                            toast = Toast.makeText(context, "操作过于频繁，请等待数据刷新....", Toast.LENGTH_SHORT);
                            toast.show();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    toast.cancel();
                                }
                            }, 1000);
                            notifyDataSetChanged();
                        }
                    }
                });
                pointHolder.ll.addView(v, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        return convertView;
    }

    public void setBoymap(String bitmap, String string) {
        login="1";
        itemVo.getImageURLIst().add(bitmap);
        itemVo.getImageUri().add(string);
    }

    private final class ViewHolder {
        ImageView icon;
        TextView label;
        ImageView iconLeft;
        ImageView iconRight;
        TextView desc;
        TextView content;
        LinearLayout lastItem;
        TextView addflag;
    }

    private final class PointHolder {
        LinearLayout ll;
        ClearWatcherEditText remark;
    }

}
