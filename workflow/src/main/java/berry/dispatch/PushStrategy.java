package berry.dispatch;

import java.util.List;

public interface PushStrategy {

    String getTargetWorker(List<String> worker);
}