package client_test;

import java.util.logging.Logger;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.timeout.IdleState;
import org.jboss.netty.handler.timeout.IdleStateAwareChannelHandler;
import org.jboss.netty.handler.timeout.IdleStateEvent;

public class ClientHeartBeat extends IdleStateAwareChannelHandler {
	private static Logger logger = Logger.getLogger(ClientHeartBeat.class
			.getName());

	@Override
	public void channelIdle(ChannelHandlerContext ctx, IdleStateEvent e)
			throws Exception {
		if (e.getState() == IdleState.WRITER_IDLE) {
			e.getChannel().write("heartbeat package");
		}
		if (e.getState() == IdleState.READER_IDLE) {
			e.getChannel().close();
			logger.info("client close");
		}
	}
}
