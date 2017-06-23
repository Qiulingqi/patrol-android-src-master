package com.saic.visit.model;

import com.saic.visit.http.RequestSupport;

import java.util.List;

public class RequestVoRequest  extends RequestSupport {
	public RequestVoRequest(String messageId) {
		super();
		setMessageId(messageId);
	}
 
	private Long taskId;
	
	/**
	 * 登录人员
	 */
	private Long supervisorId;
	
	/**
	 * 人员与关系
	 */
	private List<ReceptionistTypeVo> receptionistTypeVos;
	
	/**
	 * 
	 */
	private SurveyVo surveyVo;

	public Long getSupervisorId() {
		return supervisorId;
	}

	public void setSupervisorId(Long supervisorId) {
		this.supervisorId = supervisorId;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public List<ReceptionistTypeVo> getReceptionistTypeVos() {
		return receptionistTypeVos;
	}

	public void setReceptionistTypeVos(List<ReceptionistTypeVo> receptionistTypeVos) {
		this.receptionistTypeVos = receptionistTypeVos;
	}

	public SurveyVo getSurveyVo() {
		return surveyVo;
	}

	public void setSurveyVo(SurveyVo surveyVo) {
		this.surveyVo = surveyVo;
	}
	
}
