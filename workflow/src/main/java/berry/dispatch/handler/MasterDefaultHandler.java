package berry.dispatch.handler;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import berry.dispatch.FailOverManager;
import berry.dispatch.WorkerManager;
import berry.dispatch.heartbeat.FindHeartbeatEvent;
import berry.dispatch.heartbeat.LostHeartbeatEvent;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

@Component
public class MasterDefaultHandler extends ChannelInboundHandlerAdapter {
	
	private final AttributeKey<String> Worker_ID = AttributeKey.valueOf("MasterDefaultHandler" + this);
	
	@Resource
    private WorkerManager workerManager;
	
	@Resource
	private FailOverManager failOverManager;
	
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
    	
    	Attribute<String> workerId = ctx.channel().attr(Worker_ID);
    	workerManager.removeWorker(workerId.get());
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object msg) {
    	
    	Attribute<String> workerId = ctx.channel().attr(Worker_ID);
    	
        if (msg instanceof FindHeartbeatEvent) {
            String nodeId = ((FindHeartbeatEvent) msg).getNodeId();
            workerId.set(nodeId);
            workerManager.addWorker(nodeId, ctx.channel());
        } else if (msg instanceof LostHeartbeatEvent) {
        	workerManager.removeWorker(workerId.get());
            ctx.channel().close();
            failOverManager.failOver(workerId.get());
        }
    }
}
