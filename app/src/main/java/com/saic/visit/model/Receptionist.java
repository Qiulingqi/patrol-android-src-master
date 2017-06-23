package com.saic.visit.model;

public class Receptionist extends BaseDomain {
	
	/**
	 * 经销商id
	 */
	private Long dealerId;
	
	/**
	 *名称
	 */
	private String name;
	
	/**
	 * 性别
	 */
	private String gender;
	
	/**
	 * 岗位
	 */
	private String position;
	
	/**
	 * 电话
	 */
	private String phone;
	
	public Long getDealerId() {
		return dealerId;
	}

	public void setDealerId(Long dealerId) {
		this.dealerId = dealerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
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

	public Receptionist() {
		super();
	}

	public Receptionist(String name, String gender, String position,
			String phone) {
		super();
		this.name = name;
		this.gender = gender;
		this.position = position;
		this.phone = phone;
	}
	
	
}
