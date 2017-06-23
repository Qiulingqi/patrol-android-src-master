package com.saic.visit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.saic.visit.R;
import com.saic.visit.common.BaseActivity;
import com.saic.visit.constant.Constants;
import com.saic.visit.http.ResponseSupport;
import com.saic.visit.http.VolleyRequestManager;
import com.saic.visit.model.TaskInfoVo;
import com.saic.visit.model.TaskListRequest;
import com.saic.visit.model.TaskListResponse;
import com.saic.visit.utils.DialogUtil;
import com.saic.visit.utils.NetWorkUtil;
import com.saic.visit.utils.SharePreferenceUtil;
import com.saic.visit.utils.TaskAdapter;
import com.saic.visit.utils.ToastUtil;
import com.saic.visit.widget.MongoPullToRefreshOrLoadMoreListView;

import java.util.ArrayList;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by liuhui on 2016/5/13.
 */
public class MainActicity extends BaseActivity {

    @Bind(R.id.rela_back)
    RelativeLayout relaBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.rela_right)
    RelativeLayout relaRight;
    @Bind(R.id.main_task)
    Button mainTask;
    @Bind(R.id.main_complete)
    Button mainComplete;
    @Bind(R.id.img_right)
    ImageView imgRight;
    @Bind(R.id.list_task)
    MongoPullToRefreshOrLoadMoreListView listTask;
    private ArrayList<TaskInfoVo> tasks = new ArrayList<TaskInfoVo>();
    private TaskAdapter adapter;
    private Integer status = null;
    private int zhaungtai=0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        txtTitle.setText(getResources().getText(R.string.main));
        relaBack.setVisibility(View.INVISIBLE);
        listTask.setAutoLoadMore(true);
        listTask.setCanLoadMore(false);
        listTask.setCanRefresh(true);
        getTaskList(status);
        listTask.setOnRefreshListener(new MongoPullToRefreshOrLoadMoreListView.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        zhaungtai=getIntent().getIntExtra("haha",0);
        if(zhaungtai==1){
            status = null;
            getTaskList(status);
            zhaungtai=0;
        }

    }

    @OnClick({R.id.rela_back, R.id.rela_right, R.id.main_task, R.id.main_complete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rela_right:
                finish();
                MainActicity.this.overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
                break;
            case R.id.main_task:
                mainTask.setBackgroundResource(R.drawable.blue_button);
                mainComplete.setBackgroundResource(R.drawable.white_button);
                DialogUtil.showLoading(MainActicity.this);
                status = null;
                getTaskList(status);
                break;
            case R.id.main_complete:
                mainTask.setBackgroundResource(R.drawable.white_button);
                mainComplete.setBackgroundResource(R.drawable.blue_button);
                DialogUtil.showLoading(MainActicity.this);
                status = 1;
                getTaskList(status);
                break;
        }
    }

    /**
     * 刷新动作 加载数据
     * @param status
     */
    public void getTaskList(Integer status){
        if(!NetWorkUtil.isNetworkConnected(MainActicity.this)){
            ToastUtil.show(MainActicity.this, "网络连接不可用");
            return;
        }else{
            DialogUtil.showLoading(MainActicity.this);
            //DialogUtil.dismisLoading();
        }
        int id = SharePreferenceUtil.getIntValue(Constants.USERID,MainActicity.this);
        TaskListRequest taskListRequest;
        if(status == null){
            taskListRequest = new TaskListRequest("Task/findBySupervisorId?supervisorId="+id);

        }else {
            taskListRequest = new TaskListRequest("Task/findBySupervisorId?supervisorId=" + id + "&status=" + status);
        }
        Log.d("this is ________",id+"");

        VolleyRequestManager.getInstance(this).startHttpGetRequest(this, taskListRequest,
                ResponseSupport.class, new Response.ListenerV2<ResponseSupport>() {
                    @Override
                    public void onResponse(ResponseSupport response, Map<String, String> headers) {
                        if (!VolleyRequestManager.realResponseResultSupport(MainActicity.this, response, null, true))
                            return;
                        Gson gsonss = new Gson();
                        TaskListResponse taskListResponse = gsonss.fromJson(gsonss.toJson(response.obj), TaskListResponse.class);
                        //Log.e("111111",taskListResponse.taskInfoVos.toString());
                        adapter = new TaskAdapter(MainActicity.this, taskListResponse.taskInfoVos);
                        System.out.println("this is "+taskListResponse.taskInfoVos.size());
                        listTask.setAdapter(adapter);
                        listTask.onRefreshComplete();
                    }
                }, volleyError);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode ==100 && resultCode == 100){
            mainTask.setBackgroundResource(R.drawable.blue_button);
            mainComplete.setBackgroundResource(R.drawable.white_button);
            //DialogUtil.showLoading(MainActicity.this);
            status = null;
            getTaskList(status);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            MainActicity.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        }
        return false;
    }
}
