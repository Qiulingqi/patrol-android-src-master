package com.saic.visit.model;

import com.saic.visit.http.RequestSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * 1001
 * APP登录手机APP端登录到系统，校验登录信息
 *
 */
public class TaskListResponse extends RequestSupport {
    public List<TaskInfoVo> taskInfoVos;
    public TaskListResponse() {
        taskInfoVos = new ArrayList<TaskInfoVo>();
    }
}
