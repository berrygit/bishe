package berry.engine.retry;

import berry.engine.retry.interfaces.RetryStrategy;

public class RetryStrategyFactory {

	public static RetryStrategy get(String strategy) {

		if ("const".equals(strategy)) {
			return new ConstRetryStrategy();
		} else if ("step".equals(strategy)) {
			return new StepRetryStrategy();
		} else if ("index".equals(strategy)) {
			return new IndexRetryStrategy();
		}

		return null;

	}

}
