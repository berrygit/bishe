package berry.api;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import berry.dispatch.po.ChannelMessage;

public class WorkflowContext implements ChannelMessage {

	private static final long serialVersionUID = 1L;

	private Map<String, Object> context = new ConcurrentHashMap<String, Object>();

	public Object getInfo(String key) {
		return context.get(key);
	}

	public void setInfo(String key, Object value) {
		context.put(key, value);
	}

}
