package berry.dispatch.impl;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import berry.common.enums.WorkflowInstanceState;
import berry.db.dao.WorkflowInstanceDao;
import berry.db.po.WorkflowInstanceBean;
import berry.dispatch.FailOverManager;
import berry.engine.WorkflowEngine;

@Component
public class DefaultFailOverManager implements FailOverManager {

	@Resource
	private WorkflowInstanceDao workflowInstanceDao;

	@Resource
	private WorkflowEngine workflowEngine;

	private ExecutorService executor = Executors.newSingleThreadExecutor();

	@Override
	public void failOver(String worker) {

		executor.execute(new Runnable() {

			@Override
			public void run() {
				List<WorkflowInstanceBean> instances = workflowInstanceDao.getRunningInstanceByNode(worker);

				if (instances == null) {
					return;
				}

				for (WorkflowInstanceBean instance : instances) {

					instance.setNode("");
					instance.setStatus(WorkflowInstanceState.INIT.name());

					try {
						workflowEngine.rollback(instance);
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}

					workflowInstanceDao.updateStatusAndNodeInfoById(instance);
				}
			}
		});
	}
}
