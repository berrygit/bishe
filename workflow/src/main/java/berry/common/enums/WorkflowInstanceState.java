package berry.common.enums;

public enum WorkflowInstanceState {

	INIT, // 初始化
	RUNNING, // 运行中
	FINISH, // 完成
	FAILED, // 失败
	TIMEOUT, // 超时
	SCHEDULABLE, // 可调度
	SCHEDULE, // 调度中
	HUMAN_ROLLBACK, //人工回滚
	HUMAN_RETRY // 人工重试
	
}
