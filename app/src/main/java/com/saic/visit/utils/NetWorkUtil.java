package com.saic.visit.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Administrator on 2016/6/14.
 */
public class NetWorkUtil {
    /**
     * 判断网络状态
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager connMgr = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            // wifi网
            NetworkInfo networkInfo = connMgr
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            // 3G网
            NetworkInfo mobilWorkInfo = connMgr
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if ((networkInfo != null && networkInfo.isConnected())
                    || (mobilWorkInfo != null && mobilWorkInfo.isConnected())) {
                return true;
            }
        }
        return false;
    }
}
