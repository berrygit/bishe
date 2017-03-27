package dispatch.po;

public class HeartBeatMessage implements ChannelMessage {

	private static final long serialVersionUID = 1L;

	private final String nodeInfo;

	public HeartBeatMessage(String nodeInfo) {
		this.nodeInfo = nodeInfo;
	}

	public String getMessage() {
		return nodeInfo;
	}

}
