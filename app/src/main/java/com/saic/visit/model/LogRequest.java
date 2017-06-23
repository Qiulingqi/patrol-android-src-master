package com.saic.visit.model;

import com.saic.visit.http.RequestSupport;

import java.util.ArrayList;
import java.util.List;

public class LogRequest extends RequestSupport {
	public LogRequest(String messageId) {
		super();
		setMessageId(messageId);
	}
 
	private Long surveyId;

	private Long supervisorId;

	private List<QuestionsLog> operatorLogDetails = new ArrayList<QuestionsLog>();



	public Long getSurveyId() {
		return surveyId;
	}

	public void setSurveyId(Long surveyId) {
		this.surveyId = surveyId;
	}

	public Long getSupervisorId() {
		return supervisorId;
	}

	public void setSupervisorId(Long supervisorId) {
		this.supervisorId = supervisorId;
	}

	public List<QuestionsLog> getOperatorLogDetails() {
		return operatorLogDetails;
	}

	public void setOperatorLogDetails(List<QuestionsLog> operatorLogDetails) {
		this.operatorLogDetails = operatorLogDetails;
	}
}
