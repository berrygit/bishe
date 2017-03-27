import dispatch.impl.DefaultRpcWorkerService;
import dispatch.po.HeartBeatMessage;

public class Main2 {

	public static void main(String[] args) throws Exception {
		DefaultRpcWorkerService workerService = new DefaultRpcWorkerService();

		workerService.start("localhost", 8080);
		workerService.sendMessage(new HeartBeatMessage("hello"));
		workerService.sendMessage(new HeartBeatMessage("hello"));
		workerService.sendMessage(new HeartBeatMessage("hello"));
		workerService.sendMessage(new HeartBeatMessage("hello"));
		workerService.sendMessage(new HeartBeatMessage("hello"));
	}

}
