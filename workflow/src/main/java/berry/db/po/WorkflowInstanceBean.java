package berry.db.po;

import java.util.Date;

public class WorkflowInstanceBean {
	
	private String id;

	private String requestId;

	private String workflowName;

	private String initInfo;

	private String status;

	private long timeoutMils;

	private Date gmtBegin;

	private Date gmtUpdate;

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

	public Date getGmtBegin() {
		return gmtBegin;
	}

	public void setGmtBegin(Date gmtBegin) {
		this.gmtBegin = gmtBegin;
	}

	public Date getGmtUpdate() {
		return gmtUpdate;
	}

	public void setGmtUpdate(Date gmtUpdate) {
		this.gmtUpdate = gmtUpdate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
