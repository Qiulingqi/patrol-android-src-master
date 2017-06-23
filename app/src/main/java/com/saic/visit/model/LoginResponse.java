package com.saic.visit.model;

import com.saic.visit.http.ResponseSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuhui on 2016/5/20.
 */
public class LoginResponse {
    public Supervisor supervisor;
    public List<TaskInfoVo> tasks;
    public LoginResponse(){
        supervisor = new Supervisor();
        tasks = new ArrayList<TaskInfoVo>();
    }
}
