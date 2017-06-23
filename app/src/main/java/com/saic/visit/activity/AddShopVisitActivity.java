package com.saic.visit.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.saic.visit.R;
import com.saic.visit.common.BaseActivity;
import com.saic.visit.constant.Constants;
import com.saic.visit.model.CatalogVo;
import com.saic.visit.model.CheckPointVo;
import com.saic.visit.model.ItemVo;
import com.saic.visit.model.ModuleVo;
import com.saic.visit.model.QuestionVo;
import com.saic.visit.model.TaskDetailResponse;
import com.saic.visit.utils.SharePreferenceUtil;
import com.saic.visit.utils.StringUtils;
import com.saic.visit.utils.TaskPointAdapter;
import com.saic.visit.utils.ToastUtil;
import com.saic.visit.utils.ViewHeightUtils;
import com.saic.visit.utils.ViewUtil;
import com.saic.visit.widget.RadioButtonAndEditTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by liuhui on 2016/5/19.
 */
public class AddShopVisitActivity extends BaseActivity {

    @Bind(R.id.rela_back)
    RelativeLayout relaBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.img_right)
    ImageView imgRight;
    @Bind(R.id.rela_right)
    RelativeLayout relaRight;
    @Bind(R.id.txt_model)
    TextView txtModel;
    @Bind(R.id.radio_model)
    RadioButtonAndEditTextView radioModel;
    @Bind(R.id.txt_check_link_one)
    TextView txtCheckLinkOne;
    @Bind(R.id.txt_check_link_two)
    TextView txtCheckLinkTwo;
    @Bind(R.id.radio_check_link_one)
    RadioButtonAndEditTextView radioCheckLinkOne;
    @Bind(R.id.radio_check_link_two)
    RadioButtonAndEditTextView radioCheckLinkTwo;
    @Bind(R.id.txt_check_item)
    TextView txtCheckItem;
    @Bind(R.id.list_content)
    ListView listContent;
    @Bind(R.id.layout)
    FrameLayout layout;
    @Bind(R.id.btn_add)
    Button btnAdd;
    private String model, linkOne,linkTwo, item;
    Drawable drawableBlue;
    Drawable drawableGray;
    private TaskPointAdapter mTaskPointAdapter;
    private List<CheckPointVo> tasks = new ArrayList<CheckPointVo>();
    private ViewGroup.LayoutParams params;
    TaskDetailResponse taskListResponse;
    TaskDetailResponse taskMode;
    TaskDetailResponse taskList;
    //模块索引
    private int modelIndex = -1;
    //一级目录索引
    private int linkIndexOne = -1;
    //二级目录索引
    private int linkIndexTwo = -1;
    private QuestionVo mAddQuestionVo = new QuestionVo();
    private int size = 0;
    private int pointSize = 0;
    List<CheckPointVo> selectPoint = new ArrayList<CheckPointVo>();
    Gson gsonss;
    String taskId;
    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_add_shop_visit);
        ButterKnife.bind(this);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        txtTitle.setText("添加/修改记录");
        relaRight.setVisibility(View.INVISIBLE);
         gsonss = new Gson();
        taskId = SharePreferenceUtil.getStringValue(Constants.TASKID,AddShopVisitActivity.this);
        String data = SharePreferenceUtil.getStringValue(Constants.TASKDETAIL + taskId, AddShopVisitActivity.this);
        String modelData = SharePreferenceUtil.getStringValue(Constants.MODEL + taskId, AddShopVisitActivity.this);
        taskListResponse = gsonss.fromJson(data, TaskDetailResponse.class);
        taskMode  = gsonss.fromJson(modelData, TaskDetailResponse.class);
        taskList = gsonss.fromJson(modelData, TaskDetailResponse.class);
        listContent.setAdapter(mTaskPointAdapter);
        drawableBlue = getResources().getDrawable(R.drawable.check_blue);
        /// 这一步必须要做,否则不会显示.
        drawableBlue.setBounds(0, 0, drawableBlue.getMinimumWidth(), drawableBlue.getMinimumHeight());
        drawableGray = getResources().getDrawable(R.drawable.check_gray);
        drawableGray.setBounds(0, 0, drawableGray.getMinimumWidth(), drawableGray.getMinimumHeight());
        /// 这一步必须要做,否则不会显示
        radioModel.initData(radioModel, taskListResponse,taskListResponse.models.size(), true, listener);
        params = layout.getLayoutParams();
        setRadioButtonOpenOrClose(R.id.txt_model);
    }

    @OnClick({R.id.rela_back, R.id.btn_add, R.id.rela_right, R.id.txt_model, R.id.txt_check_link_one, R.id.txt_check_link_two, R.id.txt_check_item})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rela_back:
                finish();
                AddShopVisitActivity.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                break;
            case R.id.btn_add:
                /**
                 * 添加完照片返回的时候就应该吧表创建好
                 *
                 */
                ToastUtil.show(AddShopVisitActivity.this,"开始添加");

                if(pointSize<= 0){
                    ToastUtil.show(AddShopVisitActivity.this,"请先选择检查项!");
                    return;
                }
                if(size <= 0 ){
                    ToastUtil.show(AddShopVisitActivity.this,"请先选择检查点!");
                    return;
                }

                ModuleVo mAddModuleVo = new ModuleVo();
                mAddModuleVo = taskList.models.get(modelIndex);
                CatalogVo catalogVo = taskList.models.get(modelIndex).getCatalogVos().get(linkIndexOne);
                CatalogVo catalogVo2 = taskList.models.get(modelIndex).getCatalogVos().get(linkIndexOne).getCatalogVos().get(linkIndexTwo);
                catalogVo2.setCheckPointVos(selectPoint);
                ArrayList<CatalogVo> catalist = new ArrayList<CatalogVo>();
                ArrayList<CatalogVo> catalist2 = new ArrayList<CatalogVo>();
                catalist2.add(catalogVo2);
                catalist.add(catalogVo);
                catalogVo.setCatalogVos(catalist2);
                for(int i=0;i<taskListResponse.survey.getModuleVos().size();i++){
                    if(taskListResponse.survey.getModuleVos().get(i).getId().longValue()==mAddModuleVo.getId().longValue()){
                       //获取当前目录下的questions
                        List<QuestionVo> questionVos = taskListResponse.survey.getModuleVos().get(i).getQuestionVos();
                        int questionSize = questionVos.size();
                        boolean exsitQuestion = isExsitQuestion(questionVos, questionSize);
                        if(!exsitQuestion){
                            mAddQuestionVo.setCatalogVos(catalist);
                            mAddQuestionVo.setModuleId(mAddModuleVo.getId());
                            taskListResponse.survey.getModuleVos().get(i).getQuestionVos().add(mAddQuestionVo);
                        }else{
                            for(int j =0; j<questionSize; j++){
                                if(questionVos.get(j).getCatalogVos().get(0).getCatalogVos().get(0).getCheckPointVos().get(0).getId().longValue()== selectPoint.get(0).getId().longValue()){
                                    QuestionVo mAddQuestion = new QuestionVo();
                                    mAddQuestion.setModuleId(mAddModuleVo.getId());
                                    mAddQuestion.setCatalogVos(catalist);

                                    //获取当前的items
                                    List<ItemVo> itemVos = questionVos.get(j).getCatalogVos().get(0).getCatalogVos().get(0).getCheckPointVos().get(0).getItemVos();
                                    List<ItemVo> itemVosAdd = selectPoint.get(0).getItemVos();
                                    handleNewQuestion(mAddQuestion,itemVosAdd);
                                    int size = itemVos.size();
                                    for(int k = 0; k< size; k++){
                                        if(itemVos.get(k).ischeck()||itemVosAdd.get(k).ischeck()){
                                            questionVos.get(j).getCatalogVos().get(0).getCatalogVos().get(0).getCheckPointVos().get(0).getItemVos().get(k).setIscheck(true);
                                        }else{
                                            questionVos.get(j).getCatalogVos().get(0).getCatalogVos().get(0).getCheckPointVos().get(0).getItemVos().get(k).setIscheck(false);
                                        }
                                    }

                                    questionVos.get(j).setRemark(StringUtils.isEmpty(questionVos.get(j).getRemark())?"":(questionVos.get(j).getRemark()+getResources().getText(R.string.tran))+(StringUtils.isEmpty(mAddQuestionVo.getRemark())?"":mAddQuestionVo.getRemark()));
                                    taskListResponse.survey.getModuleVos().get(i).setQuestionVos(questionVos);

                                }
                            }
                        }
                        taskListResponse.models = taskMode.models;
                        Gson gson = new Gson();
                        String taskList = gson.toJson(taskListResponse);
                        SharePreferenceUtil.save(Constants.TASKDETAIL + taskId, taskList, AddShopVisitActivity.this);
                        setResult(100);
                        finish();
                        AddShopVisitActivity.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                    }
                }

                break;
            case R.id.rela_right:
                break;
            case R.id.txt_model:
                setRadioButtonOpenOrClose(R.id.txt_model);
                break;
            case R.id.txt_check_link_one:
                if(modelIndex == -1){
                    ToastUtil.show(AddShopVisitActivity.this,"请先选择模块!");
                    return;
                }
                setRadioButtonOpenOrClose(R.id.txt_check_link_one);
                break;
            case R.id.txt_check_link_two:
                if(linkIndexOne == -1){
                    ToastUtil.show(AddShopVisitActivity.this,"请先选择一级目录!");
                    return;
                }
                setRadioButtonOpenOrClose(R.id.txt_check_link_two);
                break;
            case R.id.txt_check_item:
                if(linkIndexTwo == -1){
                    ToastUtil.show(AddShopVisitActivity.this,"请先选择二级目录!");
                    return;
                }
                setRadioButtonOpenOrClose(R.id.txt_check_item);
                break;

        }
    }
    private void setRadioButtonOpenOrClose(int id) {
        if (R.id.txt_model == id) {
            radioModel.setVisibility(View.VISIBLE);
            radioCheckLinkOne.setVisibility(View.VISIBLE);
            radioCheckLinkTwo.setVisibility(View.GONE);
            listContent.setVisibility(View.GONE);
            params.height = 0 ;
            layout.setLayoutParams(params);
        } else if (R.id.txt_check_link_one == id) {
            radioModel.setVisibility(View.VISIBLE);
            radioCheckLinkOne.setVisibility(View.VISIBLE);
            radioCheckLinkTwo.setVisibility(View.VISIBLE);
            listContent.setVisibility(View.GONE);
            params.height = 0 ;
            layout.setLayoutParams(params);
        }else if(R.id.txt_check_link_two == id){
            radioModel.setVisibility(View.VISIBLE);
            radioCheckLinkOne.setVisibility(View.VISIBLE);
            radioCheckLinkTwo.setVisibility(View.VISIBLE);
            listContent.setVisibility(View.VISIBLE);
//            params.height = 0 ;
//            layout.setLayoutParams(params);
        } else if (R.id.txt_check_item == id ) {
            params.height = ViewHeightUtils.setListViewHeightBasedOnChildren1(listContent,AddShopVisitActivity.this)+ViewUtil.dpToPx(16, AddShopVisitActivity.this);
            layout.setLayoutParams(params);
            mTaskPointAdapter.notifyDataSetChanged();
            radioModel.setVisibility(View.VISIBLE);
            radioCheckLinkOne.setVisibility(View.VISIBLE);
            radioCheckLinkTwo.setVisibility(View.VISIBLE);
            listContent.setVisibility(View.VISIBLE);
        }
    }

    RadioButtonAndEditTextView.RadioButtonClickListener listener = new RadioButtonAndEditTextView.RadioButtonClickListener() {

        @Override
        public void afterPositive(RadioButtonAndEditTextView CheckItem, String radioValue,int index) {

            if (CheckItem == radioModel) {
                modelIndex = index;
                if (!StringUtils.isEmpty(radioValue)) {
                    if (radioValue.equals(model)) {
                        txtCheckLinkOne.setCompoundDrawables(drawableBlue, null, null, null);
                    } else {
                        linkOne = "";
                        linkTwo = "";
                        linkIndexOne = -1;
                        linkIndexTwo = -1;
                        radioCheckLinkOne.initData(radioCheckLinkOne, taskListResponse.models.get(modelIndex), taskListResponse.models.get(modelIndex).getCatalogVos().size(), true, listener);
                        txtCheckLinkOne.setCompoundDrawables(drawableGray, null, null, null);
                        txtCheckLinkTwo.setCompoundDrawables(drawableGray, null, null, null);
                        txtCheckItem.setCompoundDrawables(drawableGray, null, null, null);
                        setRadioButtonOpenOrClose(R.id.txt_model);
                    }
                    txtModel.setCompoundDrawables(drawableBlue, null, null, null);
                    model = radioValue;
                } else {
                    txtModel.setCompoundDrawables(drawableGray, null, null, null);
                }


            }

            if (CheckItem == radioCheckLinkOne) {
                linkIndexOne = index;
                if (!StringUtils.isEmpty(radioValue)) {
                    if (radioValue.equals(linkOne)) {
                        txtCheckLinkTwo.setCompoundDrawables(drawableBlue, null, null, null);
                    } else {
                        linkTwo = "";
                        linkIndexTwo = -1;
                        radioCheckLinkTwo.initData(radioCheckLinkTwo, taskListResponse.models.get(modelIndex).getCatalogVos().get(linkIndexOne), taskListResponse.models.get(modelIndex).getCatalogVos().get(linkIndexOne).getCatalogVos().size(), true, listener);
                        txtCheckLinkTwo.setCompoundDrawables(drawableGray, null, null, null);
                        txtCheckItem.setCompoundDrawables(drawableGray, null, null, null);
                        setRadioButtonOpenOrClose(R.id.txt_check_link_one);
                    }
                    txtCheckLinkOne.setCompoundDrawables(drawableBlue, null, null, null);
                    linkOne = radioValue;
                } else {
                    txtModel.setCompoundDrawables(drawableGray, null, null, null);
                }


            }
            if (CheckItem == radioCheckLinkTwo) {
                linkIndexTwo = index;
                if (!StringUtils.isEmpty(radioValue)) {
                    if (radioValue.equals(linkTwo)) {
//                        txtCheckItem.setCompoundDrawables(drawableBlue, null, null, null);
                    } else {
                        listContent.setVisibility(View.GONE);
                        txtCheckItem.setCompoundDrawables(drawableGray, null, null, null);
                        setRadioButtonOpenOrClose(R.id.txt_check_link_two);
                    }
                    txtCheckLinkTwo.setCompoundDrawables(drawableBlue, null, null, null);
                    linkTwo = radioValue;
                } else {
                    txtCheckLinkTwo.setCompoundDrawables(drawableGray, null, null, null);
                }
                tasks = taskListResponse.models.get(modelIndex).getCatalogVos().get(linkIndexOne).getCatalogVos().get(linkIndexTwo).getCheckPointVos();
                mTaskPointAdapter = new TaskPointAdapter(AddShopVisitActivity.this, tasks, new TaskPointAdapter.CheckClickListener() {
                    @Override
                    public void afterPositive(List<CheckPointVo> select) {
                        pointSize = select.size();
                        size = 0;
                        if(select.size()> 0){
                            int itemSize = select.get(0).getItemVos().size();
                            for(int i=0; i < itemSize; i++){
                                if(select.get(0).getItemVos().get(i).ischeck()){
                                    size++;
                                }
                            }
                            selectPoint = select;
                        }
                    }
                    @Override
                    public void afterPositive(String remark) {
                        if(!StringUtils.isEmpty(remark)){
                            mAddQuestionVo.setRemark(remark);
                        }

                    }
                });
                listContent.setAdapter(mTaskPointAdapter);
                setRadioButtonOpenOrClose(R.id.txt_check_item);
            }

        }

    };

    public  void setHeight(){
        params.height = ViewHeightUtils.setListViewHeightBasedOnChildren1(listContent,AddShopVisitActivity.this)+ViewUtil.dpToPx(16, AddShopVisitActivity.this);
        layout.setLayoutParams(params);
    }

    public  void setSelect(Boolean flag){
        if(flag){
            txtCheckItem.setCompoundDrawables(drawableBlue, null, null, null);
        }else{
            txtCheckItem.setCompoundDrawables(drawableGray, null, null, null);
        }

    }


    private boolean isExsitQuestion(List<QuestionVo> questionVos ,int questionSize){
        for(int j =0; j<questionSize; j++){
            if(questionVos.get(j).getCatalogVos().get(0).getCatalogVos().get(0).getCheckPointVos().get(0).getId().longValue()== selectPoint.get(0).getId().longValue()){
                return  true;
            }
        }
            return  false;
    }

    private void handleNewQuestion(QuestionVo mAddQuestion,List<ItemVo> itemVosAdd){
        String stringValue = SharePreferenceUtil.getStringValue(Constants.ADDQUESTION + taskId, AddShopVisitActivity.this);
        List<QuestionVo> addTaskList = new ArrayList<QuestionVo>();
        ModuleVo moduleVo = gsonss.fromJson(stringValue, ModuleVo.class);
        addTaskList.add(mAddQuestion);
        //添加相同
        QuestionVo existQuestion = null;
        List<QuestionVo> questionVos = null;
        if(moduleVo!=null&&moduleVo.getQuestionVos()!=null){
            int newQuestionSize = moduleVo.getQuestionVos().size();
            questionVos = moduleVo.getQuestionVos();
            for(int i = 0;i< newQuestionSize;i++){
                if(questionVos.get(i).getModuleId().longValue() == mAddQuestion.getModuleId().longValue() ){
                    existQuestion  = questionVos.get(i);
                    List<ItemVo> itemVos = questionVos.get(i).getCatalogVos().get(0).getCatalogVos().get(0).getCheckPointVos().get(0).getItemVos();
                    for(int k = 0 ,len =questionVos.get(i).getCatalogVos().get(0).getCatalogVos().get(0).getCheckPointVos().get(0).getItemVos().size();k < len;k++ ){
                        if(itemVos.get(k).ischeck()||itemVosAdd.get(k).ischeck()){
                            questionVos.get(i).getCatalogVos().get(0).getCatalogVos().get(0).getCheckPointVos().get(0).getItemVos().get(k).setIscheck(true);
                        }else{
                            questionVos.get(i).getCatalogVos().get(0).getCatalogVos().get(0).getCheckPointVos().get(0).getItemVos().get(k).setIscheck(false);
                        }
                    }
                    return;
                }
            }
        }else{
            moduleVo = new ModuleVo();
        }
        if(existQuestion ==null){
            if(moduleVo.getQuestionVos()!=null){
                moduleVo.getQuestionVos().add(mAddQuestion);
            }else{
                List<QuestionVo> qv = new ArrayList<QuestionVo>();
                qv.add(mAddQuestion);
                moduleVo.setQuestionVos(qv);
            }

        } else {
            moduleVo.setQuestionVos(questionVos);
        }
        SharePreferenceUtil.save(Constants.ADDQUESTION,gsonss.toJson(moduleVo),AddShopVisitActivity.this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            finish();
            AddShopVisitActivity.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        }
        return false;
    }
}
