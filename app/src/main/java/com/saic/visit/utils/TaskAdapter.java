package com.saic.visit.utils;


import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.saic.visit.R;
import com.saic.visit.activity.MainActicity;
import com.saic.visit.activity.TaskDetailActivity;
import com.saic.visit.constant.Constants;
import com.saic.visit.http.ResponseSupport;
import com.saic.visit.http.VolleyRequestManager;
import com.saic.visit.model.ItemVo;
import com.saic.visit.model.ModuleVo;
import com.saic.visit.model.QuestionVo;
import com.saic.visit.model.ReceptionistTypeVo;
import com.saic.visit.model.TaskDetailRequest;
import com.saic.visit.model.TaskDetailResponse;
import com.saic.visit.model.TaskInfoVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by liuhui on 2016/5/13.
 */
public class TaskAdapter extends BaseAdapter {
    private Context context;
    private List<TaskInfoVo> tasks;

    public TaskAdapter(Context context, List<TaskInfoVo> tasks) {
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
            convertView = View.inflate(context, R.layout.item_task, null);
            viewHolder = new ViewHolder();
            viewHolder.taskName = (TextView) convertView.findViewById(R.id.task_name);
            viewHolder.taskTime = (TextView) convertView.findViewById(R.id.task_time);
            viewHolder.taskStutas = (ImageView) convertView.findViewById(R.id.task_status);
            viewHolder.taskType = (ImageView) convertView.findViewById(R.id.task_type);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final TaskInfoVo taskInfoVo = tasks.get(position);
        String task = taskInfoVo.getCode();
        task = task + context.getResources().getText(R.string.line) + taskInfoVo.getDealerCode()
                + context.getResources().getText(R.string.line) + taskInfoVo.getShortName();
//        // 项目类型
//        if(taskInfoVo.getMainType()==1){
//            task = task + context.getResources().getText(R.string.line) +"售前";
//        }else if(taskInfoVo.getMainType()==2){
//            task = task + context.getResources().getText(R.string.line) + "售后";
//        }
//        //项目类型 (1明访 2暗访)
//        if(taskInfoVo.getRelType()==1){
//            task = task + context.getResources().getText(R.string.line) + "明访";
//        }else if(taskInfoVo.getRelType()==2){
//            task = task + context.getResources().getText(R.string.line) + "暗访";
//        }
        //执行状态(1已完成,2待确认,3寻访中,4计划中)
        if (taskInfoVo.getStatus() == 1) {
            viewHolder.taskStutas.setImageResource(R.drawable.main_type4);
            viewHolder.taskType.setImageResource(R.drawable.gray_circle);
        } else if (taskInfoVo.getStatus() == 2) {
            viewHolder.taskStutas.setImageResource(R.drawable.main_type2);
            viewHolder.taskType.setImageResource(R.drawable.blue_circle);
        } else if (taskInfoVo.getStatus() == 3) {
            viewHolder.taskStutas.setImageResource(R.drawable.main_type3);
            viewHolder.taskType.setImageResource(R.drawable.orange_circle);
        } else if (taskInfoVo.getStatus() == 4) {
            viewHolder.taskStutas.setImageResource(R.drawable.main_type1);
            viewHolder.taskType.setImageResource(R.drawable.gray_circle);
        }
        viewHolder.taskName.setText(task);
        if (taskInfoVo.getStatus() == 4) {
            viewHolder.taskTime.setText("时间：" + StringUtils.getStringDateSimple(taskInfoVo.getPlanTime()));
        } else {
            viewHolder.taskTime.setText(taskInfoVo.getDealerName());
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetWorkUtil.isNetworkConnected(context)) {
                    ToastUtil.show(context, "网络连接不可用");
                    return;
                }
                DialogUtil.showLoading(context);
                TaskDetailRequest taskDetailRequest = new TaskDetailRequest("Task/findByTaskId?taskId=" + taskInfoVo.getId());
                VolleyRequestManager.getInstance(context).startHttpGetRequest(this, taskDetailRequest,
                        ResponseSupport.class, new Response.ListenerV2<ResponseSupport>() {
                            @Override
                            public void onResponse(ResponseSupport response, Map<String, String> headers) {
                                if (!VolleyRequestManager.realResponseResultSupport(context, response, null, true))
                                    return;
                                Gson gsonss = new Gson();
                                TaskDetailResponse taskListResponse = gsonss.fromJson(gsonss.toJson(response.obj), TaskDetailResponse.class);
                                String taskId = taskInfoVo.getId() + "";
                                handleDatas(taskId, taskListResponse, gsonss, response, taskInfoVo);

                            }
                        }, volleyError);

            }
        });
        return convertView;
    }

    class ViewHolder {
        public TextView taskName;
        public TextView taskTime;
        public ImageView taskStutas;
        public ImageView taskType;

    }

    Response.ErrorListener volleyError = new Response.ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError error) {
            DialogUtil.dismisLoading();
        }
    };


    private void handleDatas(String taskId, TaskDetailResponse taskListResponse, Gson gson, ResponseSupport response, TaskInfoVo taskInfoVo) {
        //获取本地的
        String data = SharePreferenceUtil.getStringValue(Constants.TASKDETAIL + taskId, context);

        TaskDetailResponse local = gson.fromJson(data, TaskDetailResponse.class);
        String dataDel = SharePreferenceUtil.getStringValue(Constants.TASKDEL + taskId, context);
        ModuleVo localDel = gson.fromJson(dataDel, ModuleVo.class);
        List<ReceptionistTypeVo> list = handleReception(gson, response, local);
        if (list != null) {
            taskListResponse.receptionistTypes = list;
        }
        if (taskInfoVo.getStatus() != 1) {
            taskListResponse.survey.setModuleVos(handTaskList(taskId, gson, response, local, localDel));
            //taskListResponse.models(handTaskList(taskId, gson, response, local, localDel));
            taskListResponse.models=handTaskListone(gson,response,local);

        }

        System.out.println("this is dataurl"+gson.toJson(taskListResponse));
        SharePreferenceUtil.save(Constants.TASKID, taskId, context);
        SharePreferenceUtil.save(Constants.TASKDETAIL + taskId, gson.toJson(taskListResponse), context);
        SharePreferenceUtil.save(Constants.MODEL + taskId, gson.toJson(response.obj), context);
        SharePreferenceUtil.save(Constants.RESPONSE + taskId, gson.toJson(response.obj), context);
        SharePreferenceUtil.saveInt(Constants.TYPE + taskId, taskInfoVo.getStatus(), context);

        Intent intent = new Intent(context, TaskDetailActivity.class);
        intent.putExtra(Constants.TASKDETAIL, taskListResponse);
        intent.putExtra("jingxiaoshang", taskInfoVo.getShortName());
        intent.putExtra("jingxiaoshangdaima", taskInfoVo.getDealerCode());
        ((MainActicity) context).startActivityForResult(intent, 100);
        ((MainActicity) context).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }


    /**
     * 处理接待人员 本地与网络的数据
     */
    private List<ReceptionistTypeVo> handleReception(Gson gson, ResponseSupport response, TaskDetailResponse localReception) {
        if (localReception == null) {
            return null;
        }
        List<ReceptionistTypeVo> localReceptionList = localReception.receptionistTypes;
        TaskDetailResponse recepResponse = gson.fromJson(gson.toJson(response.obj), TaskDetailResponse.class);
        List<ReceptionistTypeVo> recepResponseList = recepResponse.receptionistTypes;
        int responseSize = recepResponseList.size();
        int localRecepSize = localReceptionList.size();
        if (responseSize <= localRecepSize) {
            return localReceptionList;
        }
        return localReceptionList;
    }


    private ArrayList<ModuleVo> handTaskListone(Gson gson, ResponseSupport response, TaskDetailResponse local){
        ArrayList<ModuleVo> moduleVos=new ArrayList<ModuleVo>();
        //获得网络上的数据
        TaskDetailResponse taskListResponse = gson.fromJson(gson.toJson(response.obj), TaskDetailResponse.class);
        //拿到所有的model
        List<ModuleVo> localModuleVoList = taskListResponse.models;
        if(local==null||local.models.size()<=0){
            moduleVos.addAll(taskListResponse.models);
        }else{
            moduleVos.addAll(local.models);
        }

        return moduleVos;
    }

    private ArrayList<ModuleVo> handTaskList(String taskId, Gson gson, ResponseSupport response, TaskDetailResponse local, ModuleVo localDel) {
        ArrayList<ModuleVo> moduleVos = new ArrayList<ModuleVo>();
        //获得网络上的数据
        TaskDetailResponse taskListResponse = gson.fromJson(gson.toJson(response.obj), TaskDetailResponse.class);
        //拿到所有的model
        List<ModuleVo> localModuleVoList = taskListResponse.survey.getModuleVos();
        if ((local == null || local.survey == null || local.survey.getModuleVos().size() <= 0) && (localDel == null || localDel.getQuestionVos() == null || localDel.getQuestionVos().size() <= 0)) {
            //本地无数据 删除的数据也没有
            moduleVos.addAll(taskListResponse.survey.getModuleVos());
        } else {
           // moduleVos.addAll(local.survey.getModuleVos());
            if (localDel == null || localDel.getQuestionVos() == null || localDel.getQuestionVos().size() <= 0) {
                //本地无删除的数据 只需要替换本地与网络的替换
                //找出服务器删除 本地还存在的
                //找出服务器新增的
                //用来存储本地所有的Question
                List<QuestionVo> respondeQuestion = new ArrayList<QuestionVo>();
                List<QuestionVo> respondeQuestion2 = new ArrayList<QuestionVo>();
                List<QuestionVo> question = new ArrayList<QuestionVo>();
                List<QuestionVo> resAndLocal = new ArrayList<QuestionVo>();
                List<QuestionVo> resAndLocal2 = new ArrayList<QuestionVo>();
                List<QuestionVo> addLocal = new ArrayList<QuestionVo>();
                //              本地module
                List<ModuleVo> moduleVos1 = local.survey.getModuleVos();
                List<QuestionVo> localQuestion = new ArrayList<QuestionVo>();
                List<QuestionVo> localQuestion2 = new ArrayList<QuestionVo>();
                int moduleVos1Size = moduleVos1.size();
                for (int i = 0; i < moduleVos1Size; i++) {
                    if (moduleVos1.get(i).getQuestionVos().size() > 0) {
                        localQuestion.addAll(moduleVos1.get(i).getQuestionVos());
                        localQuestion2.addAll(moduleVos1.get(i).getQuestionVos());
                    }
                }
                for (QuestionVo questionVo : localQuestion) {
                    if (questionVo.getId() == null) {
                        addLocal.add(questionVo);
                    }
                }

//                网络module
                List<ModuleVo> responseModuleVos = taskListResponse.survey.getModuleVos();
                int responseModuleVosSize = responseModuleVos.size();
                for (int i = 0; i < responseModuleVosSize; i++) {
                    if (responseModuleVos.get(i).getQuestionVos().size() > 0) {
                        respondeQuestion.addAll(responseModuleVos.get(i).getQuestionVos());
                        respondeQuestion2.addAll(responseModuleVos.get(i).getQuestionVos());
                    }
                }
                //替换本地与网络上的数据
                int localSize = localQuestion.size();
                int responseSize = respondeQuestion.size();
                for (int i = 0; i < localSize; i++) {
                    for (int j = 0; j < responseSize; j++) {
                        if (localQuestion.get(i).getId() != null && localQuestion.get(i).getId().longValue() == respondeQuestion.get(j).getId().longValue()) {
                            if (isExsitAdd(localQuestion.get(i), taskId)) {
                                resAndLocal.add(addQuesAndResponse(localQuestion.get(i), taskId));
                            } else {
                                resAndLocal.add(responseLastAndLocal(respondeQuestion.get(j), taskId, localQuestion.get(i)));
                            }
                            resAndLocal2.add(respondeQuestion.get(j));

                        }
                    }
                }
                //respondeQuestion2剩下的为新增的
                respondeQuestion2.removeAll(resAndLocal2);
                question.addAll(resAndLocal);
                question.addAll(respondeQuestion2);
                question.addAll(addLocal);
                for (int i = 0; i < localModuleVoList.size(); i++) {
                    List<QuestionVo> mList = new ArrayList<QuestionVo>();
                    for (int j = 0; j < question.size(); j++) {
                        if (question.get(j).getModuleId() != null && localModuleVoList.get(i).getId().longValue() == question.get(j).getModuleId().longValue()) {
                            mList.add(question.get(j));
                        }
                    }
                    responseModuleVos.get(i).setQuestionVos(mList);
                }
                moduleVos.addAll(responseModuleVos);

            } else {
                //本地有删除
                List<QuestionVo> respondeQuestion = new ArrayList<QuestionVo>();
                List<QuestionVo> respondeQuestion2 = new ArrayList<QuestionVo>();
                List<QuestionVo> question = new ArrayList<QuestionVo>();
                List<QuestionVo> resAndLocal = new ArrayList<QuestionVo>();
                List<QuestionVo> resAndLocal2 = new ArrayList<QuestionVo>();
                List<QuestionVo> addLocal = new ArrayList<QuestionVo>();
                //              本地module
                List<ModuleVo> moduleVos1 = local.survey.getModuleVos();
                List<QuestionVo> localQuestion = new ArrayList<QuestionVo>();
                List<QuestionVo> localQuestion2 = new ArrayList<QuestionVo>();
                int moduleVos1Size = moduleVos1.size();
                for (int i = 0; i < moduleVos1Size; i++) {
                    if (moduleVos1.get(i).getQuestionVos().size() > 0) {
                        localQuestion.addAll(moduleVos1.get(i).getQuestionVos());
                        localQuestion2.addAll(moduleVos1.get(i).getQuestionVos());
                    }
                }
                for (QuestionVo questionVo : localQuestion) {
                    if (questionVo.getId() == null) {
                        addLocal.add(questionVo);
                    }
                }

//                网络module
                List<ModuleVo> responseModuleVos = taskListResponse.survey.getModuleVos();
                int responseModuleVosSize = responseModuleVos.size();
                for (int i = 0; i < responseModuleVosSize; i++) {
                    if (responseModuleVos.get(i).getQuestionVos().size() > 0) {
                        respondeQuestion.addAll(responseModuleVos.get(i).getQuestionVos());
                        respondeQuestion2.addAll(responseModuleVos.get(i).getQuestionVos());
                    }
                }
                //替换本地与网络上的数据
                int localSize = localQuestion.size();
                int responseSize = respondeQuestion.size();
                for (int i = 0; i < localSize; i++) {
                    for (int j = 0; j < responseSize; j++) {
                        if (localQuestion.get(i).getId() != null && localQuestion.get(i).getId().longValue() == respondeQuestion.get(j).getId().longValue()) {
                            if (isExsitAdd(localQuestion.get(i), taskId)) {
                                resAndLocal.add(addQuesAndResponse(localQuestion.get(i), taskId));
                            } else {
                                //处理上次response与本地 看是否有修改
                                resAndLocal.add(responseLastAndLocal(respondeQuestion.get(j), taskId, localQuestion.get(i)));
                            }
                            resAndLocal2.add(respondeQuestion.get(j));
                        }
                    }
                }
                //respondeQuestion2剩下的为新增的
                respondeQuestion2.removeAll(resAndLocal2);
                question.addAll(resAndLocal);
                question.addAll(respondeQuestion2);
                question.addAll(addLocal);
                int localDelSize = localDel.getQuestionVos().size();
                for (int i = 0; i < localDelSize; i++) {
                    for (int j = 0; j < question.size(); j++) {
                        if (question.get(j).getId() != null && localDel.getQuestionVos().get(i).getId().longValue() == question.get(j).getId().longValue()) {
                            question.remove(question.get(j));
                        }
                    }
                }
                for (int i = 0; i < localModuleVoList.size(); i++) {
                    List<QuestionVo> mList = new ArrayList<QuestionVo>();
                    for (int j = 0; j < question.size(); j++) {
                        if (question.get(j).getModuleId() != null && localModuleVoList.get(i).getId().longValue() == question.get(j).getModuleId().longValue()) {
                            mList.add(question.get(j));
                        }
                    }
                    responseModuleVos.get(i).setQuestionVos(mList);
                }
                moduleVos.addAll(responseModuleVos);
            }
        }
        return moduleVos;
    }

    private boolean isExsitAdd(QuestionVo vo, String taskId) {
        Gson gson = new Gson();
        String addQuestion = SharePreferenceUtil.getStringValue(Constants.ADDQUESTION + taskId, context);
        ModuleVo add = gson.fromJson(addQuestion, ModuleVo.class);
        if (add != null && add.getQuestionVos() != null) {
            List<QuestionVo> addQues = add.getQuestionVos();
            for (int i = 0, len = addQues.size(); i < len; i++) {
                if (addQues.get(i).getId().longValue() == vo.getId().longValue()) {
                    return true;
                }
            }
        }
        return false;
    }

    private QuestionVo addQuesAndResponse(QuestionVo vo, String taskId) {
        //获取新增的
        Gson gson = new Gson();
        String addQuestion = SharePreferenceUtil.getStringValue(Constants.ADDQUESTION + taskId, context);
        ModuleVo add = gson.fromJson(addQuestion, ModuleVo.class);
        if (add != null && add.getQuestionVos() != null) {
            List<QuestionVo> addQues = add.getQuestionVos();
            for (int i = 0, len = addQues.size(); i < len; i++) {
                if (addQues.get(i).getId().longValue() == vo.getId().longValue()) {
                    List<ItemVo> itemVos = addQues.get(i).getCatalogVos().get(0).getCatalogVos().get(0).getCheckPointVos().get(0).getItemVos();
                    List<ItemVo> responseitemVos = vo.getCatalogVos().get(0).getCatalogVos().get(0).getCheckPointVos().get(0).getItemVos();
                    for (int j = 0, len2 = itemVos.size(); j < len2; j++) {
                        if (itemVos.get(j).ischeck() || responseitemVos.get(j).ischeck()) {
                            itemVos.get(j).setIscheck(true);
                        } else {
                            itemVos.get(j).setIscheck(false);
                        }
                    }
                    vo.getCatalogVos().get(0).getCatalogVos().get(0).getCheckPointVos().get(0).setItemVos(itemVos);
                    return vo;
                }

            }
        }
        return vo;
    }

    private QuestionVo responseLastAndLocal(QuestionVo response, String taskId, QuestionVo local) {
        Gson gson = new Gson();
        String lastResponse = SharePreferenceUtil.getStringValue(Constants.RESPONSE + taskId, context);
        TaskDetailResponse lastTaskResponse = gson.fromJson(lastResponse, TaskDetailResponse.class);
        List<ModuleVo> moduleVos = lastTaskResponse.survey.getModuleVos();
        List<QuestionVo> lastResponseQuestion = new ArrayList<QuestionVo>();
        for (int i = 0; i < moduleVos.size(); i++) {
            lastResponseQuestion.addAll(moduleVos.get(i).getQuestionVos());
        }
        for (int j = 0, len = lastResponseQuestion.size(); j < len; j++) {
            if (local.getId().longValue() == lastResponseQuestion.get(j).getId().longValue()) {
                List<ItemVo> itemVos = local.getCatalogVos().get(0).getCatalogVos().get(0).getCheckPointVos().get(0).getItemVos();
                List<ItemVo> lastitemVos = lastResponseQuestion.get(j).getCatalogVos().get(0).getCatalogVos().get(0).getCheckPointVos().get(0).getItemVos();
                for (int k = 0; k < itemVos.size(); k++) {
                    if (!itemVos.get(k).ischeck() && lastitemVos.get(k).ischeck()) {
                        return local;
                    }
                }
            }
        }
        return response;
    }

}
