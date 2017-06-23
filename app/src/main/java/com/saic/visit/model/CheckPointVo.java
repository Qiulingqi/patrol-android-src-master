package com.saic.visit.model;

import java.io.Serializable;
import java.util.List;


public class CheckPointVo implements Serializable{
	
	private Long id;
	
	private Long catalogId;
	
	private String name;
	
	private Double weight;
	
	private String desc;
	private String remark;
	private String pointCode;
	// 在增加一个字段   用来标识已经拍过照片
	private String addFlag;

	public String getAddFlag() {
		return addFlag;
	}

	public void setAddFlag(String addFlag) {
		this.addFlag = addFlag;
	}

	public String getPointCode() {
		return pointCode;
	}

	public void setPointCode(String pointCode) {
		this.pointCode = pointCode;
	}

	/**
	 * 是否可备注
	 */
	private Integer markable;
	
	private List<ItemVo> itemVos;

	public Long getCatalogId() {
		return catalogId;
	}

	public void setCatalogId(Long catalogId) {
		this.catalogId = catalogId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Integer getMarkable() {
		return markable;
	}

	public void setMarkable(Integer markable) {
		this.markable = markable;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<ItemVo> getItemVos() {
		return itemVos;
	}

	public void setItemVos(List<ItemVo> itemVos) {
		this.itemVos = itemVos;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
