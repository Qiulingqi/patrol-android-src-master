package com.saic.visit.http;

public class VolleyConfig {
    public static final boolean DEBUG_LOG = false;
    public static final boolean DEBUG = true;

    /**
     * 获取服务器文件列表   上传本地文件
     */

    public static String FILE_URL = "http://103.40.192.190:8082/";

    // 测试环境的配置
    // public static String BASE_URL = "http://10.47.17.243:8080/";
    // public static String BASE_URL = "http://10.47.17.201:8080/";
    //TEST
    // public static String BASE_URL = "http://10.47.16.131:8080/";
    //内网
    // public static String BASE_URL = "http://10.22.1.184:8080/";
    //  public static String BASE_URL = "http://103.40.192.195:8080/patrol-web/";
    //生产1
    //public static String BASE_URL = "http://nqa.atsshanghai.com/";
    //生产2
    // public static String BASE_URL = "http://nqa.atsshanghai.com:8082/";
    //生产3
    public static String BASE_URL = "http://103.40.192.190:8082/";
    public static String BASE_URL2="http://192.168.199.100:8090/";
    //public static String BASE_URL="http://103.40.192.190:8080/";
    //  public static String BASE_URL = "http://192.168.199.249:8090/";
    public static String content_type = "application/json;charset=utf-8";

    public static String getUrl(String path) {
        if (path != null) {
            return BASE_URL + path;
        }
        return BASE_URL + path;
    }

    public static String getUrl2(String path){
        if (path != null) {
            return BASE_URL2 + path;
        }
        return BASE_URL2 + path;
    }

    public static enum NetMode {
        INTERNAL,
        EXTERNAL
    }
}
