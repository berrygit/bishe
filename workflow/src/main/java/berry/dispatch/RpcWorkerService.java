package berry.dispatch;

public interface RpcWorkerService {

	void start(String host, int port) throws Exception;

	void stop() throws InterruptedException;

}
