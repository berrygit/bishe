package berry.engine.model.interfaces;

public interface StepTask extends Task {

	String getStepName();

	long getMaxRetry();

	long getRetryIntervalMlis();
	
	String getRetryStrategy();
}
