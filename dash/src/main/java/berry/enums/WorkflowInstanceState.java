package berry.enums;

public enum WorkflowInstanceState {

	INIT, // 可调度的
	RUNNING, // 运行中
	FINISH, // 完成
	FAILED, // 失败
	TIMEOUT, // 超时
	SCHEDULE, // 调度中
	HUMAN_ROLLBACK, //人工回滚
	HUMAN_RETRY
	
}
