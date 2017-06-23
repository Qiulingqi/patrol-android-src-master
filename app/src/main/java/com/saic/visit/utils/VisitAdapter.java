package com.saic.visit.utils;


import android.animation.ObjectAnimator;
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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.saic.visit.R;
import com.saic.visit.activity.MyApplication;
import com.saic.visit.constant.Constants;
import com.saic.visit.model.ItemVo;
import com.saic.visit.model.LogRequest;
import com.saic.visit.model.ModuleVo;
import com.saic.visit.model.QuestionVo;
import com.saic.visit.model.QuestionsLog;
import com.saic.visit.model.TaskDetailResponse;
import com.saic.visit.utils.excelutil.Order;
import com.saic.visit.widget.MListView;
import com.saic.visit.widget.MTextViewItemQuestion;
import com.saic.visit.widget.MTextViewItemQuestionOther;
import com.saic.visit.widget.MyGridView;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuhui on 2016/5/13.
 */

/**
 * 这里是展示的子布局
 */
public class VisitAdapter extends BaseAdapter {
    private Context context;
    private List<QuestionVo> tasks;
    private List<QuestionVo> select = new ArrayList<QuestionVo>();
    private ViewGroup.LayoutParams params;
    private List<ItemVo> itemVosList = new ArrayList<ItemVo>();
    private int index = -1;
    private CheckClickListener listener;
    private ModuleVo moduleVo;
    private TextView text;
    /**
     * type == 1显示编辑 type == 2 显示保存 type == 3显示删除
     */
    private int type = 1;
    private boolean flag;
    private String taskId;
    private Gson gsonss;
    private LogRequest detailsLog;
    private List<String> itemVos1;
    private List<List<String>> data = new ArrayList<>();
    private List<String> notEditItemVos11;
    private Boolean flag1;
    private GridViewAdapter gridViewAdapter;
    // 表格的序号
    private int xuhao = 0;
    private DbManager db;

    public VisitAdapter(Context context, ModuleVo vo, List<QuestionVo> tasks, CheckClickListener listener, boolean flag) {
        this.context = context;
        this.tasks = tasks;
        this.listener = listener;
        this.moduleVo = vo;
        this.flag = flag;
        initDatas(context);
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
            convertView = View.inflate(context, R.layout.item_question, null);
            viewHolder = new ViewHolder();
            viewHolder.visitLink = (MTextViewItemQuestion) convertView.findViewById(R.id.visit_link);
            viewHolder.layoutItem = (LinearLayout) convertView.findViewById(R.id.layout_item);
            viewHolder.visitItem = (MTextViewItemQuestionOther) convertView.findViewById(R.id.visit_item);
            viewHolder.editRemark = (EditText) convertView.findViewById(R.id.edit_remark);
            // viewHolder.content = (MListView) convertView.findViewById(R.id.list_content);
            viewHolder.gview = (MyGridView) convertView.findViewById(R.id.gview);
            // viewHolder.GridHeight = (LinearLayout) convertView.findViewById(GridHeight);
            viewHolder.layout = (FrameLayout) convertView.findViewById(R.id.layout);
            viewHolder.relaRight = (RelativeLayout) convertView.findViewById(R.id.rela_right);
            viewHolder.imgEdit = (ImageView) convertView.findViewById(R.id.img_edit);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final QuestionVo vo = tasks.get(position);
        viewHolder.editRemark.setText(vo.getRemark());
        if (flag) {
            viewHolder.editRemark.setEnabled(false);
            viewHolder.imgEdit.setVisibility(View.GONE);
        }
        viewHolder.layoutItem.setVisibility(select.contains(vo) ? View.GONE : View.VISIBLE);
        System.out.println("this is xxx"+tasks.get(position).getType());
        if (tasks.get(position).getType() == 2) {
            viewHolder.imgEdit.setImageResource(R.drawable.save);
            viewHolder.editRemark.setEnabled(true);
        } else if (tasks.get(position).getType() == 3) {
            viewHolder.imgEdit.setImageResource(R.drawable.close);
            ObjectAnimator.ofFloat(viewHolder.imgEdit, "alpha", 0, 1).setDuration(1000).start();
            viewHolder.editRemark.setEnabled(false);
        } else {
            viewHolder.imgEdit.setImageResource(R.drawable.edit);
            viewHolder.editRemark.setEnabled(false);
        }

        viewHolder.visitLink.setText(vo.getCatalogVos().get(0).getName() + context.getResources().getText(R.string.point) + vo.getCatalogVos().get(0).getCatalogVos().get(0).getName());
        viewHolder.visitItem.setText(vo.getCatalogVos().get(0).getCatalogVos().get(0).getCheckPointVos().get(0).getName());
        //System.out.println("this is println"+vo.getCatalogVos().get(0).getCatalogVos().get(0).getCheckPointVos().get(0).getName());
        List<ItemVo> itemVos = vo.getCatalogVos().get(0).getCatalogVos().get(0).getCheckPointVos().get(0).getItemVos();
        if (!vo.isEdit()) {
            List<String> notEditItemVos = new ArrayList<>();
            List<String> notEditItemVos1 = new ArrayList<>();
            for (ItemVo itemVo : itemVos) {
                List<String> imageURLIst = itemVo.getImageURLIst();
                List<String> imageUri = itemVo.getImageUri();
                if (itemVo.getImageUri().size() > 0) {
                    notEditItemVos1.addAll(imageUri);
                }
                if (itemVo.getImageURLIst().size() > 0)
                    notEditItemVos.addAll(imageURLIst);
            }
            // itemVos = notEditItemVos;
            itemVos1 = notEditItemVos;
            data.add(itemVos1);
            notEditItemVos11 = notEditItemVos1;
        }

        String pointCode = vo.getCatalogVos().get(0).getCatalogVos().get(0).getCheckPointVos().get(0).getPointCode();
        String name = vo.getCatalogVos().get(0).getCatalogVos().get(0).getCheckPointVos().get(0).getName();
        String desc=vo.getCatalogVos().get(0).getCatalogVos().get(0).getCheckPointVos().get(0).getRemark();
        System.out.println("this is out____________"+desc);
        String modelName = vo.getCatalogVos().get(0).getModelName();

        String s = StringUtil.ListToString(notEditItemVos11);
        flag1 = false;
        db = MyApplication.initDbSqlite();

        for (int i = 0; i < MyApplication.excelList2.size(); i++) {
            if (notEditItemVos11.get(0).equals(MyApplication.excelList2.get(i).get(0))) {
                MyApplication.excelList.get(i).receiverAddr = s;

                try {
                    List<Order> all = db.findAll(Order.class);
                    all.get(i).receiverAddr = s;
                    db.update(all);
                    //    db.save(all);
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        }

        for (int i = 0; i < MyApplication.excelList.size(); i++) {
            if (s.equals(MyApplication.excelList.get(i).receiverAddr)) {
                flag1 = true;
            }
        }
        if (flag1 == false) {
            if (null != MyApplication.excelList) {
                if (MyApplication.excelList.size() > 1) {
                    Order order = MyApplication.excelList.get(MyApplication.excelList.size() - 1);
                    xuhao = Integer.parseInt(order.xuhao);
                    xuhao++;
                }
            }

            MyApplication.excelList.add(new Order(xuhao + "", pointCode, "照片", "否", name, s, desc));
            try {
                db.save(new Order(xuhao + "", pointCode, "照片", "否", name, s, desc));
            } catch (Exception e) {
                e.printStackTrace();
            }
            MyApplication.excelList2.add(notEditItemVos11);
            xuhao++;
            // 增加一步   将当前数据库的所有文件对象、
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < MyApplication.excelList.size(); i++) {
                String receiverAddr = MyApplication.excelList.get(i).receiverAddr;
                sb.append(i+"----"+receiverAddr);
            }
            String ss = sb.toString();
            String jingXiaoShang = MyApplication.JingXiaoShang;
            String jingXiaoCode = MyApplication.JingXiaoCode;
            String fileName = jingXiaoShang + jingXiaoCode + "__Eachoperation_log.txt";
            FileHelper.writeTxtToFile(ss, MyApplication.filePath, fileName);
        }
        /* gridViewAdapter = new GridViewAdapter(itemVos1, context);
           viewHolder.gview.setAdapter(gridViewAdapter);
           gridViewAdapter.notifyDataSetChanged();*/
        /**
         * 当点击的时候再去展示图片
         */
        if (itemVos1.size() > 2) {
            viewHolder.layoutItem.setMinimumHeight((itemVos1.size() / 2) * 500);
        } else {
            viewHolder.layoutItem.setMinimumHeight(500);
        }
        viewHolder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.layoutItem.setVisibility(View.VISIBLE);
                List<String> strings = data.get(position);
                gridViewAdapter = new GridViewAdapter(strings, context);
                viewHolder.gview.setAdapter(gridViewAdapter);
                gridViewAdapter.notifyDataSetChanged();
            }
        });
        VisitPointAdapter mContentAdpter = new VisitPointAdapter(context, itemVos, new VisitPointAdapter.CheckClickListener() {
            @Override
            public void afterPositive(List<ItemVo> itemVos) {
                if (itemVos != null) {
                    itemVosList = itemVos;
                }

            }
        }, vo.isEdit());
        itemVosList = itemVos;
//        viewHolder.check.initData(vo.getCatalogVos().get(0).getCatalogVos().get(0).getCheckPointVos().get(0).getItemVos(), new CheckBoxAndEditTextView.CheckClickListener() {
//            @Override
//            public void afterPositive(List<ItemVo> data) {
//                vo.getModuleVo().getCatalogVos().get(0).getCheckPointVos().get(0).setItemVos(data);
//                String taskId = SharePreferenceUtil.getStringValue(Constants.TASKID, context);
//                Gson gsonss = new Gson();
//                String taskListResponseStr = SharePreferenceUtil.getStringValue(Constants.TASKDETAIL + taskId, context);
//                String modelStr = SharePreferenceUtil.getStringValue(Constants.MODEL + taskId, context);
//                TaskDetailResponse taskListResponse = gsonss.fromJson(taskListResponseStr, TaskDetailResponse.class);
//                TaskDetailResponse model = gsonss.fromJson(modelStr, TaskDetailResponse.class);
//                taskListResponse.models = model.models;
//                taskListResponse.survey.setQuestionVos(tasks);
//                SharePreferenceUtil.save(Constants.TASKDETAIL + taskId, gsonss.toJson(taskListResponse), context);
//            }
//        },false);
        //  viewHolder.content.setAdapter(mContentAdpter);
        //  mContentAdpter.notifyDataSetChanged();
//        params = viewHolder.layout.getLayoutParams();
//        if (vo.getCatalogVos().get(0).getCatalogVos().get(0).getCheckPointVos().get(0).getItemVos().size() > 0) {
//            params.height = ViewHeightUtils.setListViewHeightBasedOnChildren1(viewHolder.content, context) + ViewUtil.dpToPx(22, context);
//        } else {
//            params.height = 0;
//        }
        // params.height = ViewHeightUtils.setListViewHeightBasedOnChildren1(viewHolder.content, context);
        viewHolder.editRemark.setTag(position);
        viewHolder.editRemark.addTextChangedListener(new MyTextWatcher(viewHolder));
        viewHolder.editRemark.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    index = (Integer) v.getTag();
                }
                return false;
            }
        });
        viewHolder.relaRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tasks.get(position).getType() == 2) {
                    tasks.get(position).setIsEdit(false);
                    vo.getCatalogVos().get(0).getCatalogVos().get(0).getCheckPointVos().get(0).setItemVos(itemVosList);
                    vo.getCatalogVos().get(0).getCatalogVos().get(0).getCheckPointVos().get(0).setRemark(tasks.get(position).getRemark());
                    String taskListResponseStr = SharePreferenceUtil.getStringValue(Constants.TASKDETAIL + taskId, context);
                    String modelStr = SharePreferenceUtil.getStringValue(Constants.MODEL + taskId, context);
                    TaskDetailResponse taskListResponse = gsonss.fromJson(taskListResponseStr, TaskDetailResponse.class);
                    TaskDetailResponse model = gsonss.fromJson(modelStr, TaskDetailResponse.class);
                    taskListResponse.models = model.models;
                    for (int i = 0; i < taskListResponse.survey.getModuleVos().size(); i++) {
                        if (taskListResponse.survey.getModuleVos().get(i).getId().longValue() == vo.getModuleId().longValue()) {
                            tasks.get(position).setType(1);
                            //保存修改的信息
                            saveLog(taskListResponse.survey.getModuleVos().get(i).getName(), vo, "修改扣分原因");
                            initDatas(context);
                            taskListResponse.survey.getModuleVos().get(i).setQuestionVos(tasks);
                            SharePreferenceUtil.save(Constants.TASKDETAIL + taskId, gsonss.toJson(taskListResponse), context);
                            notifyChange();
                            return;
                        }
                    }
                } else if (tasks.get(position).getType() == 3) {
                    //删除
                    String taskId = SharePreferenceUtil.getStringValue(Constants.TASKID, context);
                    String taskListResponseStr = SharePreferenceUtil.getStringValue(Constants.TASKDETAIL + taskId, context);
                    String modelStr = SharePreferenceUtil.getStringValue(Constants.MODEL + taskId, context);
                    TaskDetailResponse taskListResponse = gsonss.fromJson(taskListResponseStr, TaskDetailResponse.class);
                    TaskDetailResponse model = gsonss.fromJson(modelStr, TaskDetailResponse.class);
                    taskListResponse.models = model.models;
                    for (int i = 0; i < taskListResponse.survey.getModuleVos().size(); i++) {
                        if (taskListResponse.survey.getModuleVos().get(i).getId().longValue() == vo.getModuleId().longValue()) {
                            //保存删除的信息
                            saveLog(taskListResponse.survey.getModuleVos().get(i).getName(), vo, "删除扣分项");
                            initDatas(context);
                            tasks.get(position).setIsEdit(false);
                            select.remove(vo);
                            tasks.remove(vo);
                            listener.afterPositive(moduleVo);
                            taskListResponse.survey.getModuleVos().get(i).setQuestionVos(tasks);
                            if (vo.getId() != null) {
                                List<QuestionVo> delList = new ArrayList<QuestionVo>();
                                String delStr = SharePreferenceUtil.getStringValue(Constants.TASKDEL + taskId, context);
                                ModuleVo delModuleVo = gsonss.fromJson(delStr, ModuleVo.class);
                                delList.add(vo);
                                if (delModuleVo != null && delModuleVo.getQuestionVos() != null) {
                                    delModuleVo.getQuestionVos().addAll(delList);
                                } else {
                                    delModuleVo = new ModuleVo();
                                    delModuleVo.setQuestionVos(delList);
                                }
                                SharePreferenceUtil.save(Constants.TASKDEL + taskId, gsonss.toJson(delModuleVo), context);
                            }
                            SharePreferenceUtil.save(Constants.TASKDETAIL + taskId, gsonss.toJson(taskListResponse), context);
                            notifyChange();
                            return;
                        }
                    }
                } else {
                    //编辑
                    tasks.get(position).setType(2);
                    tasks.get(position).setIsEdit(true);
                }
                notifyChange();
            }
        });
        viewHolder.visitItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tasks.get(position).setType(1);
                tasks.get(position).setIsEdit(false);
                notifyChange();
            }
        });

        viewHolder.visitItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                tasks.get(position).setType(3);
                tasks.get(position).setIsEdit(false);
                notifyChange();
                return true;
            }
        });
        //TODO edittext可滑动
        viewHolder.editRemark.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e("touch", "visit-touchevent:" + event.getAction());
                if (v.getId() == viewHolder.editRemark.getId()) {
                    v.getParent().requestDisallowInterceptTouchEvent(event.getAction() == MotionEvent.ACTION_UP ? false : true);
                }
                return false;
            }
        });

//        viewHolder.imgDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String taskId = SharePreferenceUtil.getStringValue(Constants.TASKID, context);
//                String delStr = SharePreferenceUtil.getStringValue(Constants.TASKDEL + taskId, context);
//                TaskDetailResponse taskListDel = gsonss.fromJson(delStr, TaskDetailResponse.class);
//                if(taskListDel==null||taskListDel.survey==null){
//                    taskListDel = new TaskDetailResponse();
//                }
//                if(vo.getId()!=null){
//                    Del.add(vo);
//                }
//                tasks.remove(vo);
//                String taskListResponseStr = SharePreferenceUtil.getStringValue(Constants.TASKDETAIL + taskId, context);
//                String modelStr = SharePreferenceUtil.getStringValue(Constants.MODEL + taskId, context);
//                TaskDetailResponse taskListResponse = gsonss.fromJson(taskListResponseStr, TaskDetailResponse.class);
//                TaskDetailResponse model = gsonss.fromJson(modelStr, TaskDetailResponse.class);
//                taskListResponse.models = model.models;
//                taskListResponse.survey.setQuestionVos(tasks);
//                SharePreferenceUtil.save(Constants.TASKDETAIL + taskId, gsonss.toJson(taskListResponse), context);
//                taskListDel.survey.getQuestionVos().addAll(Del);
//                SharePreferenceUtil.save(Constants.TASKDEL + taskId, gsonss.toJson(taskListDel), context);
//                notifyDataSetChanged();
//            }
//        });
        return convertView;
    }

    /**
     * 集合去重
     */
    public List<Order> remove(List<Order> list) {
        int size = list.size();
        for (int i = 0; i < size - 1; i++) {
            for (int j = size - 1; j > i; j--) {
                if (list.get(j).receiverAddr.equals(list.get(i).receiverAddr)) {
                    list.remove(j);
                }
            }
        }
        return list;
    }

    class ViewHolder {
        public MTextViewItemQuestion visitLink;
        public MTextViewItemQuestionOther visitItem;
        public LinearLayout layoutItem;
        public EditText editRemark;
        public MListView content;
        public FrameLayout layout;
        public ImageView imgEdit;
        public RelativeLayout relaRight;
        /*/
        新添加的
         */
        public GridView gview;
        public LinearLayout GridHeight;

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
//                String taskId = SharePreferenceUtil.getStringValue(Constants.TASKID, context);
//                Gson gsonss = new Gson();
//                String taskListResponseStr = SharePreferenceUtil.getStringValue(Constants.TASKDETAIL + taskId, context);
//                String modelStr = SharePreferenceUtil.getStringValue(Constants.MODEL + taskId, context);
//                TaskDetailResponse taskListResponse = gsonss.fromJson(taskListResponseStr, TaskDetailResponse.class);
//                TaskDetailResponse model = gsonss.fromJson(modelStr, TaskDetailResponse.class);
//                taskListResponse.models = model.models;
//                taskListResponse.survey.setQuestionVos(tasks);
//                SharePreferenceUtil.save(Constants.TASKDETAIL + taskId, gsonss.toJson(taskListResponse), context);
////                notifyDataSetChanged();
            }
        }


    }

    public void notifyChange() {
        listener.notifyParent();
    }

    public interface CheckClickListener {
        /**
         * 监听选中或没选中
         */
        void afterPositive(ModuleVo vo);

        void notifyParent();
    }


    private void initDatas(Context context) {
        taskId = SharePreferenceUtil.getStringValue(Constants.TASKID, context);
        gsonss = new Gson();
        String itemsDetails = SharePreferenceUtil.getStringValue(Constants.ITEMDETAILS + taskId, context);
        detailsLog = gsonss.fromJson(itemsDetails, LogRequest.class);
        if (detailsLog == null || detailsLog.getOperatorLogDetails() == null) {
            detailsLog = new LogRequest("");
        }
    }

    private void saveLog(String model, QuestionVo vo, String type) {
        vo.getCatalogVos().get(0).setCurrentTime(StringUtils.getTimestamp(System.currentTimeMillis()));
        vo.getCatalogVos().get(0).setModelName(model);
        QuestionsLog log = new QuestionsLog();
        log.setOperatorTime(StringUtils.getTimestamp(System.currentTimeMillis()));
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("【" + type + "】<br/>");
        stringBuffer.append(vo.getCatalogVos().get(0).getModelName());
        stringBuffer.append("/" + vo.getCatalogVos().get(0).getName());
        stringBuffer.append("/" + vo.getCatalogVos().get(0).getCatalogVos().get(0).getName());
        stringBuffer.append("/" + vo.getCatalogVos().get(0).getCatalogVos().get(0).getCheckPointVos().get(0).getName() + "<br/>" + "扣分原因：" + "<br/>");
        int k = 1;
        for (int j = 0, length = vo.getCatalogVos().get(0).getCatalogVos().get(0).getCheckPointVos().get(0).getItemVos().size(); j < length; j++) {
            if (vo.getCatalogVos().get(0).getCatalogVos().get(0).getCheckPointVos().get(0).getItemVos().get(j).ischeck()) {
                stringBuffer.append(k + "、" + vo.getCatalogVos().get(0).getCatalogVos().get(0).getCheckPointVos().get(0).getItemVos().get(j).getName() + "<br/>");
                k++;
            }
        }
        stringBuffer.append("备注：" + (vo.getCatalogVos().get(0).getCatalogVos().get(0).getCheckPointVos().get(0).getRemark() == null ? "" : vo.getCatalogVos().get(0).getCatalogVos().get(0).getCheckPointVos().get(0).getRemark()) + "<br/>");
        log.setOperatorLog(stringBuffer.toString());
        detailsLog.getOperatorLogDetails().add(log);
        SharePreferenceUtil.save(Constants.ITEMDETAILS + taskId, gsonss.toJson(detailsLog, LogRequest.class), context);
    }


}
