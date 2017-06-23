package com.saic.visit.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ItemVo implements Serializable{
	
	private Long id;
	private Long checkpointId;
	private boolean ischeck;
	private String name;
	private Integer weight;

	private List<String> imageURLIst = new ArrayList<>();
	private List<String> imageUri = new ArrayList<>();


	public List<String> getImageUri() {
		return imageUri;
	}

	public void setImageUri(List<String> imageUri) {
		this.imageUri = imageUri;
	}

	public List<String> getImageURLIst() {
		return imageURLIst;
	}

	public void setImageURLIst(List<String> imageURLIst) {
		this.imageURLIst = imageURLIst;
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

	public Long getCheckpointId() {
		return checkpointId;
	}

	public void setCheckpointId(Long checkpointId) {
		this.checkpointId = checkpointId;
	}

	
	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public boolean ischeck() {
		return ischeck;
	}

	public void setIscheck(boolean ischeck) {
		this.ischeck = ischeck;
	}

}
