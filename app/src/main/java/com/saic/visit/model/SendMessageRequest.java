package com.saic.visit.model;

import com.saic.visit.http.RequestSupport;

/**
 * 1001
 * 发送短信获取验证码
 *
 */
public class SendMessageRequest extends RequestSupport {
    public SendMessageRequest(String messageId) {
        super();
        setMessageId(messageId);
    }
}
