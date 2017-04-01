package berry.dispatch.po;

public class RpcCallMessage implements ChannelMessage{

	private static final long serialVersionUID = 1L;
	
	private String messageId;
	
	private String workflowID;

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getWorkflowID() {
		return workflowID;
	}

	public void setWorkflowID(String workflowID) {
		this.workflowID = workflowID;
	}

}
