package com.saic.visit.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.saic.visit.R;
import com.saic.visit.common.BaseActivity;
import com.saic.visit.constant.Constants;
import com.saic.visit.model.TaskDetailResponse;
import com.saic.visit.utils.ScoreModelAdapter;
import com.saic.visit.utils.SharePreferenceUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ScoreActivity extends BaseActivity {
    @Bind(R.id.rela_back)
    RelativeLayout relaBack;
    @Bind(R.id.img_right)
    ImageView imgRight;
    @Bind(R.id.rela_right)
    RelativeLayout relaRight;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.list_score)
    ListView listScore;
    private ScoreModelAdapter mScoreAdapter;
    private String taskId;
    private TaskDetailResponse taskMode;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_score);
        ButterKnife.bind(this);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        Gson gson = new Gson();
        taskId = SharePreferenceUtil.getStringValue(Constants.TASKID, ScoreActivity.this);
        String modelData = SharePreferenceUtil.getStringValue(Constants.MODEL + taskId, ScoreActivity.this);
        taskMode = gson.fromJson(modelData, TaskDetailResponse.class);
        txtTitle.setText(getResources().getText(R.string.score_title));
        relaRight.setVisibility(View.INVISIBLE);
        mScoreAdapter = new ScoreModelAdapter(ScoreActivity.this, taskMode.models);
        listScore.setAdapter(mScoreAdapter);
    }

    @OnClick(R.id.rela_back)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rela_back:
                finish();
                break;
        }
    }

}


