package com.saic.visit.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class ModuleVo  implements Serializable{
	
	private Long id;
	
	private String	name;
	
	private Double weight;

	private List<QuestionVo> questionVos;

	private List<CatalogVo> catalogVos;
	
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ModuleVo(Long id, String name, Double weight) {
		super();
		this.id = id;
		this.name = name;
		this.weight = weight;
	}

	public ModuleVo() {
		super();
	}

	public List<CatalogVo> getCatalogVos() {
		return catalogVos;
	}

	public void setCatalogVos(ArrayList<CatalogVo> catalogVos) {
		this.catalogVos = catalogVos;
	}

	public List<QuestionVo> getQuestionVos() {
		return questionVos;
	}

	public void setQuestionVos(List<QuestionVo> questionVos) {
		this.questionVos = questionVos;
	}

	@Override
	public String toString() {
		return id+""+name+""+weight;
	}
}
