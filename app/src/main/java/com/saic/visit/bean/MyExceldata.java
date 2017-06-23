package com.saic.visit.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by 1 on 2017/5/24.
 */

@Table(name = "MyExceldata", onCreated = "")
public class MyExceldata {
    /**
     * name = "id"：数据库表中的一个字段
     * isId = true：是否是主键
     * autoGen = true：是否自动增长
     * property = "NOT NULL"：添加约束
     */
    @Column(name = "id", isId = true, autoGen = true, property = "NOT NULL")
    private int id;
    @Column(name = "xuhao")
    private String xuhao;
    @Column(name = "pointcode")
    private String pointcode;
    @Column(name = "type")
    private String type;
    @Column(name = "iswith")
    private String iswith;
    @Column(name = "name")
    private String name;
    @Column(name = "content")
    private String content;
    @Column(name = "beizhu")
    private String beizhu;


    public MyExceldata(String xuhao, String pointcode, String type, String iswith, String name, String content, String beizhu) {
        this.xuhao = xuhao;
        this.pointcode = pointcode;
        this.type = type;
        this.iswith = iswith;
        this.name = name;
        this.content = content;
        this.beizhu = beizhu;
    }


    //默认的构造方法必须写出，如果没有，这张表是创建不成功的
    public MyExceldata() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getXuhao() {
        return xuhao;
    }

    public void setXuhao(String xuhao) {
        this.xuhao = xuhao;
    }

    public String getPointcode() {
        return pointcode;
    }

    public void setPointcode(String pointcode) {
        this.pointcode = pointcode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIswith() {
        return iswith;
    }

    public void setIswith(String iswith) {
        this.iswith = iswith;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBeizhu() {
        return beizhu;
    }

    public void setBeizhu(String beizhu) {
        this.beizhu = beizhu;
    }

}
