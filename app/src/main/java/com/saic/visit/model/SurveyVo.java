package com.saic.visit.model;

import java.util.ArrayList;
import java.util.List;

public class SurveyVo extends BaseVo {
	
	private Long id;
	/**
	 * 任务id
	 */
	private Long taskId;
	
	/**
	 * 店访时间
	 */
	private Long supervisionTime;
	
	/**
	 * 备注
	 */
	private String remark;
	
	/**
	 * 确认方式
	 */
	private Integer confirmType;
	
	/**
	 * 确认时间
	 */
	private Long confirmTime;
	
	/**
	 * 确认电话
	 */
	private String confirmPhone;
	
	/**
	 * 是否有意义
	 */
	private Integer isObjection;

    /**
     * 是否提交
     */
    private boolean isCommit;

	private List<ModuleVo> moduleVos = new ArrayList<ModuleVo>();
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public List<ModuleVo> getModuleVos() {
		return moduleVos;
	}

	public void setModuleVos(List<ModuleVo> moduleVos) {
		this.moduleVos = moduleVos;
	}

	public Long getSupervisionTime() {
		return supervisionTime;
	}

	public void setSupervisionTime(Long supervisionTime) {
		this.supervisionTime = supervisionTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getConfirmType() {
		return confirmType;
	}

	public void setConfirmType(Integer confirmType) {
		this.confirmType = confirmType;
	}

	public Long getConfirmTime() {
		return confirmTime;
	}

	public void setConfirmTime(Long confirmTime) {
		this.confirmTime = confirmTime;
	}

	public String getConfirmPhone() {
		return confirmPhone;
	}

	public void setConfirmPhone(String confirmPhone) {
		this.confirmPhone = confirmPhone;
	}

	public Integer getIsObjection() {
		return isObjection;
	}

	public void setIsObjection(Integer isObjection) {
		this.isObjection = isObjection;
	}

	public boolean isCommit() {
		return isCommit;
	}

	public void setIsCommit(boolean isCommit) {
		this.isCommit = isCommit;
	}
}
