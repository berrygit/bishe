package berry.db.po;

import java.util.Date;

public class WorkflowTaskBean {
	
	private String workflowId;
	
	private String taskName;
	
	private String status;
	
	private String input;
	
	private String output;
	
	private String excetionMessage;
	
	private String node;
	
	private Date gmtBegion;
	
	private Date gmtEnd;

	public String getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(String workflowId) {
		this.workflowId = workflowId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public String getExcetionMessage() {
		return excetionMessage;
	}

	public void setExcetionMessage(String excetionMessage) {
		this.excetionMessage = excetionMessage;
	}

	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		this.node = node;
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
