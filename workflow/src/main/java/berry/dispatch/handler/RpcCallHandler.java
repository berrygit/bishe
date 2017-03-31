package berry.dispatch.handler;

import berry.dispatch.po.RpcCallMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class RpcCallHandler extends SimpleChannelInboundHandler<RpcCallMessage>{

	@Override
	protected void channelRead0(ChannelHandlerContext arg0, RpcCallMessage arg1) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
