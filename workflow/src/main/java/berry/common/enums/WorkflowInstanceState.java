package berry.common.enums;

public enum WorkflowInstanceState {

	INIT, // 可调度的
	RUNNING, // 运行中
	FINISH, // 完成
	FAILED, // 失败
	TIMEOUT, // 超时
	TERMINATED // 异常终止

}
