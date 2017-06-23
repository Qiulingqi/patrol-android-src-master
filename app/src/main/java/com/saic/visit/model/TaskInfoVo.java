package com.saic.visit.model;

import java.io.Serializable;

public class TaskInfoVo  implements Serializable {
	
	/**
	 * id
	 */
	private int id;
	
	/**
	 * 任务名称
	 */
	private String name;
	
	/**
	 * 品牌id
	 */
	private Long brandId;
	
	/**
	 * 品牌名称
	 */
	private String brandName;
	
	/**
	 * 经销商简称
	 */
	private String  shortName;
	
	/**
	 * 大区id
	 */
	private Long areaId;
	
	/**
	 * 大区名称
	 */
	private String areaName;
	
	/**
	 * 执行状态(1已完成,2待确认,3寻访中,4计划中)
	 */
	private Integer status;
	
	/**
	 * 项目类型 (1售前 2售后)
	 */
	private Integer mainType;
	
	/**
	 * 项目类型 (1明访 2暗访)
	 */
	private Integer relType;
	
	/**
	 * 年份
	 */
	private Integer year;
	
	/**
	 * 项目状态(1有效 2无效)
	 */
	private Integer infoStatus;
	
	/**
	 *计划开始时间
	 */
	private Long planTime;
	
	/**
	 * 实际开始时间
	 */
	private Long beginTime;
	
	/**
	 * 实际结束时间
	 */
	private Long endTime;
	
	/**
	 * 经销商名称
	 */
	private String  dealerName;

	private String code;
	private String dealerCode;

	public boolean isOpenText;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getMainType() {
		return mainType;
	}

	public void setMainType(Integer mainType) {
		this.mainType = mainType;
	}

	public Integer getRelType() {
		return relType;
	}

	public void setRelType(Integer relType) {
		this.relType = relType;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getInfoStatus() {
		return infoStatus;
	}

	public void setInfoStatus(Integer infoStatus) {
		this.infoStatus = infoStatus;
	}

	public Long getPlanTime() {
		return planTime;
	}

	public void setPlanTime(Long planTime) {
		this.planTime = planTime;
	}

	public Long getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Long beginTime) {
		this.beginTime = beginTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public String getDealerName() {
		return dealerName;
	}

	public void setDealerName(String dealerName) {
		this.dealerName = dealerName;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public boolean isOpenText() {
		return isOpenText;
	}

	public void setIsOpenText(boolean isOpenText) {
		this.isOpenText = isOpenText;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDealerCode() {
		return dealerCode;
	}

	public void setDealerCode(String dealerCode) {
		this.dealerCode = dealerCode;
	}
}
