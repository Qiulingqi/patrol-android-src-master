package com.saic.visit.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.saic.visit.R;
import com.saic.visit.common.BaseActivity;
import com.saic.visit.constant.Constants;
import com.saic.visit.model.CatalogVo;
import com.saic.visit.model.CheckPointVo;
import com.saic.visit.model.ItemVo;
import com.saic.visit.model.LogRequest;
import com.saic.visit.model.ModuleVo;
import com.saic.visit.model.QuestionVo;
import com.saic.visit.model.QuestionsLog;
import com.saic.visit.model.Score;
import com.saic.visit.model.TaskDetailResponse;
import com.saic.visit.utils.AddPhoto;
import com.saic.visit.utils.DialogUtil;
import com.saic.visit.utils.SharePreferenceUtil;
import com.saic.visit.utils.StringUtils;
import com.saic.visit.utils.VisitTreeAdapter;
import com.saic.visit.utils.superme.WeiboDialogUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *
 */
public class AddVisitActivity extends BaseActivity implements AddPhoto {

    @Bind(R.id.rela_right)
    RelativeLayout relaRight;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.list_visit)
    ListView listScore;
    private List<Score> mDatas = new ArrayList<Score>();
    //  private TreeListViewAdapter mAdapter;
    private VisitTreeAdapter mAdapter;
    private TaskDetailResponse taskDetailResponse;
    private String taskId;
    private Gson gsonss;
    private TaskDetailResponse taskMode;
    private List<CatalogVo> addItems;
    private LogRequest logs;
    public static String theFileName = null;

    public Context context;
    private String fileName;
    private String pathUrl;
    private String imageName;
    private String path3;
    private Dialog loadingDialog2;


    @Override
    protected void initView(Bundle savedInstanceState) {
        this.getWindow().setFlags(0x80000000, 0x80000000);
        if(savedInstanceState!=null){
            fileName=savedInstanceState.getString("filename");
        }
        setContentView(R.layout.activity_add_visit);
        ButterKnife.bind(this);
    }
    @Override
    protected void init(Bundle savedInstanceState) {
        txtTitle.setText("添加/修改记录");
        relaRight.setVisibility(View.INVISIBLE);
        try {
            initDatas();
            mAdapter = new VisitTreeAdapter(listScore, AddVisitActivity.this, mDatas, 0, AddVisitActivity.this);
            listScore.setAdapter(mAdapter);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.rela_back, R.id.btn_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rela_back:
            case R.id.btn_add:
                addData();
                break;
        }
    }

    private void addData() {

        List<CheckPointVo> selectPoint;
        List<ModuleVo> firstList = taskDetailResponse.models;
        List<CatalogVo> secondList = null;
        List<CatalogVo> threeList = null;
        List<CheckPointVo> fourList = null;
        List<ItemVo> fiveList = null;

        for (int i = 0; i < firstList.size(); i++) {
            secondList = firstList.get(i).getCatalogVos();
            for (int j = 0; j < secondList.size(); j++) {
                threeList = secondList.get(j).getCatalogVos();
                for (int k = 0; k < threeList.size(); k++) {
                    fourList = threeList.get(k).getCheckPointVos();
                    for (int x = 0; x < fourList.size(); x++) {
                        fiveList = fourList.get(x).getItemVos();
                        boolean isAdd = false;
                        //判断是否有添加
                        for (ItemVo itemVo : fiveList) {
                            //只要有一个check就为添加，仅有remark不添加
                            if (itemVo.getImageURLIst().size() > 0) {
                                isAdd = true;
                                break;
                            }
                        }
                        if (isAdd) {
                            QuestionVo mAddQuestionVo = new QuestionVo();
                            String remark = taskDetailResponse.models.get(i).getCatalogVos().get(j).getCatalogVos().get(k).getCheckPointVos().get(x).getRemark();
                            if (!StringUtils.isEmpty(remark)) {
                                mAddQuestionVo.setRemark(remark);
                            }
                            mAddQuestionVo.setRemark(remark);
                            selectPoint = new ArrayList<CheckPointVo>();
                            selectPoint.add(taskDetailResponse.models.get(i).getCatalogVos().get(j).getCatalogVos().get(k).getCheckPointVos().get(x));
                            addSingle(gsonss.fromJson(SharePreferenceUtil.getStringValue(Constants.MODEL + taskId, AddVisitActivity.this), TaskDetailResponse.class), mAddQuestionVo, selectPoint, i, j, k);
                        }
                    }
                }
            }
        }
        /**
         * 在这里不让他替换原来的数据
         */
        //  taskDetailResponse.models = taskMode.models;

        SharePreferenceUtil.save(Constants.TASKDETAIL + taskId, gsonss.toJson(taskDetailResponse), AddVisitActivity.this);

        saveLog();
    }

    private void addSingle(TaskDetailResponse taskList, QuestionVo mAddQuestionVo, List<CheckPointVo> selectPoint, int modelIndex, int linkIndexOne, int linkIndexTwo) {
        ModuleVo mAddModuleVo = taskList.models.get(modelIndex);
        CatalogVo catalogVo = taskList.models.get(modelIndex).getCatalogVos().get(linkIndexOne);
        CatalogVo catalogVo2 = taskList.models.get(modelIndex).getCatalogVos().get(linkIndexOne).getCatalogVos().get(linkIndexTwo);
        catalogVo2.setCheckPointVos(selectPoint);
        ArrayList<CatalogVo> catalist = new ArrayList<CatalogVo>();
        ArrayList<CatalogVo> catalist2 = new ArrayList<CatalogVo>();
        catalist2.add(catalogVo2);
        catalist.add(catalogVo);
        catalogVo.setCatalogVos(catalist2);
        catalogVo.setCurrentTime(StringUtils.getTimestamp(System.currentTimeMillis()));
        catalogVo.setModelName(mAddModuleVo.getName());
        //catalogVo 未新增的
        addItems.add(catalogVo);

        for (int i = 0; i < taskDetailResponse.survey.getModuleVos().size(); i++) {
            if (taskDetailResponse.survey.getModuleVos().get(i).getId().longValue() == mAddModuleVo.getId().longValue()) {
                //获取当前目录下的questions
                List<QuestionVo> questionVos = taskDetailResponse.survey.getModuleVos().get(i).getQuestionVos();
                int questionSize = questionVos.size();
                boolean exsitQuestion = isExsitQuestion(questionVos, questionSize, selectPoint);
                if (!exsitQuestion) {
                    mAddQuestionVo.setCatalogVos(catalist);
                    mAddQuestionVo.setModuleId(mAddModuleVo.getId());
                    taskDetailResponse.survey.getModuleVos().get(i).getQuestionVos().add(mAddQuestionVo);
                } else {
                    /**
                     * 如果已经添加过   那么就删除原来的重新给他赋值
                     */
                    mAddQuestionVo.setCatalogVos(catalist);
                    mAddQuestionVo.setModuleId(mAddModuleVo.getId());
                    taskDetailResponse.survey.getModuleVos().get(i).getQuestionVos().remove(0);
                    taskDetailResponse.survey.getModuleVos().get(i).getQuestionVos().add(mAddQuestionVo);
                    for (int j = 0; j < questionSize; j++) {
                        if (questionVos.get(j).getCatalogVos().get(0).getCatalogVos().get(0).getCheckPointVos().get(0).getId().longValue() == selectPoint.get(0).getId().longValue()) {
                            QuestionVo mAddQuestion = new QuestionVo();
                            mAddQuestion.setModuleId(mAddModuleVo.getId());
                            mAddQuestion.setCatalogVos(catalist);

                            //获取当前的items
                            List<ItemVo> itemVos = questionVos.get(j).getCatalogVos().get(0).getCatalogVos().get(0).getCheckPointVos().get(0).getItemVos();
                            List<ItemVo> itemVosAdd = selectPoint.get(0).getItemVos();
                            handleNewQuestion(mAddQuestion, itemVosAdd);
                            int size = itemVos.size();
                            for (int k = 0; k < size; k++) {
                                if (itemVos.get(k).ischeck() || itemVosAdd.get(k).ischeck()) {
                                    questionVos.get(j).getCatalogVos().get(0).getCatalogVos().get(0).getCheckPointVos().get(0).getItemVos().get(k).setIscheck(true);
                                } else {
                                    questionVos.get(j).getCatalogVos().get(0).getCatalogVos().get(0).getCheckPointVos().get(0).getItemVos().get(k).setIscheck(false);
                                }
                            }
                            questionVos.get(j).setRemark(StringUtils.isEmpty(questionVos.get(j).getRemark()) ? "" : (questionVos.get(j).getRemark() + getResources().getText(R.string.tran)) + (StringUtils.isEmpty(mAddQuestionVo.getRemark()) ? "" : mAddQuestionVo.getRemark()));
                            taskDetailResponse.survey.getModuleVos().get(i).setQuestionVos(questionVos);

                        }
                    }
                }
            }
                /*taskListResponse.models = taskMode.models;
                Gson gson = new Gson();
                String taskList = gson.toJson(taskListResponse);
                SharePreferenceUtil.save(Constants.TASKDETAIL + taskId, taskList, AddShopVisitActivity.this);*/
        }

    }


    private boolean isExsitQuestion(List<QuestionVo> questionVos, int questionSize, List<CheckPointVo> selectPoint) {
        for (int j = 0; j < questionSize; j++) {
            if (questionVos.get(j).getCatalogVos().get(0).getCatalogVos().get(0).getCheckPointVos().get(0).getId().longValue() == selectPoint.get(0).getId().longValue()) {
                return true;
            }
        }
        return false;
    }
    QuestionVo existQuestion;
    private void handleNewQuestion(QuestionVo mAddQuestion, List<ItemVo> itemVosAdd) {
        String stringValue = SharePreferenceUtil.getStringValue(Constants.ADDQUESTION + taskId, AddVisitActivity.this);
        List<QuestionVo> addTaskList = new ArrayList<QuestionVo>();
        ModuleVo moduleVo = gsonss.fromJson(stringValue, ModuleVo.class);
        addTaskList.add(mAddQuestion);
        //添加相同
        existQuestion=null;
        List<QuestionVo> questionVos = null;
        if (moduleVo != null && moduleVo.getQuestionVos() != null) {
            int newQuestionSize = moduleVo.getQuestionVos().size();
            questionVos = moduleVo.getQuestionVos();
            for (int i = 0; i < newQuestionSize; i++) {
                if (questionVos.get(i).getModuleId().longValue() == mAddQuestion.getModuleId().longValue()) {
                    existQuestion = questionVos.get(i);
                    List<ItemVo> itemVos = questionVos.get(i).getCatalogVos().get(0).getCatalogVos().get(0).getCheckPointVos().get(0).getItemVos();
                    for (int k = 0, len = questionVos.get(i).getCatalogVos().get(0).getCatalogVos().get(0).getCheckPointVos().get(0).getItemVos().size(); k < len; k++) {
                        if (itemVos.get(k).ischeck() || itemVosAdd.get(k).ischeck()) {
                            questionVos.get(i).getCatalogVos().get(0).getCatalogVos().get(0).getCheckPointVos().get(0).getItemVos().get(k).setIscheck(true);
                        } else {
                            questionVos.get(i).getCatalogVos().get(0).getCatalogVos().get(0).getCheckPointVos().get(0).getItemVos().get(k).setIscheck(false);
                        }
                    }
                    return;
                }
            }
        } else {
            moduleVo = new ModuleVo();
        }
        if (existQuestion == null) {
            if (moduleVo.getQuestionVos() != null) {
                moduleVo.getQuestionVos().add(mAddQuestion);
            } else {
                List<QuestionVo> qv = new ArrayList<QuestionVo>();
                qv.add(mAddQuestion);
                moduleVo.setQuestionVos(qv);
            }

        } else {
            moduleVo.setQuestionVos(questionVos);
        }
        SharePreferenceUtil.save(Constants.ADDQUESTION, gsonss.toJson(moduleVo), AddVisitActivity.this);
    }


    private void initDatas() {
        gsonss = new Gson();
        taskId = SharePreferenceUtil.getStringValue(Constants.TASKID, AddVisitActivity.this);
        taskMode = gsonss.fromJson(SharePreferenceUtil.getStringValue(Constants.MODEL + taskId, AddVisitActivity.this), TaskDetailResponse.class);
        addItems = new ArrayList<>();
        // id , pid , label , 其他属性.
//        String modelData = SharePreferenceUtil.getStringValue(Constants.MODEL + taskId, AddVisitActivity.this);
        String modelData = SharePreferenceUtil.getStringValue(Constants.TASKDETAIL + taskId, AddVisitActivity.this);
        Gson gson = new Gson();
        taskDetailResponse = gson.fromJson(modelData, TaskDetailResponse.class);
        List<ModuleVo> models = taskDetailResponse.models;
        int modelSize = models.size();
        for (int i = 0; i < modelSize; i++) {
            mDatas.add(new Score((int) models.get(i).getId().longValue(), 0, (i + 1) + models.get(i).getName(), 1));
            for (int j = 0, len1 = models.get(i).getCatalogVos().size(); j < len1; j++) {
                mDatas.add(new Score((int) models.get(i).getCatalogVos().get(j).getId().longValue(), (int) models.get(i).getId().longValue(), (i + 1) + "." + (j + 1) + models.get(i).getCatalogVos().get(j).getName(), 2));
                for (int k = 0, len2 = models.get(i).getCatalogVos().get(j).getCatalogVos().size(); k < len2; k++) {
                    mDatas.add(new Score((int) models.get(i).getCatalogVos().get(j).getCatalogVos().get(k).getId().longValue(), (int) models.get(i).getCatalogVos().get(j).getId().longValue(), (i + 1) + "." + (j + 1) + "." + (k + 1) + models.get(i).getCatalogVos().get(j).getCatalogVos().get(k).getName(), 3));
                    for (int m = 0, len3 = models.get(i).getCatalogVos().get(j).getCatalogVos().get(k).getCheckPointVos().size(); m < len3; m++) {
                        mDatas.add(new Score((int) models.get(i).getCatalogVos().get(j).getCatalogVos().get(k).getCheckPointVos().get(m).getId().longValue(), (int) models.get(i).getCatalogVos().get(j).getCatalogVos().get(k).getId().longValue(), models.get(i).getCatalogVos().get(j).getCatalogVos().get(k).getCheckPointVos().get(m).getName(), 4, models.get(i).getCatalogVos().get(j).getCatalogVos().get(k).getCheckPointVos().get(m).getDesc()));
                        mDatas.add(new Score(Integer.MAX_VALUE, (int) models.get(i).getCatalogVos().get(j).getCatalogVos().get(k).getCheckPointVos().get(m).getId().longValue(), "", 5, "", models.get(i).getCatalogVos().get(j).getCatalogVos().get(k).getCheckPointVos().get(m)));
                    }
                }
            }
        }

    }

    /**
     * 在系统的返回键加上监听  无论有没有数据  都进行 保存操作
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
           /* finish();
            AddVisitActivity.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);*/
            //loadingDialog1=WeiboDialogUtils.createLoadingDialog(AddVisitActivity.this, "正在保存.....");
            //loadingDialog2=WeiboDialogUtils.createLoadingDialog(AddVisitActivity.this,"正在保存.....");
            addData();
        }
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            //loadingDialog1=WeiboDialogUtils.createLoadingDialog(AddVisitActivity.this, "正在保存.....");
            //loadingDialog2=WeiboDialogUtils.createLoadingDialog(AddVisitActivity.this,"正在保存.....");
            addData();
        }

        return false;
    }

    private void saveLog() {
        //获取未上传的历史日志
        StringBuffer stringBuffer = new StringBuffer();
        LogRequest log = gsonss.fromJson(SharePreferenceUtil.getStringValue(Constants.ITEMDETAILS + taskId, AddVisitActivity.this), LogRequest.class);
        //新增日志拼接
        stringBuffer.append("【添加扣分项】" + "<br/>");
        for (int i = 0, len = addItems.size(); i < len; i++) {
            stringBuffer.append((i + 1) + "、" + addItems.get(i).getModelName());
            stringBuffer.append("/" + addItems.get(i).getName());
            stringBuffer.append("/" + addItems.get(i).getCatalogVos().get(0).getName());
            stringBuffer.append("/" + addItems.get(i).getCatalogVos().get(0).getCheckPointVos().get(0).getName() + "<br/>" + "扣分原因：" + "<br/>");
            int k = 1;
            for (int j = 0, length = addItems.get(i).getCatalogVos().get(0).getCheckPointVos().get(0).getItemVos().size(); j < length; j++) {
                if (addItems.get(i).getCatalogVos().get(0).getCheckPointVos().get(0).getItemVos().get(j).ischeck()) {
                    stringBuffer.append(k + "、" + addItems.get(i).getCatalogVos().get(0).getCheckPointVos().get(0).getItemVos().get(j).getName() + "<br/>");
                    k++;
                }
            }
            stringBuffer.append("备注：" + (addItems.get(i).getCatalogVos().get(0).getCheckPointVos().get(0).getRemark() == null ? "" : addItems.get(i).getCatalogVos().get(0).getCheckPointVos().get(0).getRemark()) + "<br/>");
        }
        if (log == null || log.getOperatorLogDetails() == null) {
            log = new LogRequest("");
        }
        QuestionsLog questionsLog = new QuestionsLog();
        questionsLog.setOperatorTime(StringUtils.getTimestamp(System.currentTimeMillis()));
        questionsLog.setOperatorLog(stringBuffer.toString());
        log.getOperatorLogDetails().add(questionsLog);
        SharePreferenceUtil.save(Constants.ITEMDETAILS + taskId, gsonss.toJson(log, LogRequest.class), AddVisitActivity.this);
        setResult(100);
        finish();
        AddVisitActivity.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

    }

    /**
     * 接口里边的方法
     *
     * @param i
     */
    @Override
    public void getPhote(String i) {
        theFileName = i;

        openTakePhoto();
    }

    private void openTakePhoto() {

/*        String state = Environment.getExternalStorageState(); //拿到sdcard是否可用的状态码
        if (state.equals(Environment.MEDIA_MOUNTED)) {   //如果可用
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 1);
        } else {
            ToastUtil.show(AddVisitActivity.this, "不可用");
        }*/


        //  图片不压缩  需要提前设置好存储路径
        Intent intentPhote = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentPhote.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

        File out = new File(getPhotopath());
            Uri uri = Uri.fromFile(out);
            // 获取拍照后未压缩的原图片，并保存在uri路径中
            intentPhote.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//                intentPhote.putExtra(MediaStore.Images.Media.ORIENTATION, 180);
            startActivityForResult(intentPhote, 2000);






    }

    /**
     * 获取原图片存储路径
     *
     * @return
     */
    private String getPhotopath() {
        // 照片全路径
        // 文件夹路径
        fileName = "";
        pathUrl = MyApplication.path1;
        imageName = new DateFormat().format("yyyyMMddhhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpeg";
        if(TaskDetailActivity.Jingxiaoshang!=null&&TaskDetailActivity.JingCode!=null){
            imageName = TaskDetailActivity.Jingxiaoshang + "_" + TaskDetailActivity.JingCode + "_" + theFileName + "_" + imageName;
        }else{
            imageName="";
        }
        //File file = new File(pathUrl);
        //  file.mkdirs();// 创建文件夹
        fileName = pathUrl + imageName;
        return fileName;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("filename",fileName);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2000 && resultCode == Activity.RESULT_OK) {
            /**
             * 压缩两套图 为了展示效果  先拿出乱进行压缩  在存放进另一个目录
             */
                FileOutputStream b = null;
                Bitmap getimage = getimage(fileName);
                try {
                    path3 = MyApplication.path3;
                    File file = new File(path3);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    b = new FileOutputStream(path3 + imageName);
                    getimage.compress(Bitmap.CompressFormat.JPEG, 10, b);// 把数据写入文件
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                fileName = pathUrl + imageName;
                if (null != fileName && !"".equals(fileName) && null != imageName && !"".equals(imageName)) {
                    mAdapter.setBoymap(path3 + imageName, imageName);
                }
            mAdapter.notifyDataSetChanged();
        try {

        }catch (Exception e){
            e.printStackTrace();
        }


        }

    }

    /**
     * 压缩图片的方法
     */
    private Bitmap getimage(String srcPath) {
            BitmapFactory.Options newOpts = new BitmapFactory.Options();
            //开始读入图片，此时把options.inJustDecodeBounds 设回true了
            newOpts.inJustDecodeBounds = true;
            Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);//此时返回bm为空
            int w = newOpts.outWidth;
            int h = newOpts.outHeight;
            //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
            float hh = 800f;//这里设置高度为800f
            float ww = 480f;//这里设置宽度为480f
            //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
            int be = 1;//be=1表示不缩放
            if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
                be = (int) (newOpts.outWidth / ww);
            } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
                be = (int) (newOpts.outHeight / hh);
            }
            if (be <= 0)
                be = 1;
            newOpts.inSampleSize = be;//设置缩放比例
            //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
            newOpts.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
            return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
    }

    private Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {    //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }
}

/*    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            String sdStatus = Environment.getExternalStorageState();
            if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                Log.i("TestFile",
                        "SD card is not avaiable/writeable right now.");
                return;
            }
            String names = new DateFormat().format("yyyyMMddhhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpeg";
            String name = TaskDetailActivity.Jingxiaoshang + "_" + TaskDetailActivity.JingCode + "_" + theFileName +"_" + names;
            Bundle bundle = data.getExtras();
            Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
            FileOutputStream b = null;
            //???????????????????????????????为什么不能直接保存在系统相册位置呢？？？？？？？？？？？？
           // String path = "/sdcard/凯迪拉克/凯迪拉克评估/" + TaskDetailActivity.Jingxiaoshang + "_" + TaskDetailActivity.JingCode + "/";

            String path = MyApplication.path1;
            String fileName = path + name;
            try {
                b = new FileOutputStream(fileName);
                bitmap.compress(Bitmap.CompressFormat.JPEG,100, b);// 把数据写入文件
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    b.flush();
                    b.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (!"".equals(fileName) && null != fileName && !"".equals(name) && null != name)
                mAdapter.setBoymap(fileName,name);
            }
            mAdapter.notifyDataSetChanged();
        }*/



