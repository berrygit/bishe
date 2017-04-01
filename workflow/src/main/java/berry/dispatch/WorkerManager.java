package berry.dispatch;

import berry.dispatch.common.NosuitableWorkerException;
import io.netty.channel.Channel;

public interface WorkerManager {

	Channel getSuitableWorker() throws NosuitableWorkerException;

	void removeWorker(String key);

	void addWorker(String key, Channel channel);

	void clear();

}
