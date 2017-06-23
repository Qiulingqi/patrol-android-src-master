package com.saic.visit.model;

import java.io.Serializable;
import java.util.List;


/**
 * 环节
 */
public class CatalogVo implements Serializable {

	private String modelName;

	private Long id;
	/**
	 * 模块id
	 */
	private Long moduleId;
	
	/**
	 *权重 
	 */
	private Double weight;
	
	/**
	 * 名称
	 */
	private String name;
	private String currentTime;

	private List<CatalogVo> catalogVos;
	
	private List<CheckPointVo> checkPointVos;

	public Long getModuleId() {
		return moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CatalogVo() {
		super();
	}


	public List<CheckPointVo> getCheckPointVos() {
		return checkPointVos;
	}

	public void setCheckPointVos(List<CheckPointVo> checkPointVos) {
		this.checkPointVos = checkPointVos;
	}

	public List<CatalogVo> getCatalogVos() {
		return catalogVos;
	}

	public void setCatalogVos(List<CatalogVo> catalogVos) {
		this.catalogVos = catalogVos;
	}

	public String getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(String currentTime) {
		this.currentTime = currentTime;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
}
