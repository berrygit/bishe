package berry.dispatch.handler;

import javax.annotation.Resource;

import berry.common.enums.WorkflowInstanceState;
import berry.db.dao.WorkflowInstanceDao;
import berry.db.po.WorkflowInstanceBean;
import berry.dispatch.common.LocalAddress;
import berry.dispatch.enums.ResponseState;
import berry.dispatch.po.RpcCallMessage;
import berry.dispatch.po.RpcResponseMessage;
import berry.engine.WorkflowEngine;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class RpcRequestHandler extends SimpleChannelInboundHandler<RpcCallMessage>{

	@Resource
	private WorkflowEngine worklfowEngine;
	
	@Resource
	private WorkflowInstanceDao workflowInstanceDao;

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, RpcCallMessage msg) throws Exception {
		
		RpcResponseMessage response = new RpcResponseMessage();
		
		response.setMessageId(msg.getMessageId());
		response.setResult(ResponseState.SUCCESS.name());
		response.setWorkflowId(msg.getWorkflowID());
		
		// 返回响应
		ctx.channel().writeAndFlush(response);
		
		WorkflowInstanceBean instance = new WorkflowInstanceBean();
		
		instance.setStatus(WorkflowInstanceState.RUNNING.name());
		instance.setNode(LocalAddress.getIp());
		
		// 否则过滤掉
		if (workflowInstanceDao.updateStatusAndNodeInfoByIdAndNodeisEmpty(instance) == 1) {
			worklfowEngine.execute(instance);
		}
	}

}
