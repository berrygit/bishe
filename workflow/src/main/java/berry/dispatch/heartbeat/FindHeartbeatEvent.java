package berry.dispatch.heartbeat;

public class FindHeartbeatEvent implements HeartbeatEvent{
	
	private String nodeId;

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	
}
