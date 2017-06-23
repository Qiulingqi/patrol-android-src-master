package com.saic.visit.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuhui on 2016/5/23.
 */
public class TaskDetailResponse implements Serializable {

    /**
     * 评分标准信息列表
     */
    public List<ModuleVo> models ;
    /**
     * 接待关系表
     */
    public List<ReceptionistTypeVo> receptionistTypes;
    /**
     * 所有接待人员列表
     */
    public List<Receptionist> receptionists;
    public SurveyVo survey;
    public Dealer dealer;


    public TaskDetailResponse() {
        models = new ArrayList<ModuleVo>();
        receptionistTypes = new ArrayList<ReceptionistTypeVo>();
        receptionists = new ArrayList<Receptionist>();
        survey = new SurveyVo();
        dealer = new Dealer();
    }
}
