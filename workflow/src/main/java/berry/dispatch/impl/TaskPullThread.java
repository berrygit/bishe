package berry.dispatch.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import com.aliyun.grandcanal.schedule.master.ConnectedSlaveManager;
import com.aliyun.grandcanal.schedule.master.TaskWrapper;
import com.aliyun.grandcanal.schedule.master.WrappedTask;
import com.aliyun.grandcanal.schedule.master.traffic.TaskFlowControl;
import com.aliyun.grandcanal.schedule.task.SchedulableTask;
import com.aliyun.grandcanal.schedule.task.TaskManager;
import com.google.common.collect.Collections2;

public class TaskPullThread implements Runnable{

	// 任务拉取周期，缺省为100毫秒
	private static final long PULL_CYCLE = 100L;
	private static final int TOLERANT_BLOCK_CYCLES = 5;

	private final int queueSizeLimit;

	private final TaskManager taskManager;
	private final Queue<WrappedTask> freshTaskQueue;
	private final Map<String, WrappedTask> runningTaskMap;

	// 每分钟能够拉取的最大任务数量
	private final TaskFlowControl flowController;

	// SlaveManager,用于获取当前可用的节点数量
	private final ConnectedSlaveManager slaveManager;

	int waitCount = 0;

	public TaskPullThread(TaskManager taskManager,
			Queue<WrappedTask> freshTaskQueue,
			Map<String, WrappedTask> runningTaskMap, int queueSizeLimit,
			int flowLimit, ConnectedSlaveManager slaveManager) {
		this.taskManager = taskManager;
		this.freshTaskQueue = freshTaskQueue;
		this.runningTaskMap = runningTaskMap;
		this.queueSizeLimit = queueSizeLimit;

		// 设置SlaveManager
		this.slaveManager = slaveManager;

		// 任务流控，防止工作流引擎被调度任务冲垮
		this.flowController = new TaskFlowControl(flowLimit);
	}

	@Override
	public void run() throws InterruptedException {

		try {
			int nodes = this.slaveManager.getOnlineSlaves();
			if (flowController.enableControl(nodes)) {
				// 如果每分钟拉取的调度任务超过设置的流控阈值，则需要等到到可用的时间窗口继续处理
				log.warn("Task pull thread is suspended by flow control, wait for next round execution...");
			} else if (freshTaskQueue.size() >= queueSizeLimit) {
				// 如果发送队列已满，则增加一个等待周期。
				if ((++waitCount) > TOLERANT_BLOCK_CYCLES) {
					// 发送队列发送不出去，这里应该报警了
					// AlertUtil.Alert("ScheduleMaster", "send-queue-blocked",
					// "send queue block for too long");
					log.warn(String
							.format("SendQueue has kept full in %d pull-data cycle (%d ms). Keep wait now.",
									(waitCount - 1), PULL_CYCLE
											* (waitCount - 1)));
				}
			} else {
				waitCount = 0;
				List<SchedulableTask> tasks = taskManager.getPendingTasks();
				if (tasks != null && !tasks.isEmpty()) {
					Collection<SchedulableTask> pendingTasks = filterDuplicate(
							tasks, runningTaskMap);
					Collection<WrappedTask> pendingWrappedTask = Collections2
							.transform(pendingTasks, new TaskWrapper());
					if (!pendingWrappedTask.isEmpty()) {
						// 增加调度任务处理数量
						this.flowController.entry(pendingWrappedTask.size());

						log.info("Load fresh tasks: "
								+ pendingWrappedTask.toString());
						for (WrappedTask task : pendingWrappedTask) {
							runningTaskMap.put(task.getTask().getId(), task);
							freshTaskQueue.offer(task);
						}
						return;
					}
				} else {
					runningTaskMap.clear();
				}
			}
		} catch (Exception e) {
			log.error("pull workflow task from database failed.", e);
		}
		Thread.sleep(PULL_CYCLE);
	}

	/**
	 * <p>
	 * 在 runningTaskMap中去掉与 tasks中不重复的元素
	 * <p>
	 * 说明：这是设计相关的， Worker拿到task后会将数据库中相应记录(gc_workflow_instance)置为在执行状态，
	 * 则Master拉取任务时就不会再拉取该条数据，即tasks中就没有这条数据了。 表示任务已经被调度到Worker，记录不应该在Master中了。
	 * <p>
	 * 在 tasks中去掉与 runningTaskMap中重复的元素
	 * <p>
	 * 说明：runningTaskMap中元素表示任务还没有被调度，task记录在Master中，或在发送的链路上，还未抵达Worker。
	 * 新拉取的相同task不需要再发送一遍。
	 * 
	 * @param tasks
	 *            准备过滤的数据
	 * @param runningTaskMap
	 *            正在运行的任务列表
	 * @return 过滤后的数据，即tasks本身。（tasks是出入参）
	 */
	Collection<SchedulableTask> filterDuplicate(List<SchedulableTask> tasks,
			Map<String, ? extends Object> runningTaskMap) {
		/*
		 * tasks已经被检查过非空，这里不需要重新检查一次。
		 */
		if (runningTaskMap.isEmpty()) {
			return tasks;
		}

		// 首先从等待调度任务列表和运行中任务集合中找出重复的任务id, 然后将这些重复任务ID加入集合，并且从等待调度任务列表中删除
		// 缓存tasks里的id，并去除tasks中已经在runningTaskMap中的任务。
		Set<String> repeatIds = new HashSet<String>();
		for (Iterator<SchedulableTask> iterator = tasks.iterator(); iterator
				.hasNext();) {
			SchedulableTask task = iterator.next();
			String id = task.getId();

			if (runningTaskMap.containsKey(id)) {
				repeatIds.add(id);
				iterator.remove();
			}
		}

		// 将运行中任务集合中不重复的任务id去除
		// 已经完成的任务从runningTaskMap去掉
		for (Iterator<String> iterator = runningTaskMap.keySet().iterator(); iterator
				.hasNext();) {
			String runningTaskId = iterator.next();
			if (!repeatIds.contains(runningTaskId)) {
				iterator.remove();
			}
		}
		return tasks;
	}
	
}
