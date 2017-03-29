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
import berry.engine.model.interfaces.StepTask;

public class XmlWorkflowInstanceHandler extends DefaultHandler {

	private String currentTag;

	private WorkflowInstanceModel instance;

	private StepTaskModel task;

	private RollbackTaskModel rollbackTask;

	private List<StepTask> stepTaskList;

	private ApplicationContext context;

	public XmlWorkflowInstanceHandler(ApplicationContext context) {
		this.context = context;
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

			String action = attributes.getValue("action");

			String clazz = attributes.getValue("entity");

			Map<String, ?> beans;
			try {
				beans = context.getBeansOfType(Class.forName(clazz));

				if (beans != null && !beans.isEmpty()) {
					task.setActionAndEntity(action, beans.values().iterator().next());
				} else {
					task.setActionAndEntity(action, Class.forName(clazz).newInstance());
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new IllegalStateException(e);
			}
			
			task.setMaxRetry(Integer.valueOf(attributes.getValue("maxRetry")));
			task.setRetryIntervalMlis(Integer.valueOf(attributes.getValue("retryIntervalMlis")));
			task.setRetryStrategy((attributes.getValue("retryStrategy")));

		} else if ("rollback".equals(currentTag)) {
			rollbackTask = new RollbackTaskModel();
			
			String action = attributes.getValue("action");

			String clazz = attributes.getValue("entity");

			Map<String, ?> beans;
			try {
				beans = context.getBeansOfType(Class.forName(clazz));

				if (beans != null && !beans.isEmpty()) {
					rollbackTask.setActionAndEntity(action, beans.values().iterator().next());
				} else {
					rollbackTask.setActionAndEntity(action, Class.forName(clazz).newInstance());
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new IllegalStateException(e);
			}
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

	public WorkflowInstanceModel getInstance() {
		return instance;
	}

}
