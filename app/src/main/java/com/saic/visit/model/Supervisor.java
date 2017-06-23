package com.saic.visit.model;


public class Supervisor extends  BaseDomain {
	
	/**
	 * 登录id
	 */
	private String loginId;
	
	/**
	 * 登录密码
	 */
	private String loginPwd;
	
	/**
	 * 联系人姓名
	 */
	private String name;
	
	/**
	 * 联系人电话
	 */
	private String phone;
	
	/**
	 * 邮件
	 */
	private String email;

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getLoginPwd() {
		return loginPwd;
	}

	public void setLoginPwd(String loginPwd) {
		this.loginPwd = loginPwd;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	
}
