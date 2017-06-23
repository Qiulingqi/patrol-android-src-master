package com.saic.visit.model;

import java.io.Serializable;

/**
 * Created by 1 on 2017/3/17.
 */

public class YingSheExcel implements Serializable {

    public String code;
    public String information;
    public String type;
    public String content;

    public YingSheExcel(String code, String information,  String type, String content) {
        this.code = code;
        this.information = information;
        this.type = type;
        this.content = content;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
