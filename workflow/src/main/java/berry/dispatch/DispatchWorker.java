package berry.dispatch;

/**
 * Created by john on 2017/3/21.
 */
public interface DispatchWorker {

	void start();

	void stop();

	void changeLeader(String leader);
}
