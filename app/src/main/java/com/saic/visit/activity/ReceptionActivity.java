package com.saic.visit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.saic.visit.R;
import com.saic.visit.common.BaseActivity;
import com.saic.visit.constant.Constants;
import com.saic.visit.model.Receptionist;
import com.saic.visit.model.ReceptionistTypeVo;
import com.saic.visit.model.TaskDetailResponse;
import com.saic.visit.model.TaskInfoVo;
import com.saic.visit.utils.ReceptionAdapter;
import com.saic.visit.utils.SharePreferenceUtil;
import com.saic.visit.utils.TaskAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by liuhui on 2016/5/17.
 */
public class ReceptionActivity extends BaseActivity {
    @Bind(R.id.rela_back)
    RelativeLayout relaBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.img_right)
    ImageView imgRight;
    @Bind(R.id.rela_right)
    RelativeLayout relaRight;
    @Bind(R.id.list_reception)
    ListView listReception;
    private List<ReceptionistTypeVo> reception = new ArrayList<ReceptionistTypeVo>();
    private ReceptionAdapter adapter;
    TaskDetailResponse taskListResponse;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_reception);
        ButterKnife.bind(this);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        txtTitle.setText(getIntent().getExtras().getString(Constants.TASK));
        relaRight.setVisibility(View.INVISIBLE);
        initData();

    }

    @OnClick({R.id.rela_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rela_back:
                finish();
                ReceptionActivity.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && resultCode == 100){
            initData();
        }

    }

    private void initData(){
        Gson gsonss = new Gson();
        String taskId = SharePreferenceUtil.getStringValue(Constants.TASKID, ReceptionActivity.this);
        String data = SharePreferenceUtil.getStringValue(Constants.TASKDETAIL + taskId, ReceptionActivity.this);
        taskListResponse = gsonss.fromJson(data, TaskDetailResponse.class);
        reception = taskListResponse.receptionistTypes;
        adapter = new ReceptionAdapter(ReceptionActivity.this, reception);
        listReception.setAdapter(adapter);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            finish();
            ReceptionActivity.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        }
        return false;
    }
}
