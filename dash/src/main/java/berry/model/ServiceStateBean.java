package berry.model;

public class ServiceStateBean {
	
	private String serviceName;

	private long successCount;
	
	private long failedCount;
	
	private long timeoutCount;
	
	private long max;
	
	private long min;
	
	private long average;
	
	public long getMax() {
		return max;
	}

	public void setMax(long max) {
		this.max = max;
	}

	public long getMin() {
		return min;
	}

	public void setMin(long min) {
		this.min = min;
	}

	public long getAverage() {
		return average;
	}

	public void setAverage(long average) {
		this.average = average;
	}

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

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}


}
