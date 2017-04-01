package berry.dispatch.po;

public class RpcResponseMessage implements ChannelMessage {

	private static final long serialVersionUID = 1L;
	
	private String messageId;
	
	private String workflowId;
	
	private String result;

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(String workflowId) {
		this.workflowId = workflowId;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

}
