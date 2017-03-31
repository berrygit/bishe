package berry.dispatch.handler;

import berry.dispatch.po.RpcResponseMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class RpcResponseHandler extends SimpleChannelInboundHandler<RpcResponseMessage>{

	@Override
	protected void channelRead0(ChannelHandlerContext arg0, RpcResponseMessage arg1) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
