package berry.engine.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import berry.engine.model.RollbackTaskModel;
import berry.engine.model.StepTaskModel;
import berry.engine.model.WorkflowInstanceModel;
import berry.engine.model.interfaces.RollbackTask;
import berry.engine.model.interfaces.StepTask;
import berry.engine.model.interfaces.Task;

public class XmlWorkflowInstanceHandler extends DefaultHandler {

	private String currentTag;

	private WorkflowInstanceModel instance;

	private StepTask task;

	private RollbackTask rollbackTask;

	private List<StepTask> stepTaskList;

	private ApplicationContext context;
	
	private boolean loadOnlyFromSpring = false;

	public XmlWorkflowInstanceHandler(ApplicationContext context, boolean loadOnlyFromSpring) {
		this.context = context;
		this.loadOnlyFromSpring = loadOnlyFromSpring;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		currentTag = qName;

		if ("workflow-sequence".equals(currentTag)) {

			stepTaskList = new ArrayList<StepTask>();
			instance = new WorkflowInstanceModel(stepTaskList);

			instance.setName(attributes.getValue("name"));
			instance.setTimeoutMils(Long.valueOf(attributes.getValue("timeoutMils")));

		} else if ("step".equals(currentTag)) {
			task = new StepTaskModel();
			
			setInvodeMeteInfo(attributes, task);
			
			task.setAction(attributes.getValue("action"));
			task.setMaxRetry(Long.valueOf(attributes.getValue("maxRetry")));
			task.setRetryIntervalMlis(Long.valueOf(attributes.getValue("retryIntervalMlis")));
			task.setRetryStrategy((attributes.getValue("retryStrategy")));

		} else if ("rollback".equals(currentTag)) {
			
			rollbackTask = new RollbackTaskModel();
			
			setInvodeMeteInfo(attributes, rollbackTask);
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if ("step".equals(currentTag)) {

			stepTaskList.add(task);

		} else if ("rollback".equals(currentTag)) {

			instance.setRollbackTask(rollbackTask);
		}
	}
	
	private void setInvodeMeteInfo(Attributes attributes, Task task){
		
		String method = attributes.getValue("method");

		String clazz = attributes.getValue("entity");

		Map<String, ?> beans;
		try {
			beans = context.getBeansOfType(Class.forName(clazz));

			if (beans != null && !beans.isEmpty()) {
				task.setInvokeMetaInfo(method, beans.values().iterator().next());
			} else {
				
				if (loadOnlyFromSpring){
					throw new IllegalStateException("can't bean find in spring");
				}
				
				task.setInvokeMetaInfo(method, Class.forName(clazz).newInstance());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalStateException(e);
		}
	}

	public WorkflowInstanceModel getInstance() {
		return instance;
	}

}
