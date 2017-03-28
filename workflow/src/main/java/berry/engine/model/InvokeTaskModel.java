package berry.engine.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import berry.api.WorkflowContext;
import berry.engine.model.interfaces.Task;

public class InvokeTaskModel implements Task {

	private Object entity;

	private Method method;

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> invoke(WorkflowContext context) throws Throwable {

		Map<String, Object> result = null;

		try {
			result = (Map<String, Object>) method.invoke(this.entity, context);
		} catch (IllegalAccessException | IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException t) {
			throw t.getTargetException();
		}

		return result;

	}

}
