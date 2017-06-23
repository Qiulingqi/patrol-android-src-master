package com.saic.visit.utils.excelutil;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by 1 on 2017/3/17.
 */

@Table(name = "Order", onCreated = "")
public class Order {

    /**
     * name = "id"：数据库表中的一个字段
     * isId = true：是否是主键
     * autoGen = true：是否自动增长
     * property = "NOT NULL"：添加约束
     */
    @Column(name = "id", isId = true, autoGen = true, property = "NOT NULL")
    private int id;
    @Column(name = "xuhao")
    public String xuhao;
    @Column(name = "pointid")
    public String pointid;
    @Column(name = "restPhone")
    public String restPhone;
    @Column(name = "istrfa")
    public String istrfa;
    @Column(name = "restName")
    public String restName;
    @Column(name = "receiverAddr")
    public String receiverAddr;
    @Column(name = "beizhu")
    public String beizhu;

    public Order() {
    }

    public Order(String xuhao, String pointid, String restPhone, String istrfa, String restName, String receiverAddr, String beizhu) {
        this.xuhao = xuhao;
        this.pointid = pointid;
        this.restPhone = restPhone;
        this.istrfa = istrfa;
        this.restName = restName;
        this.receiverAddr = receiverAddr;
        this.beizhu = beizhu;
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

    public String getPointid() {
        return pointid;
    }

    public void setPointid(String pointid) {
        this.pointid = pointid;
    }

    public String getRestPhone() {
        return restPhone;
    }

    public void setRestPhone(String restPhone) {
        this.restPhone = restPhone;
    }

    public String getIstrfa() {
        return istrfa;
    }

    public void setIstrfa(String istrfa) {
        this.istrfa = istrfa;
    }

    public String getRestName() {
        return restName;
    }

    public void setRestName(String restName) {
        this.restName = restName;
    }

    public String getReceiverAddr() {
        return receiverAddr;
    }

    public void setReceiverAddr(String receiverAddr) {
        this.receiverAddr = receiverAddr;
    }

    public String getBeizhu() {
        return beizhu;
    }

    public void setBeizhu(String beizhu) {
        this.beizhu = beizhu;
    }


    @Override
    public String toString() {
        return "序号---"+xuhao+"---取证点信息---"+pointid+"---文件---"+restName;
    }
}
