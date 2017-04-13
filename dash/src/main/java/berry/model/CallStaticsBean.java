package berry.model;

public class CallStaticsBean {

	private long successCount;
	
	private long failedCount;
	
	private long timeoutCount;

	public long getSuccessCount() {
		return successCount;
	}

	public void setSuccessCount(long successCount) {
		this.successCount = successCount;
	}

	public long getFailedCount() {
		return failedCount;
	}

	public void setFailedCount(long failedCount) {
		this.failedCount = failedCount;
	}

	public long getTimeoutCount() {
		return timeoutCount;
	}

	public void setTimeoutCount(long timeoutCount) {
		this.timeoutCount = timeoutCount;
	}

}
