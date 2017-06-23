package com.saic.visit.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.saic.visit.R;
import com.saic.visit.common.BaseActivity;
import com.saic.visit.constant.Constants;
import com.saic.visit.model.Receptionist;
import com.saic.visit.model.ReceptionistTypeVo;
import com.saic.visit.model.TaskDetailResponse;
import com.saic.visit.utils.PopWindowAdapter;
import com.saic.visit.utils.SharePreferenceUtil;
import com.saic.visit.utils.ToastUtil;
import com.saic.visit.utils.UserInfoAdapter;
import com.saic.visit.widget.ClearEditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by liuhui on 2016/5/17.
 */
public class UserInfoActivity extends BaseActivity {
    @Bind(R.id.rela_back)
    RelativeLayout relaBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.img_right)
    ImageView imgRight;
    @Bind(R.id.rela_right)
    RelativeLayout relaRight;
    @Bind(R.id.top)
    RelativeLayout top;
    @Bind(R.id.username)
    AutoCompleteTextView username;
    @Bind(R.id.position)
    ClearEditText position;
    @Bind(R.id.mobile)
    ClearEditText mobile;
    @Bind(R.id.btn_save)
    Button btnSave;
    @Bind(R.id.view)
    View view;
    @Bind(R.id.man)
    RadioButton man;
    @Bind(R.id.women)
    RadioButton women;
    @Bind(R.id.group)
    RadioGroup group;
    private PopupWindow popView;
    private PopWindowAdapter adapter;
    private ListView listPop;
    TaskDetailResponse taskListResponse;
    ReceptionistTypeVo re;
    ReceptionistTypeVo initRe;
    ReceptionistTypeVo newRe = new ReceptionistTypeVo();
    int index;
    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_userinfo);
        ButterKnife.bind(this);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        re = (ReceptionistTypeVo) getIntent().getExtras().get(Constants.USERINFO);
        index = getIntent().getExtras().getInt(Constants.USERINDEX);
        initRe = (ReceptionistTypeVo) getIntent().getExtras().get(Constants.USERINFO);
        Gson gsonss = new Gson();
        String taskId = SharePreferenceUtil.getStringValue(Constants.TASKID, UserInfoActivity.this);
        String data = SharePreferenceUtil.getStringValue(Constants.TASKDETAIL + taskId, UserInfoActivity.this);
        taskListResponse = gsonss.fromJson(data, TaskDetailResponse.class);
        UserInfoAdapter mAdapter = new UserInfoAdapter(UserInfoActivity.this, taskListResponse.receptionists);
        username.setAdapter(mAdapter);
        username.setThreshold(1);  //设置输入一个字符 提示，默认为2
        txtTitle.setText(re.getName());
        relaRight.setVisibility(View.INVISIBLE);
        initDatas(re);
    }


    @OnClick({R.id.rela_back, R.id.btn_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rela_back:
                finish();
                UserInfoActivity.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                break;
            case R.id.btn_save:
                if (username.getText().toString() == null || "".equals(username.getText().toString())) {
                    ToastUtil.show(UserInfoActivity.this, "请输入姓名");
                    return;
                }
                if (!women.isChecked() && !man.isChecked()) {
                    ToastUtil.show(UserInfoActivity.this, "请选择性别");
                    return;
                }

                if (position.getText().toString() == null || "".equals(position.getText().toString())) {
                    ToastUtil.show(UserInfoActivity.this, "请输入职位");
                    return;
                }

                if (mobile.getText().toString() == null || "".equals(mobile.getText().toString())) {
                    ToastUtil.show(UserInfoActivity.this, "请输入电话号码");
                    return;
                }
                newRe.setGender(women.isChecked() ? 2 : 1);
                newRe.setReceptionistName(username.getText().toString());
                newRe.setPhone(mobile.getText().toString());
                newRe.setPosition(position.getText().toString());
                if (newRe.getPosition().equals(initRe.getPosition())
                        &&newRe.getReceptionistName().equals(initRe.getReceptionistName())
                        &&newRe.getPhone().equals(initRe.getPhone())
                        &&newRe.getGender().equals(initRe.getGender())) {
                    ToastUtil.show(UserInfoActivity.this, "请修改后再保存");
                    return;
                }
                setResult(100);
                Gson gson = new Gson();
                String taskId = SharePreferenceUtil.getStringValue(Constants.TASKID, UserInfoActivity.this);
                TaskDetailResponse taskDetailResponse = gson.fromJson(SharePreferenceUtil.getStringValue(Constants.TASKDETAIL + taskId, UserInfoActivity.this), TaskDetailResponse.class);
                TaskDetailResponse taskType = gson.fromJson(SharePreferenceUtil.getStringValue(Constants.RECEPTIONIST + taskId, UserInfoActivity.this), TaskDetailResponse.class);
                if (taskType == null) {
                    taskType = new TaskDetailResponse();
                }
                if(taskType.receptionistTypes.contains(initRe)){
                    taskType.receptionistTypes.remove(initRe);
                }
                taskType.receptionistTypes.add(newRe);
                SharePreferenceUtil.save(Constants.RECEPTIONIST + taskId, gson.toJson(taskType), UserInfoActivity.this);
                taskDetailResponse.receptionistTypes.set(index,newRe);
                SharePreferenceUtil.save(Constants.TASKDETAIL + taskId, gson.toJson(taskDetailResponse), UserInfoActivity.this);
                ToastUtil.show(UserInfoActivity.this, "保存成功");
                finish();
                break;
        }
    }

    public void initData(Receptionist receptionist) {
        if (receptionist == null) {
            return;
        }
        username.setText(receptionist.getName());
        position.setText(receptionist.getPosition());
        mobile.setText(receptionist.getPhone());
        if ("女".equals(receptionist.getGender())) {
            women.setChecked(true);
            man.setChecked(false);
        } else {
            women.setChecked(false);
            man.setChecked(true);
        }
        if (username.isPopupShowing()) {
            username.dismissDropDown();
        }
    }

    public void initDatas(ReceptionistTypeVo receptionist) {
        if (receptionist == null) {
            return;
        }
        username.setText(receptionist.getReceptionistName());
        position.setText(receptionist.getPosition());
        mobile.setText(receptionist.getPhone());
        String taskId = SharePreferenceUtil.getStringValue(Constants.TASKID, UserInfoActivity.this);
        int type = SharePreferenceUtil.getIntValue(Constants.TYPE + taskId, UserInfoActivity.this);
        if (receptionist.getGender() == null||receptionist.getGender() == 1) {
            women.setChecked(false);
            man.setChecked(true);
//            if(type==1){
//                women.setEnabled(false);
//            }

        }else{
            women.setChecked(true);
            man.setChecked(false);
//            if(type==1){
//                man.setEnabled(false);
//            }
        }
        newRe = receptionist;

        if(type ==1){
            btnSave.setVisibility(View.GONE);
            username.setEnabled(false);
            mobile.setEnabled(false);
            position.setEnabled(false);
            women.setClickable(false);
            man.setClickable(false);
        }else{
            btnSave.setVisibility(View.VISIBLE);
            username.setEnabled(true);
            mobile.setEnabled(true);
            position.setEnabled(true);
            women.setClickable(true);
            man.setClickable(true);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            finish();
            UserInfoActivity.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        }
        return false;
    }
}
