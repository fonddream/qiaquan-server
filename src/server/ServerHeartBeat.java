package server;

import java.util.logging.Logger;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.timeout.IdleState;
import org.jboss.netty.handler.timeout.IdleStateAwareChannelHandler;
import org.jboss.netty.handler.timeout.IdleStateEvent;

public class ServerHeartBeat extends IdleStateAwareChannelHandler {
	private static Logger logger = Logger.getLogger(ServerHeartBeat.class
			.getName());

	@Override
	public void channelIdle(ChannelHandlerContext ctx, IdleStateEvent e)
			throws Exception {
		super.channelIdle(ctx, e);
		if (e.getState() == IdleState.READER_IDLE) {
			e.getChannel().close();
			logger.info("heart stop beating ! disconnecting ...");
		}

	}
}
