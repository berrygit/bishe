package berry.engine;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import berry.common.exception.NotFindFlowException;
import berry.engine.model.interfaces.Instance;
import berry.engine.parser.Parser;

@Component
public class WorkflowMetaInfo {

	@Resource
	private Parser parser;

	private Map<String, Instance> instances;

	@Value("${workflow.sequence.define.path}")
	private String path;

	@PostConstruct
	private void init() throws Exception {
		instances = parser.parse(path);
	}

	public Instance getInstanceInfo(String workflowName) throws NotFindFlowException {
		
		Instance workflowInstance = instances.get(workflowName);
		
		if (workflowInstance == null) {
			System.out.println("can't find workflow info");
			throw new NotFindFlowException();
		}
		
		return workflowInstance;

	}
}
