package com.saic.visit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.saic.visit.model.AffirmRequest;
import com.saic.visit.model.CatalogVo;
import com.saic.visit.model.LogRequest;
import com.saic.visit.model.QuestionsLog;
import com.saic.visit.model.SendMessageRequest;
import com.saic.visit.utils.DialogUtil;
import com.saic.visit.utils.NetWorkUtil;
import com.saic.visit.utils.SharePreferenceUtil;
import com.saic.visit.utils.StringUtils;
import com.saic.visit.utils.ToastUtil;
import com.saic.visit.widget.gridpasswordview.GridPasswordView;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by liuhui on 2016/5/16.
 */
public class TaskConfirmActivity extends BaseActivity {
    @Bind(R.id.rela_back)
    RelativeLayout relaBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.img_right)
    ImageView imgRight;
    @Bind(R.id.rela_right)
    RelativeLayout relaRight;
    @Bind(R.id.input_mobile)
    EditText inputMobile;
    @Bind(R.id.button_send)
    TextView buttonSend;
    @Bind(R.id.comfirm_password)
    GridPasswordView comfirmPassword;
    @Bind(R.id.btn_save)
    Button btnSave;
    @Bind(R.id.confirm_other)
    TextView confirmOther;
    private long length = 60 * 1000;// 倒计时长度,这里给了默认60秒
    private Timer t;
    private TimerTask tt;
    private long time;
    private Gson gson;
    private String taskId;
    private List<CatalogVo> delItems,addItems,modifyItems;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_task_confirm);
        ButterKnife.bind(this);
        //把经销商电话放进去。
        inputMobile.setText(getIntent().getStringExtra(ShopVisitActivity.INTENT_EXTRA_PHONE));
        taskId = SharePreferenceUtil.getStringValue(Constants.TASKID, TaskConfirmActivity.this);
        gson = new Gson();

    }

    @Override
    protected void init(Bundle savedInstanceState) {
        relaRight.setVisibility(View.INVISIBLE);
        txtTitle.setText(getResources().getText(R.string.confirm_shop_title));
    }


    @OnClick({R.id.rela_back, R.id.button_send, R.id.comfirm_password, R.id.btn_save, R.id.confirm_other})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rela_back:
                setResult(100);
                finish();
                TaskConfirmActivity.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                break;
            case R.id.button_send:
                if (StringUtils.isEmpty(inputMobile.getText().toString().trim())) {
                    ToastUtil.show(TaskConfirmActivity.this, "请输入手机号码");
                    return;
                }
                if (!StringUtils.matchs(Constants.REGEX_MOBILE, inputMobile.getText().toString())) {
                    ToastUtil.show(TaskConfirmActivity.this, "请输入正确的手机号码");
                    return;
                }
                if (!NetWorkUtil.isNetworkConnected(TaskConfirmActivity.this)) {
                    ToastUtil.show(TaskConfirmActivity.this, "网络连接不可用");
                    return;
                }
                SendMessageRequest lur = new SendMessageRequest("Task/affirmtel?taskId=" + taskId + "&tel=" + inputMobile.getText().toString());
                DialogUtil.showLoading(TaskConfirmActivity.this);
                VolleyRequestManager.getInstance(this).startHttpGetRequest(this, lur,
                        ResponseSupport.class, new Response.ListenerV2<ResponseSupport>() {
                            @Override
                            public void onResponse(ResponseSupport response, Map<String, String> headers) {
                                if (!VolleyRequestManager.realResponseResultSupport(TaskConfirmActivity.this, response, null, true))
                                    return;
                                ToastUtil.show(TaskConfirmActivity.this, "短信已发送，请注意查收");
                            }
                        }, volleyError);
                initTimer();
                buttonSend.setText(time / 1000 + "s");
                buttonSend.setEnabled(false);
                t.schedule(tt, 0, 1000);

                break;
            case R.id.comfirm_password:
                break;
            case R.id.btn_save:

           //     uploadLog();
                if (comfirmPassword.getPassWord() == null || "".equals(comfirmPassword.getPassWord())) {
                    ToastUtil.show(TaskConfirmActivity.this, "请输入动态口令");
                    return;
                }
                if (comfirmPassword.getPassWord().length() < 6) {
                    ToastUtil.show(TaskConfirmActivity.this, "请输入六位动态口令");
                    return;
                }
                AffirmRequest affirmRequest = new AffirmRequest("Task/affirm?taskId=" + taskId + "&security=" + comfirmPassword.getPassWord());
                DialogUtil.showLoading(TaskConfirmActivity.this);
                VolleyRequestManager.getInstance(this).startHttpGetRequest(this, affirmRequest,
                        ResponseSupport.class, new Response.ListenerV2<ResponseSupport>() {
                            @Override
                            public void onResponse(ResponseSupport response, Map<String, String> headers) {
                                if (!VolleyRequestManager.realResponseResultSupport(TaskConfirmActivity.this, response, null, true))
                                    return;
                                new Thread() {
                                    public void run() {
                                        uploadLog();
                                    }
                                }.start();
                            }
                        }, volleyError);
                break;
            case R.id.confirm_other:
                Intent intent = new Intent(TaskConfirmActivity.this, OtherConfirmActivity.class);
                startActivityForResult(intent, 200);
                TaskConfirmActivity.this.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;
        }
    }

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            buttonSend.setText(time / 1000 + "s");
            time -= 1000;
            if (time < 0) {
                buttonSend.setEnabled(true);
                buttonSend.setText("重新获取");
                clearTimer();
            }
        }

        ;
    };


    private void clearTimer() {
        if (tt != null) {
            tt.cancel();
            tt = null;
        }
        if (t != null)
            t.cancel();
        t = null;
    }

    private void initTimer() {
        time = length;
        t = new Timer();
        tt = new TimerTask() {

            @Override
            public void run() {
                handler.sendEmptyMessage(0x01);
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 10) {
            setResult(20);
            finish();
            TaskConfirmActivity.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        }
        if (requestCode == 200 && resultCode == 10) {
            setResult(20);
            finish();
            TaskConfirmActivity.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(100);
            finish();
            TaskConfirmActivity.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        }

        return false;

    }
    private void uploadLog() {
        LogRequest lr = new LogRequest("log/saveOperatorLog");
        lr.setSupervisorId(SharePreferenceUtil.getLongValue(Constants.SUPERVISORID, TaskConfirmActivity.this));
        lr.setSurveyId(Long.parseLong(taskId));
        LogRequest saveLog = gson.fromJson(SharePreferenceUtil.getStringValue(Constants.ITEMDETAILS+taskId, TaskConfirmActivity.this),LogRequest.class);
        QuestionsLog log = new QuestionsLog();
        log.setOperatorTime(StringUtils.getTimestamp(System.currentTimeMillis()));
        log.setOperatorLog("【店总确认】 确认方式：" + inputMobile.getText().toString());
        if(saveLog == null||saveLog.getOperatorLogDetails() == null) {
           lr.getOperatorLogDetails().add(log);
        }else{
            saveLog.getOperatorLogDetails().add(log);
            lr.setOperatorLogDetails(saveLog.getOperatorLogDetails());
        }

        VolleyRequestManager.getInstance(this).startHttpPostRequest(this, lr, ResponseSupport.class, new Response.ListenerV2<ResponseSupport>() {
            @Override
            public void onResponse(ResponseSupport response, Map<String, String> headers) throws Exception {
                if (!VolleyRequestManager.realResponseResultSupport(TaskConfirmActivity.this, response, null, true))
                    return;
                SharePreferenceUtil.save(Constants.DELITEMS + taskId, "", TaskConfirmActivity.this);
                SharePreferenceUtil.save(Constants.AddITEMS + taskId, "", TaskConfirmActivity.this);
                SharePreferenceUtil.save(Constants.MODIFYITEMS + taskId, "", TaskConfirmActivity.this);
                Intent intent = new Intent(TaskConfirmActivity.this, ConfirmSuccessActivity.class);
                TaskConfirmActivity.this.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                startActivityForResult(intent, 100);

            }
        }, volleyError);
    }

}
