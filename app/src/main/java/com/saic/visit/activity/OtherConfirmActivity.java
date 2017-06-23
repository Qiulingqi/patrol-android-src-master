package com.saic.visit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.saic.visit.R;
import com.saic.visit.common.BaseActivity;
import com.saic.visit.constant.Constants;
import com.saic.visit.http.ResponseSupport;
import com.saic.visit.http.VolleyRequestManager;
import com.saic.visit.model.CatalogVo;
import com.saic.visit.model.LogRequest;
import com.saic.visit.model.OtherConfirmRequest;
import com.saic.visit.model.QuestionsLog;
import com.saic.visit.utils.DialogUtil;
import com.saic.visit.utils.NetWorkUtil;
import com.saic.visit.utils.PopWindowAdapter;
import com.saic.visit.utils.SharePreferenceUtil;
import com.saic.visit.utils.StringUtils;
import com.saic.visit.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OtherConfirmActivity extends BaseActivity implements PopupWindow.OnDismissListener {
    @Bind(R.id.rela_back)
    RelativeLayout relaBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.img_right)
    ImageView imgRight;
    @Bind(R.id.rela_right)
    RelativeLayout relaRight;
    @Bind(R.id.txt_style)
    TextView txtStyle;
    @Bind(R.id.btn_save)
    Button btnSave;
    @Bind(R.id.view)
    View view;
    @Bind(R.id.edit_reason)
    EditText editReason;
    private PopupWindow popView;
    private PopWindowAdapter adapter;
    private ListView listPop;
    String[] stringArray;
    int[] integerArray;
    private int type;
    private List<OtherConfirmRequest> mList;
    private Gson gson;
    private String taskId;
    private List<CatalogVo> delItems,addItems,modifyItems;
    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_other_confirm);
        ButterKnife.bind(this);
        taskId = SharePreferenceUtil.getStringValue(Constants.TASKID,OtherConfirmActivity.this);
        gson = new Gson();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        relaRight.setVisibility(View.INVISIBLE);
        txtTitle.setText(getResources().getText(R.string.other_title));
        stringArray = getResources().getStringArray(R.array.other_confirm_style);
        integerArray = getResources().getIntArray(R.array.other_confirm_type);
        txtStyle.setText(stringArray[0]);
        type = integerArray[0];
        mList = new ArrayList<OtherConfirmRequest>();
        for (int i = 0; i < stringArray.length; i++) {
            OtherConfirmRequest otherConfirm = new OtherConfirmRequest("");
            otherConfirm.setType(integerArray[i]);
            otherConfirm.setReason(stringArray[i]);
            mList.add(otherConfirm);
        }
    }


    @OnClick({R.id.rela_back, R.id.txt_style, R.id.btn_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rela_back:
                finish();
                OtherConfirmActivity.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                break;
            case R.id.txt_style:
                initPopView();
                if (!popView.isShowing()) {
                    popView.showAsDropDown(view);
                } else {
                    popView.dismiss();
                }
                break;
            case R.id.btn_save:

                if (StringUtils.isEmpty(editReason.getText().toString().trim())) {
                    ToastUtil.show(OtherConfirmActivity.this, "请输入原因详述");
                    return;
                }
                if(!NetWorkUtil.isNetworkConnected(OtherConfirmActivity.this)){
                    ToastUtil.show(OtherConfirmActivity.this, "网络连接不可用");
                    return;
                }

                OtherConfirmRequest lur = new OtherConfirmRequest("Task/affirmOther?taskId="+taskId+"&otherType="+type+"&reason="+editReason.getText().toString());
                DialogUtil.showLoading(OtherConfirmActivity.this);
                VolleyRequestManager.getInstance(this).startHttpGetRequest(this, lur,
                        ResponseSupport.class, new Response.ListenerV2<ResponseSupport>() {
                            @Override
                            public void onResponse(ResponseSupport response, Map<String, String> headers) {
                                if (!VolleyRequestManager.realResponseResultSupport(OtherConfirmActivity.this, response, null, true))
                                    return;
                                new Thread() {
                                    public void run() {
                                        uploadLog();
                                    }
                                }.start();

                            }
                        }, volleyError);
                break;
        }
    }

    private void initPopView() {
        ArrayList<String> styles = new ArrayList<String>();

        adapter = new PopWindowAdapter(this, mList);
        if (popView == null) {
            View contentView = View.inflate(OtherConfirmActivity.this, R.layout.pop_layout, null);
            LinearLayout mLayout = (LinearLayout) contentView.findViewById(R.id.linear_pop);
            mLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popView.dismiss();
                }
            });
            popView = new PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            listPop = (ListView) contentView.findViewById(R.id.list_pop);
            listPop.setAdapter(adapter);
            popView.setFocusable(true);
            popView.setFocusable(true);
            popView.setOutsideTouchable(true);
            popView.setOnDismissListener(this);
        }
    }

    public void setTxtModel(OtherConfirmRequest name) {
        txtStyle.setText(name.getReason());
        type = name.getType();
        popView.dismiss();
    }

    @Override
    public void onDismiss() {

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == 10) {
            setResult(10);
            finish();
            OtherConfirmActivity.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            finish();
            OtherConfirmActivity.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        }
        return false;
    }

    private void uploadLog() {
        LogRequest lr = new LogRequest("log/saveOperatorLog");
        lr.setSupervisorId(SharePreferenceUtil.getLongValue(Constants.SUPERVISORID, OtherConfirmActivity.this));
        lr.setSurveyId(Long.parseLong(taskId));
        LogRequest saveLog = gson.fromJson(SharePreferenceUtil.getStringValue(Constants.ITEMDETAILS+taskId, OtherConfirmActivity.this),LogRequest.class);
        if(saveLog == null||saveLog.getOperatorLogDetails()==null){
            return;
        }
        QuestionsLog log = new QuestionsLog();
        log.setOperatorTime(StringUtils.getTimestamp(System.currentTimeMillis()));
        log.setOperatorLog("【店总确认】 确认方式：" + txtStyle.getText().toString() + editReason.getText().toString());
        saveLog.getOperatorLogDetails().add(log);
        lr.setOperatorLogDetails(saveLog.getOperatorLogDetails());
        VolleyRequestManager.getInstance(this).startHttpPostRequest(this, lr, ResponseSupport.class, new Response.ListenerV2<ResponseSupport>() {
            @Override
            public void onResponse(ResponseSupport response, Map<String, String> headers) throws Exception {
                if (!VolleyRequestManager.realResponseResultSupport(OtherConfirmActivity.this, response, null, true))
                    return;
                Intent intent = new Intent(OtherConfirmActivity.this,ConfirmSuccessActivity.class);
                startActivityForResult(intent, 200);

            }
        }, volleyError);
    }

}
