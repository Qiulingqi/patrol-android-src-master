package com.saic.visit.model;

import java.util.List;


/**
 * 问题模块
 */
public class QuestionVo extends BaseVo{
	
	private Long id;
	
	private Long surveyId;
	private Long moduleId;
	private String remark;
	private ModuleVo moduleVo;
	private int state;
	private int type;

	public List<String> getImgList() {
		return imgList;
	}

	public void setImgList(List<String> imgList) {
		this.imgList = imgList;
	}

	private List<String> imgList;

  	private List<CatalogVo> catalogVos;

  	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSurveyId() {
		return surveyId;
	}
	
	public void setSurveyId(Long surveyId) {
		this.surveyId = surveyId;
	}
	
	

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<CatalogVo> getCatalogVos() {
		return catalogVos;
	}
	
	public void setCatalogVos(List<CatalogVo> catalogVos) {
		this.catalogVos = catalogVos;
	}

	public ModuleVo getModuleVo() {
		return moduleVo;
	}

	public void setModuleVo(ModuleVo moduleVo) {
		this.moduleVo = moduleVo;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Long getModuleId() {
		return moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}
	private boolean isEdit;
	public boolean isEdit() {
		return isEdit;
	}

	public void setIsEdit(boolean isEdit) {
		this.isEdit = isEdit;
	}
}
