package com.saic.visit.model;

import com.saic.visit.http.RequestSupport;

/**
 *校验验证码
 */
public class AffirmRequest extends RequestSupport {
    public AffirmRequest(String messageId) {
        super();
        setMessageId(messageId);
    }
}
