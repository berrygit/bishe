package berry.api;

import java.util.HashMap;
import java.util.Map;

public class WorkflowContext {

	private Map<String, Object> context = new HashMap<String, Object>();

	public Map<String, Object> getContext() {
		return context;
	}

	public void setContext(Map<String, Object> context) {
		this.context = context;
	}

}
