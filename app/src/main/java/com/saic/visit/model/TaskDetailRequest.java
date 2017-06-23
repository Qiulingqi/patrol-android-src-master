package com.saic.visit.model;

import com.saic.visit.http.RequestSupport;

/**
 * Created by liuhui on 2016/5/23.
 */
public class TaskDetailRequest extends RequestSupport {
    public TaskDetailRequest(String messageId) {
        super();
        setMessageId(messageId);
    }
}
