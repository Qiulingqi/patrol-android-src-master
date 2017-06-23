package com.saic.visit.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.android.volley.Response;
import com.android.volley.httpcore.ContentType;
import com.android.volley.httpmime.mime.HttpMultipartMode;
import com.android.volley.httpmime.mime.MultipartEntityBuilder;
import com.android.volley.httpmime.mime.content.StringBody;
import com.google.gson.Gson;
import com.saic.visit.R;
import com.saic.visit.common.BaseActivity;
import com.saic.visit.constant.Constants;
import com.saic.visit.http.ResponseSupport;
import com.saic.visit.http.VolleyConfig;
import com.saic.visit.http.VolleyRequestManager;
import com.saic.visit.model.AffirmRequest;
import com.saic.visit.model.ReceptionistTypeVo;
import com.saic.visit.model.TaskDetailResponse;
import com.saic.visit.utils.NetWorkUtil;
import com.saic.visit.utils.SharePreferenceUtil;
import com.saic.visit.utils.StringUtil;
import com.saic.visit.utils.ToastUtil;
import com.saic.visit.utils.superme.WeiboDialogUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by liuhui on 2016/5/16.
 */

/**
 * 修改此处 Super-me
 */

public class TaskDetailActivity extends BaseActivity {

    @Bind(R.id.rela_back)
    RelativeLayout relaBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.img_right)
    ImageView imgRight;
    @Bind(R.id.rela_right)
    RelativeLayout relaRight;
    @Bind(R.id.layout_reception)
    LinearLayout layoutReception;
    @Bind(R.id.detail_reception)
    TextView detailReception;
    @Bind(R.id.layout_visit)
    LinearLayout layoutVisit;
    @Bind(R.id.detail_visit)
    TextView detailVisit;
    @Bind(R.id.layout_report)
    LinearLayout layoutReport;
    @Bind(R.id.detail_report)
    TextView detailReport;
    @Bind(R.id.layout_score)
    LinearLayout layoutScore;
    public static String Jingxiaoshang = null;
    public static String JingCode = null;
    private List<String> listfile = new ArrayList<>();
    private List<String> listfiles = new ArrayList<>();
    private Dialog loadingDialog1;
    private Toast toast;
    private String substring;
    private LinearLayout fileUpload;
    private int size = 1;
    private int num = 1;
    PowerManager powerManager=null;
    PowerManager.WakeLock wakeLock=null;
    private String taskId;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fileUpload = (LinearLayout) findViewById(R.id.file_Upload);
        fileUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                powerManager = (PowerManager)TaskDetailActivity.this.getSystemService(TaskDetailActivity.this.POWER_SERVICE);
                wakeLock = TaskDetailActivity.this.powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Lock");
                if(wakeLock!=null){
                    wakeLock.setReferenceCounted(false);
                    wakeLock.acquire();
                }
                if(!NetWorkUtil.isNetworkConnected(TaskDetailActivity.this)){
                    ToastUtil.show(TaskDetailActivity.this, "网络连接不可用");
                    return;
                }

                loadingDialog1 = WeiboDialogUtils.createLoadingDialog(TaskDetailActivity.this, "正在上传.....");
                /**
                 * 上传未上传完毕的文件
                 */
                String path = MyApplication.path;
                File file = new File(path);
                File[] files = file.listFiles();
                int length = files.length;
                for (int i = 0; i < length; i++) {
                    File file1 = files[i];
                    if (file1.isDirectory()) {
                        // 存放目录
                        listfile.add(file1.getPath());
                        // last.add(file1.getPath().substring(8,file1.getPath().length()));
                    }
                    if (file1.isFile()) {
                        // 存放文件
                        listfiles.add(file1.getName());
                    }
                }
                //第一步先上传图片
                for (int i = 0; i < listfile.size(); i++) {
                    // 访问网络需要在子线程中
                    final int finalI = i;
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            /**
                             * 表单请求
                             */
                            if(listfile.get(finalI).substring(20, listfile.get(finalI).length()).equals(MyApplication.JingXiaoCode+"_"+MyApplication.JingXiaoShang)){
                                Map<String, String> data = new HashMap<String, String>();
                                String substring = listfile.get(finalI).substring(20, listfile.get(finalI).length());
                                data.put("dirPath", substring);
                                if(!NetWorkUtil.isNetworkConnected(TaskDetailActivity.this)){
                                    ToastUtil.show(TaskDetailActivity.this, "网络连接不可用");
                                    loadingDialog1.cancel();
                                }
                                posts(VolleyConfig.FILE_URL+"fileUpload/getDirFileList", data, listfile.get(finalI));

                            }

                        }
                    }.start();
                }
                //  然后在上传excel表格
                for (int i = 0; i < listfiles.size(); i++) {
                    // 此处做了修改
                    final String ss = MyApplication.path + listfiles.get(i);
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            //  关于表单 。 暂时存在根目录
                            if(ss.indexOf(MyApplication.JingXiaoShang)!=-1&&ss.indexOf(MyApplication.JingXiaoCode)!=-1){
                                upLoadFilePost(ss, "");
                            }

                        }
                    }.start();

                }

            }
        });
    }

    /**
     * 建立一个Handler  用来接收子线程发出的消息  动态改变上传按钮的显示状态
     *
     * @param savedInstanceState
     */
    Handler hand = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                fileUpload.setVisibility(View.VISIBLE);
            }
            if (msg.what == 2) {
                toast = Toast.makeText(getBaseContext(), "上传成功", Toast.LENGTH_SHORT);
                toast.show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toast.cancel();
                        zhuangtai();
                        fileUpload.setVisibility(View.INVISIBLE);

                    }
                }, 100);

            }

            if(msg.what==3){
                fileUpload.setVisibility(View.INVISIBLE);
            }

            if (100 == msg.what) {
                Toast.makeText(getBaseContext(), "网络请求失败,请重试", Toast.LENGTH_SHORT).show();
            }
        }
    };


    private void zhuangtai(){
        taskId= SharePreferenceUtil.getStringValue(Constants.TASKID, TaskDetailActivity.this);
        AffirmRequest affirmRequest = new AffirmRequest("Task/affirm2?taskId=" + taskId);
        /*DialogUtil.showLoading(TaskConfirmActivity.this);*/
        VolleyRequestManager.getInstance(this).startHttpGetRequest(this, affirmRequest,
                ResponseSupport.class, new Response.ListenerV2<ResponseSupport>() {
                    @Override
                    public void onResponse(ResponseSupport response, Map<String, String> headers) {
                        if (!VolleyRequestManager.realResponseResultSupport(TaskDetailActivity.this, response, null, true))
                            return;
                       /* new Thread() {
                            public void run() {
                                uploadLog();
                            }
                        }.start();*/
                    }
                }, volleyError);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_task_detail);
        /**
         * 在这里去请求网络 看本地是否有数据未上传完毕  真是哔了狗了
         */
        // 先判断文件夹是否为空

        ButterKnife.bind(this);
    }

    private void performList() {
        String path = MyApplication.path;
        File file = new File(path);
        File[] files = file.listFiles();
        int length = files.length;
        for (int i = 0; i < length; i++) {
            File file1 = files[i];
            if (file1.isDirectory()) {
                // 存放目录
                listfile.add(file1.getPath());
                // last.add(file1.getPath().substring(8,file1.getPath().length()));

            }
//            if (file1.isFile()) {
//                // 存放文件
//                listfiles.add(file1.getName());
//            }
        }
        //第一步先上传图片
        for (int i = 0; i < listfile.size(); i++) {
            // 访问网络需要在子线程中
            final int finalI = i;
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    /**
                     * 表单请求
                     */
                    if(listfile.get(finalI).substring(20, listfile.get(finalI).length()).equals(MyApplication.JingXiaoCode+"_"+MyApplication.JingXiaoShang)){

                        Map<String, String> data = new HashMap<String, String>();
                        String substring = listfile.get(finalI).substring(20, listfile.get(finalI).length());
                        data.put("dirPath", substring);
                        posts1(VolleyConfig.FILE_URL+"fileUpload/getDirFileList", data, listfile.get(finalI));
                    }
                }
            }.start();
        }
    }

    //post表单请求  这里只做判断  不做请求
    public String posts1(String url, Map<String, String> form, String filesss) {
        HttpURLConnection conn = null;
        PrintWriter pw = null;
        BufferedReader rd = null;
        StringBuilder out = new StringBuilder();
        StringBuilder sb = new StringBuilder();
        String line = null;
        String response = null;
        for (String key : form.keySet()) {
            if (out.length() != 0) {
                out.append("&");
            }
            out.append(key).append("=").append(form.get(key));
        }
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setReadTimeout(20000);
            conn.setConnectTimeout(20000);
            conn.setUseCaches(false);
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            conn.connect();
            pw = new PrintWriter(conn.getOutputStream());
            pw.print(out.toString());
            pw.flush();
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            response = sb.toString();
            String dirPath = form.get("dirPath");
            File file = new File(filesss);
            File[] files = file.listFiles();
            if(files.length!=0){
                if (!"".equals(response)) {
                    List<String> list = new ArrayList<>();
                    JSONArray objects1 = JSON.parseArray(response);
                    for (Object ss : objects1) {
                        list.add(ss.toString());
                    }
                    List<String> loacdlist = new ArrayList<>();
                    for (int i = 0; i < files.length; i++) {
                        loacdlist.add(files[i].getName());
                    }
                    List<String> strings = StringUtil.ListAndListToList(loacdlist, list);
                    if (0 == strings.size()) {

                    } else {
                        hand.sendEmptyMessage(1);
                    }
                } else {
                    hand.sendEmptyMessage(1);
                }
            }else{
                hand.sendEmptyMessage(3);
            }
            // 拿到response之后  去重  然后上传照片

        } catch (MalformedURLException e) {
            e.printStackTrace();
            loadingDialog1.cancel();
        } catch (IOException e) {
            e.printStackTrace();
            loadingDialog1.cancel();
        }catch (Exception e){
            e.printStackTrace();
            loadingDialog1.cancel();
        }finally {
            try {
                if (pw != null) {
                    pw.close();
                }
                if (rd != null) {
                    rd.close();
                }
                if (conn != null) {
                    conn.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        /**
         * 拿到值  建立文件夹 且设置标题
         */
        String jingxiaoshang = getIntent().getExtras().getString("jingxiaoshang");
        String jingxiaoshangdaima = getIntent().getExtras().getString("jingxiaoshangdaima");
        Jingxiaoshang = jingxiaoshangdaima;
        JingCode = jingxiaoshang;
        MyApplication.JingXiaoShang = jingxiaoshang;
        MyApplication.JingXiaoCode = jingxiaoshangdaima;
        txtTitle.setText(jingxiaoshang + "  " + jingxiaoshangdaima);
        // 建立文件夹
        /**
         * 一级目录 评估方案名称
         * 二级目录 评估计划名称   映射表保存在这里
         * 三级目录 经销商代码_经销商名称  照片存在这里
         */
        File file2 = new File(MyApplication.path + jingxiaoshangdaima + "_" + jingxiaoshang + "/");
        MyApplication.path1 = MyApplication.path + jingxiaoshangdaima + "_" + jingxiaoshang + "/";
        file2.mkdirs();// 创建文件夹
        relaRight.setVisibility(View.INVISIBLE);
        initData();


        String path = MyApplication.path;
        File file = new File(path);
        File[] files = file.listFiles();
        if (null != files) {
            performList();

        }
    }

    @OnClick({R.id.rela_back, R.id.layout_reception, R.id.layout_visit, R.id.layout_report, R.id.layout_score})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.rela_back:
                setResult(100);
                finish();
                TaskDetailActivity.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                break;
            case R.id.layout_reception:
                intent = new Intent(TaskDetailActivity.this, ReceptionActivity.class);
                intent.putExtra(Constants.TASK, txtTitle.getText().toString());
                startActivity(intent);
                TaskDetailActivity.this.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;
            case R.id.layout_visit:
                intent = new Intent(TaskDetailActivity.this, ShopVisitActivity.class);
                startActivityForResult(intent, 10);
                TaskDetailActivity.this.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;
            case R.id.layout_report:
                break;
            case R.id.layout_score:
//                intent = new Intent(TaskDetailActivity.this, ScoreActivity.class);
                //TODO 如果这里报错了，有可能是数据有问题，导致树结构死循环。
                intent = new Intent(TaskDetailActivity.this, ScoreModelActivity.class);
                startActivity(intent);
                TaskDetailActivity.this.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;
        }
    }

    //post表单请求
    public String posts(String url, Map<String, String> form, String filesss) {

        HttpURLConnection conn = null;
        PrintWriter pw = null;
        BufferedReader rd = null;
        StringBuilder out = new StringBuilder();
        StringBuilder sb = new StringBuilder();
        String line = null;
        String response = null;
        for (String key : form.keySet()) {
            if (out.length() != 0) {
                out.append("&");
            }
            out.append(key).append("=").append(form.get(key));
        }
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setReadTimeout(20000);
            conn.setConnectTimeout(20000);
            conn.setUseCaches(false);
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            conn.connect();
            pw = new PrintWriter(conn.getOutputStream());
            pw.print(out.toString());
            pw.flush();
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            response = sb.toString();
            String dirPath = form.get("dirPath");
            File file = new File(filesss);
            File[] files = file.listFiles();
            // 拿到response之后  去重  然后上传照片
            if (!"".equals(response)) {
                List<String> list = new ArrayList<>();
                JSONArray objects1 = JSON.parseArray(response);
                for (Object ss : objects1) {
                    list.add(ss.toString());
                }
                List<String> loacdlist = new ArrayList<>();
                for (int i = 0; i < files.length; i++) {
                    loacdlist.add(files[i].getName());
                }
                List<String> strings = StringUtil.ListAndListToList(loacdlist, list);
                num += strings.size();
                for (int i = 0; i < strings.size(); i++) {
                    if(!NetWorkUtil.isNetworkConnected(TaskDetailActivity.this)){
                       // ToastUtil.show(TaskDetailActivity.this, "网络连接不可用");
                        loadingDialog1.cancel();
                    }

                    String ss = MyApplication.path + dirPath + "/" + strings.get(i);
                    //开始上传
                    //  首先截取字符串   因为上传只需要携带目录
                    substring = filesss.substring(20, filesss.length());
                    upLoadFilePost(ss, substring);

                }
            } else {
                for (int i = 0; i < files.length; i++) {
                    if(!NetWorkUtil.isNetworkConnected(TaskDetailActivity.this)){
                        //ToastUtil.show(TaskDetailActivity.this, "网络连接不可用");
                        loadingDialog1.cancel();
                    }
                    File file1 = files[i];
                    num += file1.length();
                    if (file1.isFile()) {
                        String ss = dirPath + file1.getName();
                        //开始上传
                        String substring2 = filesss.substring(20, filesss.length());
                        upLoadFilePost(file1.getPath(), substring2);
                    }

                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
            loadingDialog1.cancel();
        } catch (IOException e) {
            e.printStackTrace();
            loadingDialog1.cancel();
        } catch (Exception e){
            e.printStackTrace();
            loadingDialog1.cancel();
        }finally {
            try {
                if (pw != null) {
                    pw.close();
                }
                if (rd != null) {
                    rd.close();
                }
                if (conn != null) {
                    conn.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return response;
    }

    private void upLoadFilePost(String path, String fileString) {
        ContentType contentType = ContentType.create(HTTP.PLAIN_TEXT_TYPE, org.apache.http.protocol.HTTP.UTF_8);
        HttpClient client = new DefaultHttpClient();// 开启一个客户端 HTTP 请求
        HttpPost post = new HttpPost(VolleyConfig.FILE_URL+"fileUpload/fileUp2");//创建 HTTP POST 请求
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setCharset(Charset.forName(HTTP.UTF_8));//设置请求的编码格式
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);//设置浏览器兼容模式
        builder.addBinaryBody("multipartFile", new File(path));

        StringBody stringBody = new StringBody("中文乱码", contentType);
        builder.addPart("test", stringBody);

        StringBody ss = new StringBody(fileString, contentType);
        builder.addPart("dirPath", ss);

        HttpEntity entity = builder.build();// 生成 HTTP POST 实体
        post.setEntity(entity);//设置请求参数
        HttpResponse response = null;// 发起请求 并返回请求的响应
        try {
            response = client.execute(post);
        } catch (Exception e) {
            e.printStackTrace();
            loadingDialog1.cancel();
        }
        if (null != response) {
            if (response.getStatusLine().getStatusCode() == 200) {
                size++;
                if (size + 1 > num) {
                    loadingDialog1.cancel();
                    hand.sendEmptyMessage(2);
                }
            } else {
                loadingDialog1.cancel();
                hand.sendEmptyMessage(100);
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == 10) {
            setResult(100);
            finish();
            TaskDetailActivity.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        }
        if (requestCode == 10 && resultCode == 20) {
            initData();
        }
    }

    private boolean isRecord(TaskDetailResponse task) {
        if (task.receptionistTypes != null) {
            List<ReceptionistTypeVo> receptionistTypes = task.receptionistTypes;
            int size = receptionistTypes.size();
            for (int i = 0; i < size; i++) {
                if (receptionistTypes.get(i).getId() != null) {
                    return true;
                }
            }
        }
        return false;
    }

    private void initData() {
        Gson gsonss = new Gson();
        String taskId = SharePreferenceUtil.getStringValue(Constants.TASKID, TaskDetailActivity.this);
        String data = SharePreferenceUtil.getStringValue(Constants.TASKDETAIL + taskId, TaskDetailActivity.this);
        TaskDetailResponse task = gsonss.fromJson(data, TaskDetailResponse.class);
        detailReception.setText(isRecord(task) ? "未完成录入" : "已完成录入");
        int questionSize = 0;
        for (int i = 0, len = task.survey.getModuleVos().size(); i < len; i++) {
            questionSize = questionSize + task.survey.getModuleVos().get(i).getQuestionVos().size();
        }
        detailVisit.setText(task.survey == null && questionSize == 0 ? "未录入扣分项" : "已录入" + questionSize + "条扣分项");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(100);
            finish();
            TaskDetailActivity.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        }

        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(wakeLock!=null){
            wakeLock.release();
            wakeLock=null;
        }

    }
}
