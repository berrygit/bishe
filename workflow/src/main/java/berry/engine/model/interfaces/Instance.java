package berry.engine.model.interfaces;

import java.util.List;

public interface Instance {

	String getName();

	long getTimeoutMils();

	RollbackTask getRollbackTask();

	List<StepTask> getStepTaskList();
}
