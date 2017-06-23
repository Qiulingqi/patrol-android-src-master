package com.saic.visit.model;

import com.saic.visit.http.RequestSupport;

/**
 * Created by Administrator on 2016/6/7.
 */
public class OtherConfirmRequest extends RequestSupport {
    public OtherConfirmRequest(String messageId) {
        super();
        setMessageId(messageId);
    }
    private String reason;
    private Integer type;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
