package com.saic.visit.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class QuestionsLog implements Serializable{
    private String operatorLog;
    private String operatorTime;

    public String getOperatorLog() {
        return operatorLog;
    }

    public void setOperatorLog(String operatorLog) {
        this.operatorLog = operatorLog;
    }

    public String getOperatorTime() {
        return operatorTime;
    }

    public void setOperatorTime(String operatorTime) {
        this.operatorTime = operatorTime;
    }
}
