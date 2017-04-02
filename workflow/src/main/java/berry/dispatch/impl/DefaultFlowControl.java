package berry.dispatch.impl;

import berry.dispatch.FlowControl;


public class DefaultFlowControl implements FlowControl{

    private int flowLimit = 0;

    private int taskCount = 0;

    private long samplingTime = 0L;

    public DefaultFlowControl(int flowLimit) {
        this.flowLimit = flowLimit;
        this.taskCount = 0;
        this.samplingTime = System.currentTimeMillis();
    }

	@Override
	public boolean enable(int nodeCount) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - this.samplingTime > 60000) {
            // 如果当前时间与采样时间的差值大于1分钟，则直接关闭流控
            return true;
        } else if (this.taskCount > (this.flowLimit * nodeCount)) {
            // 如果当前任务数量大于流控阈值，则开启流控
            return false;
        }
        return true;
	}

	@Override
	public void entry(int count) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - this.samplingTime > 60000) {
            // 如果当前时间与采样时间的差值大于1分钟，则重新开始计数
            this.samplingTime = currentTime;
            this.taskCount = count;
        } else {
            // 如果当前时间与采样时间的差值小于1分钟，则累计计数
            this.taskCount += count;
        }
	}

}
