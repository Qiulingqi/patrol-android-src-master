package com.saic.visit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.saic.visit.R;
import com.saic.visit.common.BaseActivity;
import com.saic.visit.constant.Constants;
import com.saic.visit.http.ResponseSupport;
import com.saic.visit.http.VolleyRequestManager;
import com.saic.visit.model.LoginRequest;
import com.saic.visit.model.LoginResponse;
import com.saic.visit.utils.DialogUtil;
import com.saic.visit.utils.NetWorkUtil;
import com.saic.visit.utils.SharePreferenceUtil;
import com.saic.visit.utils.StringUtils;
import com.saic.visit.utils.ToastUtil;
import com.saic.visit.widget.ClearEditText;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by liuhui on 2016/5/13.
 * 登陆
 */
public class LoginActivity extends BaseActivity {

    @Bind(R.id.username)
    ClearEditText username;
    @Bind(R.id.password)
    ClearEditText password;
    @Bind(R.id.btn_login)
    Button btnLogin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        username.setText(SharePreferenceUtil.getStringValue(Constants.USERNAME,LoginActivity.this));
        password.setText(SharePreferenceUtil.getStringValue(Constants.PASSWORD,LoginActivity.this));
    }

    @OnClick(R.id.btn_login)
    public void onClick() {
        String userName = username.getText().toString();
        String passWord = password.getText().toString();
        if(StringUtils.isEmpty(userName)){
            ToastUtil.show(LoginActivity.this,"请输入登录名！");
            return;
        }
        if(StringUtils.isEmpty(passWord)){
            ToastUtil.show(LoginActivity.this,"请输入密码！");
            return;
        }
        if(!NetWorkUtil.isNetworkConnected(LoginActivity.this)){
            ToastUtil.show(LoginActivity.this,"网络连接不可用");
            return;
        }

        if(!StringUtils.isEmpty(userName)&&!StringUtils.isEmpty(passWord)){
            SharePreferenceUtil.save(Constants.USERNAME, userName, LoginActivity.this);
            SharePreferenceUtil.save(Constants.PASSWORD, passWord, LoginActivity.this);
            LoginRequest lur = new LoginRequest("Supervisor/login?loginId="+userName+"&loginPwd="+passWord);
            DialogUtil.showLoading(LoginActivity.this);
            VolleyRequestManager.getInstance(this).startHttpGetRequest(this, lur,
                    ResponseSupport.class, new Response.ListenerV2<ResponseSupport>() {
                        @Override
                        public void onResponse(ResponseSupport response, Map<String, String> headers) {
                            if (!VolleyRequestManager.realResponseResultSupport(LoginActivity.this, response, null, true))
                                return;
                            Gson gsonss = new Gson();
                            LoginResponse loginResponse = gsonss.fromJson(gsonss.toJson(response.obj), LoginResponse.class);
                            SharePreferenceUtil.saveInt(Constants.USERID, loginResponse.supervisor.getId(), LoginActivity.this);
                            SharePreferenceUtil.saveLong(Constants.SUPERVISORID, loginResponse.supervisor.getId(), LoginActivity.this);
                            Intent intent = new Intent(LoginActivity.this, MainActicity.class);
                            startActivity(intent);
                            LoginActivity.this.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                        }
                    }, volleyError);
        }

    }
}
