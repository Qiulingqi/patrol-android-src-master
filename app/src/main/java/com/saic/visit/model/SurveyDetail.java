package com.saic.visit.model;



public class SurveyDetail extends BaseDomain {
	
	/**
	 * 检查点
	 */
	private Long checkpointId;
	
	/**
	 * 检查项
	 */
	private Long checkpointItemId;
	
	/**
	 * 检查人
	 */
	private Long supervisorId;
	
	/**
	 * 备注
	 */
	private String remark;
	
	/**
	 * 问题
	 */
	private Long questionId;

	public Long getCheckpointId() {
		return checkpointId;
	}

	public void setCheckpointId(Long checkpointId) {
		this.checkpointId = checkpointId;
	}

	public Long getCheckpointItemId() {
		return checkpointItemId;
	}

	public void setCheckpointItemId(Long checkpointItemId) {
		this.checkpointItemId = checkpointItemId;
	}

	public Long getSupervisorId() {
		return supervisorId;
	}

	public void setSupervisorId(Long supervisorId) {
		this.supervisorId = supervisorId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}
	
}
