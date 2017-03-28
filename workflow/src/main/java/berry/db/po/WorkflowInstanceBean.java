package berry.db.po;

import java.util.Date;

public class WorkflowInstanceBean {
	
	private String requestId;
	
	private String workflowName;
	
	private String initInfo;
	
	private String status;
	
	private long timeoutMils;
	
	private String currentStep;
	
	private Date gmtBegion;
	
	private Date gmtEnd;

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getWorkflowName() {
		return workflowName;
	}

	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}

	public String getInitInfo() {
		return initInfo;
	}

	public void setInitInfo(String initInfo) {
		this.initInfo = initInfo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public long getTimeoutMils() {
		return timeoutMils;
	}

	public void setTimeoutMils(long timeoutMils) {
		this.timeoutMils = timeoutMils;
	}

	public String getCurrentStep() {
		return currentStep;
	}

	public void setCurrentStep(String currentStep) {
		this.currentStep = currentStep;
	}

	public Date getGmtBegion() {
		return gmtBegion;
	}

	public void setGmtBegion(Date gmtBegion) {
		this.gmtBegion = gmtBegion;
	}

	public Date getGmtEnd() {
		return gmtEnd;
	}

	public void setGmtEnd(Date gmtEnd) {
		this.gmtEnd = gmtEnd;
	}

}
