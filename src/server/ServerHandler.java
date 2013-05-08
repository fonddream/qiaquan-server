package server;

import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelState;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import transfer.Message;
import transfer.MessageHandler;

public class ServerHandler extends SimpleChannelUpstreamHandler {

	private static final Logger logger = Logger.getLogger(ServerHandler.class
			.getName());

	private final AtomicLong transferredMessages = new AtomicLong();

	public long getTransferredMessages() {
		return transferredMessages.get();
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {

		logger.info("client connected success!!!");

	}

	@Override
	public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e)
			throws Exception {
		if (e instanceof ChannelStateEvent
				&& ((ChannelStateEvent) e).getState() != ChannelState.INTEREST_OPS) {
			logger.info(e.toString());
		}
		super.handleUpstream(ctx, e);
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		Message msg = Message.depack(e.getMessage().toString());
		if (msg == null) {
			logger.warning("Null DC");
			return;
		}

		if (!msg.getMsgName().equals("receiveMsg")) {
			logger.info("Receive Comm from Android:	" + e.getChannel() + "\n"
					+ msg);
			logger.info(msg + "--"
					+ e.getChannel().getRemoteAddress().toString() + "--"
					+ e.getChannel().getLocalAddress().toString());
		}
		Message backMsg = MessageHandler.handler(msg);
		if ((backMsg != null) && (e.getChannel().isConnected())
				&& (e.getChannel().isOpen()))
			e.getChannel().write(backMsg.packup());
	}

	// // 如果没有用户ID，改以连接的remoteAddress作为ID
	// if ((dc.getClientIP() == null)
	// || (dc.getClientIP().equals(CommBean.ID_UNKNOWN)))
	// dc.setClientIP(e.getChannel().getRemoteAddress().toString());
	//
	// // 执行
	// DistCommunication dcRes = CoreServer.instance.handleDC(dc);
	// if ((dcRes != null) && (e.getChannel().isConnected())
	// && (e.getChannel().isOpen()))
	// e.getChannel().write(dcRes.packup(encrypt));
	// else
	// logger.warn("Null dcRes or Channel Closed");

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		logger.log(Level.WARNING, "Unexpected exception from downstream.",
				e.getCause());
		e.getChannel().close();
	}

}
