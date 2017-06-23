package com.saic.visit.model;

import com.saic.visit.http.RequestSupport;

/**
 * 1001
 * APP登录手机APP端登录到系统，校验登录信息
 *
 */
public class LoginRequest extends RequestSupport {
    public LoginRequest(String messageId) {
        super();
        setMessageId(messageId);
    }
}
