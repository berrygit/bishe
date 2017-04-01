package berry.dispatch.handler;

import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;

import berry.dispatch.common.LocalCache;
import berry.dispatch.po.RpcResponseMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class RpcResponseHandler extends SimpleChannelInboundHandler<RpcResponseMessage> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, RpcResponseMessage msg) throws Exception {
		String messageId = msg.getMessageId();
		
		Exchanger<String> exchanger = LocalCache.get(messageId);
		
		if (exchanger != null) {
			exchanger.exchange(msg.getResult(), 3, TimeUnit.SECONDS);
		}
	}

}
