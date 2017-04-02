package berry.dispatch.impl;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import berry.common.enums.WorkflowInstanceState;
import berry.db.dao.WorkflowInstanceDao;
import berry.db.po.WorkflowInstanceBean;
import berry.dispatch.FlowControl;
import berry.dispatch.WorkerManager;

public class TaskPullThread implements Runnable {

	// 任务拉取周期，缺省为100毫秒
	private static final long PULL_CYCLE = 100L;

	// 每分钟能够拉取的最大任务数量
	private final FlowControl flowController;

	private WorkflowInstanceDao workflowInstanceDao;

	private BlockingQueue<WorkflowInstanceBean> workflowQueue;

	private WorkerManager workerManager;

	public TaskPullThread(WorkflowInstanceDao workflowInstanceDao, BlockingQueue<WorkflowInstanceBean> workflowQueue,
			int flowLimit, WorkerManager workerManager) {

		// 设置SlaveManager
		this.workerManager = workerManager;

		this.flowController = new DefaultFlowControl(flowLimit);

		this.workflowInstanceDao = workflowInstanceDao;

		this.workflowQueue = workflowQueue;
	}

	@Override
	public void run() {

		boolean firstRun = true;

		while (true) {

			// 第一次启动，捞取未调度完成的任务
			if (firstRun) {
				List<WorkflowInstanceBean> workflows = workflowInstanceDao.getScheduleInstance();

				for (WorkflowInstanceBean workflow : workflows) {
					workflowQueue.offer(workflow);
				}
			}

			firstRun = false;

			int nodes = this.workerManager.getWorkerCount();
			if (flowController.enable(nodes)) {

				List<WorkflowInstanceBean> workflows = workflowInstanceDao.getBeScheduleInstance();

				for (WorkflowInstanceBean instance : workflows) {

					flowController.entry(1);

					workflowQueue.offer(instance);

					instance.setStatus(WorkflowInstanceState.SCHEDULE.name());

					// 更新状态
					workflowInstanceDao.updateStatus(instance);
				}
			}

			try {
				Thread.sleep(PULL_CYCLE);
			} catch (InterruptedException e) {
				e.printStackTrace();
				break;
			}
		}
	}
}
