package com.saic.visit.model;

import java.io.Serializable;

public class ReceptionistTypeVo  extends BaseVo implements Serializable{
	
	/**
	 * 关系类型id
	 */
	private Long  id;
	
	/**
	 * code
	 */
	private Long code;
	
	/**
	 * 接待人员类型
	 */
	private String name;
	
	/**
	 * 关系id
	 */
	private Long surveyReceptionistId;
	
	/**
	 * 任务id
	 */
	private Long taskId;
	
	/**
	 * 接待人员id
	 */
	private Long receptionistId;
	
	
	/**
	 * 接待人员名字
	 */
	private String receptionistName;
	
	/**
	 * 接待人员姓名
	 */
	private Integer gender;
	
	/**
	 * 接待人员职位
	 */
	private String position;
	private String phone;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCode() {
		return code;
	}

	public void setCode(Long code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getSurveyReceptionistId() {
		return surveyReceptionistId;
	}

	public void setSurveyReceptionistId(Long surveyReceptionistId) {
		this.surveyReceptionistId = surveyReceptionistId;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public Long getReceptionistId() {
		return receptionistId;
	}

	public void setReceptionistId(Long receptionistId) {
		this.receptionistId = receptionistId;
	}

	public String getReceptionistName() {
		return receptionistName;
	}

	public void setReceptionistName(String receptionistName) {
		this.receptionistName = receptionistName;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
}
