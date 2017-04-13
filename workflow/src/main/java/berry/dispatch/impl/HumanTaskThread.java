package berry.dispatch.impl;

import java.util.List;

import berry.common.enums.WorkflowInstanceState;
import berry.db.dao.WorkflowInstanceDao;
import berry.db.po.WorkflowInstanceBean;
import berry.engine.WorkflowEngine;

public class HumanTaskThread implements Runnable {

	private WorkflowInstanceDao workflowInstanceDao;

	private WorkflowEngine workflowEngine;

	HumanTaskThread(WorkflowInstanceDao workflowInstanceDao, WorkflowEngine workflowEngine) {
		this.workflowInstanceDao = workflowInstanceDao;
		this.workflowEngine = workflowEngine;
	}

	@Override
	public void run() {

		List<WorkflowInstanceBean> humanOperationInstance = workflowInstanceDao.getHumanOperationInstance();

		if (humanOperationInstance != null) {

			for (WorkflowInstanceBean instanceBean : humanOperationInstance) {
				if (WorkflowInstanceState.HUMAN_RETRY.name().equals(instanceBean.getStatus())) {
					try {
						workflowEngine.executeForHumanRetry(instanceBean);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				if (WorkflowInstanceState.HUMAN_ROLLBACK.name().equals(instanceBean.getStatus())) {
					try {
						workflowEngine.executeForHumanRollback(instanceBean);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

	}

}
