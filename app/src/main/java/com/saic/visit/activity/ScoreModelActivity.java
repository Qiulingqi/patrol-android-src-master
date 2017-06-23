package com.saic.visit.activity;

import android.os.Bundle;
import android.util.Log;
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
import com.saic.visit.model.ModuleVo;
import com.saic.visit.model.Score;
import com.saic.visit.model.TaskDetailResponse;
import com.saic.visit.utils.SharePreferenceUtil;
import com.saic.visit.utils.SimpleTreeAdapter;
import com.saic.visit.utils.TreeListViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/6/8.
 */

/**
 * 为什么没有注释
 */
public class ScoreModelActivity extends BaseActivity {

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
    private List<Score> mDatas = new ArrayList<Score>();
    private TreeListViewAdapter mAdapter;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_score);
        ButterKnife.bind(this);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        txtTitle.setText(getResources().getText(R.string.score_title));
        relaRight.setVisibility(View.INVISIBLE);
        try {
            initDatas();
            mAdapter = new SimpleTreeAdapter<Score>(listScore, ScoreModelActivity.this, mDatas, 0);
            //Log.e("shuju",mDatas.toString());
            listScore.setAdapter(mAdapter);

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    @OnClick(R.id.rela_back)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rela_back:
                finish();
                ScoreModelActivity.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                break;
        }
    }

    private void initDatas() {

        // id , pid , label , 其他属性
        String taskId = SharePreferenceUtil.getStringValue(Constants.TASKID, ScoreModelActivity.this);
        String modelData = SharePreferenceUtil.getStringValue(Constants.MODEL + taskId, ScoreModelActivity.this);
        Gson gson = new Gson();
        TaskDetailResponse taskDetailResponse = gson.fromJson(modelData, TaskDetailResponse.class);
        List<ModuleVo> models = taskDetailResponse.models;
        int modelSize = models.size();
        for (int i = 0; i < modelSize; i++) {
            mDatas.add(new Score((int) models.get(i).getId().longValue(), 0, (i + 1) + models.get(i).getName(), 1));
            for (int j = 0, len1 = models.get(i).getCatalogVos().size(); j < len1; j++) {
                mDatas.add(new Score((int) models.get(i).getCatalogVos().get(j).getId().longValue(), (int) models.get(i).getId().longValue(), (i + 1) + "." + (j + 1) + models.get(i).getCatalogVos().get(j).getName(), 2));
                for (int k = 0, len2 = models.get(i).getCatalogVos().get(j).getCatalogVos().size(); k < len2; k++) {
                    mDatas.add(new Score((int) models.get(i).getCatalogVos().get(j).getCatalogVos().get(k).getId().longValue(), (int) models.get(i).getCatalogVos().get(j).getId().longValue(), (i + 1) + "." + (j + 1) + "." + (k + 1) + models.get(i).getCatalogVos().get(j).getCatalogVos().get(k).getName(), 3));
                    for (int m = 0, len3 = models.get(i).getCatalogVos().get(j).getCatalogVos().get(k).getCheckPointVos().size(); m < len3; m++) {
                        String items = "";
                        for (int n = 0, len4 = models.get(i).getCatalogVos().get(j).getCatalogVos().get(k).getCheckPointVos().get(m).getItemVos().size(); n < len4; n++) {
                            if (n != len4 - 1) {
                                items = items + models.get(i).getCatalogVos().get(j).getCatalogVos().get(k).getCheckPointVos().get(m).getItemVos().get(n).getName() + "@";
                            } else {
                                items = items + models.get(i).getCatalogVos().get(j).getCatalogVos().get(k).getCheckPointVos().get(m).getItemVos().get(n).getName();
                            }

                        }
                        mDatas.add(new Score((int) models.get(i).getCatalogVos().get(j).getCatalogVos().get(k).getCheckPointVos().get(m).getId().longValue(), (int) models.get(i).getCatalogVos().get(j).getCatalogVos().get(k).getId().longValue(), models.get(i).getCatalogVos().get(j).getCatalogVos().get(k).getCheckPointVos().get(m).getName() + "@" + items, 4, models.get(i).getCatalogVos().get(j).getCatalogVos().get(k).getCheckPointVos().get(m).getDesc()));
                    }
                }
            }
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            ScoreModelActivity.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        }
        return false;
    }
}
