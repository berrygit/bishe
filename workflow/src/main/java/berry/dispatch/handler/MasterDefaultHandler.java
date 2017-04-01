package berry.dispatch.handler;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import berry.dispatch.WorkerManager;
import berry.dispatch.heartbeat.FindHeartbeatEvent;
import berry.dispatch.heartbeat.LostHeartbeatEvent;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

@Component
public class MasterDefaultHandler extends ChannelInboundHandlerAdapter {
	
	private final AttributeKey<String> SLAVE_ID = AttributeKey.valueOf("MasterDefaultHandler");
	
	@Resource
    private WorkerManager workerManager;

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
    	
    	Attribute<String> slaveId = ctx.channel().attr(SLAVE_ID);
    	workerManager.removeWorker(slaveId.get());
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object msg) {
    	
    	Attribute<String> slaveId = ctx.channel().attr(SLAVE_ID);
    	
        if (msg instanceof LostHeartbeatEvent) {
        	workerManager.removeWorker(slaveId.get());
            ctx.channel().close();
        } else if (msg instanceof FindHeartbeatEvent) {
            String nodeId = ((FindHeartbeatEvent) msg).getNodeId();
            slaveId.set(nodeId);
            workerManager.addWorker(nodeId, ctx.channel());
        }
    }
}
