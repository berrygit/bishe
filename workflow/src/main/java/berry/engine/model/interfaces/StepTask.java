package berry.engine.model.interfaces;

public interface StepTask extends Task {

	long getMaxRetry();

	long getRetryIntervalMlis();
	
	String getRetryStrategy();
	
	void setRetryStrategy(String retryStrategy);
	
	void setMaxRetry(long maxRetry);
	
	void setRetryIntervalMlis(long retryIntervalMlis);
	
	void setAction(String action);

}
