package com.saic.visit.activity;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.saic.visit.R;
import com.saic.visit.common.BaseActivity;
import com.saic.visit.constant.Constants;
import com.saic.visit.http.ResponseSupport;
import com.saic.visit.http.VolleyConfig;
import com.saic.visit.http.VolleyRequestManager;
import com.saic.visit.model.AffirmRequest;
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
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
 * Created by Administrator on 2016/5/19.
 */
public class SuccessActivity extends BaseActivity {
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
    @Bind(R.id.confirm)
    Button confirm;
    private List<String> listfile = new ArrayList<>();
    private List<String> listfiles = new ArrayList<>();
    private Toast toast;

    public static String sessionToken = "";

    private static String path = "";
    private Dialog loadingDialog1;
    private String substring;
    private int size = 1;
    private int num = 1;
    private String taskId;

    PowerManager powerManager=null;
    PowerManager.WakeLock wakeLock=null;


    Handler hand = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (100 == msg.what) {
                Toast.makeText(getBaseContext(), "网络异常，请重试", Toast.LENGTH_SHORT).show();
            }
            if (0 == msg.what) {
                toast = Toast.makeText(getBaseContext(), "上传成功", Toast.LENGTH_SHORT);
                toast.show();
                zhuangtai();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toast.cancel();
                    }
                }, 100);
            }


        }
    };


    private void zhuangtai(){
        taskId= SharePreferenceUtil.getStringValue(Constants.TASKID, SuccessActivity.this);
        AffirmRequest affirmRequest = new AffirmRequest("Task/affirm2?taskId=" + taskId);
        /*DialogUtil.showLoading(TaskConfirmActivity.this);*/
        VolleyRequestManager.getInstance(this).startHttpGetRequest(this, affirmRequest,
                ResponseSupport.class, new Response.ListenerV2<ResponseSupport>() {
                    @Override
                    public void onResponse(ResponseSupport response, Map<String, String> headers) {
                        if (!VolleyRequestManager.realResponseResultSupport(SuccessActivity.this, response, null, true))
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
        setContentView(R.layout.layout_success);
        ButterKnife.bind(this);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        relaRight.setVisibility(View.INVISIBLE);
        txtTitle.setText(getResources().getText(R.string.task_upload_title));
    }

    @OnClick({R.id.rela_back, R.id.back, R.id.confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rela_back:
                setResult(10);
                finish();
                SuccessActivity.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                break;
            case R.id.back:
                /**
                 * 修改此处  防止返回的时候数组越界
                 */
               // setResult(10);
               // finish();
               // SuccessActivity.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                Intent intent1 = new Intent(SuccessActivity.this,MainActicity.class);
                intent1.putExtra("zhaungtai",2);
                startActivity(intent1);
                break;
            case R.id.confirm:

                powerManager = (PowerManager)SuccessActivity.this.getSystemService(SuccessActivity.this.POWER_SERVICE);
                wakeLock = SuccessActivity.this.powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Lock");
                if(wakeLock!=null){
                    wakeLock.setReferenceCounted(false);
                    wakeLock.acquire();
                }
                if(!NetWorkUtil.isNetworkConnected(SuccessActivity.this)){
                    ToastUtil.show(SuccessActivity.this, "网络连接不可用");
                    return;
                }
                loadingDialog1 = WeiboDialogUtils.createLoadingDialog(SuccessActivity.this, "正在上传.....");
    /*            Intent intent = new Intent(SuccessActivity.this,TaskConfirmActivity.class);
                intent.putExtra(ShopVisitActivity.INTENT_EXTRA_PHONE,getIntent().getStringExtra(ShopVisitActivity.INTENT_EXTRA_PHONE));
                startActivityForResult(intent,100);
                SuccessActivity.this.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);*/
                /**
                 * 上传文件到服务器
                 */

                String path = MyApplication.path;
                //+MyApplication.JingXiaoCode + "_" + MyApplication.JingXiaoShang+"/"

                File file = new File(path);
                File[] files = file.listFiles();
                int length = files.length;
                for (int i = 0; i < length; i++) {
                    File file1 = files[i];
                    if (file1.isDirectory()) {
                        // 存放目录
                        listfile.add(file1.getPath());
                        // last.add(file1.getPath().substring(8,file1.getPath().length()));
                        System.out.println("this is filel"+file1.getPath());
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
                            if(!NetWorkUtil.isNetworkConnected(SuccessActivity.this)){
                                ToastUtil.show(SuccessActivity.this, "网络连接不可用");
                                loadingDialog1.cancel();
                            }

                            if(listfile.get(finalI).substring(20, listfile.get(finalI).length()).equals(MyApplication.JingXiaoCode+"_"+MyApplication.JingXiaoShang)){
                                Map<String, String> data = new HashMap<String, String>();
                                String substring = listfile.get(finalI).substring(20, listfile.get(finalI).length());
                                data.put("dirPath", substring);
                                posts(VolleyConfig.FILE_URL+"fileUpload/getDirFileList", data, listfile.get(finalI));
                            }

                        }
                    }.start();


                }
                //  然后在上传excel表格
                for (int i = 0; i < listfiles.size(); i++) {
                    // 此处做了修改
                    final String ss = MyApplication.path + listfiles.get(i);

                    // final String ss =listfiles.get(i);
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            //  关于表单 。 暂时存在根目录
                            if(!NetWorkUtil.isNetworkConnected(SuccessActivity.this)){
                                ToastUtil.show(SuccessActivity.this, "网络连接不可用");
                                loadingDialog1.cancel();
                            }
                            if(ss.indexOf(MyApplication.JingXiaoShang)!=-1&&ss.indexOf(MyApplication.JingXiaoCode)!=-1){
                                upLoadFilePost(ss, "");
                            }

                        }
                    }.start();

                }

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
                    if(!NetWorkUtil.isNetworkConnected(SuccessActivity.this)){
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
                    if(!NetWorkUtil.isNetworkConnected(SuccessActivity.this)){
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
        HttpPost post = new HttpPost(VolleyConfig.FILE_URL +"/fileUpload/fileUp2");//创建 HTTP POST 请求
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
                if (size > num) {
                    loadingDialog1.cancel();
                    hand.sendEmptyMessage(0);


                }
            } else {
                hand.sendEmptyMessage(100);
                loadingDialog1.cancel();
            }
        }
    }
    /**
     *
     *
     *        // 字符参数部分

     StringBody ss = new StringBody("CV0201_王俊刚",contentType);
     builder.addPart("dirPath",ss);
     *
     *
     /*  */
    /**
     * 添加参数
     *//*
        // 创建参数队列
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("dirPath", "CV0201_王俊刚"));
        UrlEncodedFormEntity uefEntity;
        try {
            uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
            post.setEntity(uefEntity);
        }catch (Exception e){
        }*/
    private void upLoadByCommonPost(String path) {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "******";
        try {
            URL url = new URL(VolleyConfig.FILE_URL+"fileUpload/fileUp");
            HttpURLConnection httpURLConnection = null;
            httpURLConnection = (HttpURLConnection) url
                    .openConnection();
            httpURLConnection.setChunkedStreamingMode(128 * 1024);// 128K
            // 允许输入输出流
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            // 使用POST方法
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            httpURLConnection.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);
            DataOutputStream dos = new DataOutputStream(
                    httpURLConnection.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"multipartFile\"; filename=\""
                    + path.substring(path.lastIndexOf("/") + 1) + "\"" + end);
            dos.writeBytes(end);
            FileInputStream fis = new FileInputStream(path);
            byte[] buffer = new byte[8192]; // 8k
            int count = 0;
            // 读取文件
            while ((count = fis.read(buffer)) != -1) {
                dos.write(buffer, 0, count);
            }
            fis.close();
            dos.writeBytes(end);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
            dos.flush();
            InputStream is = httpURLConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String result = br.readLine();
            Log.i("--superme:ok>>", result);
            dos.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("--superme:no>>", e.getMessage());
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(10);
            finish();
            SuccessActivity.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 100) {
            setResult(10);
            finish();
            SuccessActivity.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        }
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
        }

    }
}

