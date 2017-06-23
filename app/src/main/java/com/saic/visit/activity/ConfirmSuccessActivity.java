package com.saic.visit.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.saic.visit.R;
import com.saic.visit.common.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/5/19.
 */
public class ConfirmSuccessActivity extends BaseActivity {
    @Bind(R.id.rela_back)
    RelativeLayout relaBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.img_right)
    ImageView imgRight;
    @Bind(R.id.rela_right)
    RelativeLayout relaRight;
    @Bind(R.id.visit_no)
    TextView visitNo;
    @Bind(R.id.back)
    Button back;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.layout_confirm_success);
        ButterKnife.bind(this);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        relaRight.setVisibility(View.INVISIBLE);
        txtTitle.setText(getResources().getText(R.string.task_title));
    }

    @OnClick({R.id.rela_back, R.id.back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rela_back:
                setResult(10);
                finish();
                ConfirmSuccessActivity.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                break;
            case R.id.back:
                setResult(10);
                finish();
                ConfirmSuccessActivity.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                break;
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            setResult(10);
            finish();
            ConfirmSuccessActivity.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        }

        return false;

    }
}
