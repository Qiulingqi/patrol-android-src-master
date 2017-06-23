package com.saic.visit.common;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.saic.visit.utils.DialogUtil;
import com.saic.visit.utils.ToastUtil;

public abstract class BaseActivity extends FragmentActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView(savedInstanceState);
        init(savedInstanceState);
    }
    protected abstract void initView(Bundle savedInstanceState);
    protected abstract void init(Bundle savedInstanceState);
    protected void errorStatus(VolleyError error){
        ToastUtil.show(BaseActivity.this,"网络请求错误");
    }
    protected Response.ErrorListener volleyError = new Response.ErrorListener(){

        @Override
        public void onErrorResponse( VolleyError error ) {
            DialogUtil.dismisLoading();
            errorStatus(error);
        }
    };
}

