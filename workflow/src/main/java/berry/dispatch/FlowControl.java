package berry.dispatch;

public interface FlowControl {

    public boolean enable(int nodeCount);

    public void entry(int count);
}
