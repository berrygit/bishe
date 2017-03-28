package berry.engine.model;

import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import berry.engine.model.interfaces.Instance;
import berry.engine.model.interfaces.RollbackTask;
import berry.engine.model.interfaces.StepTask;


public class WorkflowInstanceModel implements Instance {

	private String name;

	private List<StepTask> stepTaskList;

	private RollbackTask rollbackTask;

	public WorkflowInstanceModel(List<StepTask> stepTaskList) {
		this.stepTaskList = stepTaskList;
	}

	public RollbackTask getRollbackTask() {
		return rollbackTask;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<StepTask> getStepTaskList() {
		return stepTaskList;
	}

	public void setRollbackTask(RollbackTask rollbackTask) {
		this.rollbackTask = rollbackTask;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
