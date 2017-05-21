package berry.engine.invoke;

import java.util.Date;

import berry.common.exception.TimeoutException;

public class TimeoutChecker {

	public static void check(long timeoutMils, Date gmtBegin) throws TimeoutException {

		if ((gmtBegin.getTime() + timeoutMils) < System.currentTimeMillis()) {
			throw new TimeoutException();
		}
	}
}
