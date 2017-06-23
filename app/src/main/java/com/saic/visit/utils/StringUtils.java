package com.saic.visit.utils;

import com.saic.visit.constant.Constants;

import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/5/20.
 */
public class StringUtils {
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    public static String getStringDateSimple(long l) {
        if(l==0){
            return "";
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(l);
    }

    public static String getTimestamp(long l) {
        if(l==0){
            return "";
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(l);
    }

    public static boolean matchs(String format, String content) {
        try {
            Pattern pattern = Pattern.compile(format);
            Matcher match = pattern.matcher(content);
            return match.matches();
        } catch (Exception e) {
            return false;
        }
    }
}
