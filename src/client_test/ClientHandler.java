/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package client_test;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import transfer.Message;

/**
 * Handler implementation for the echo client. It initiates the ping-pong
 * traffic between the echo client and server by sending the first message to
 * the server.
 */
public class ClientHandler extends SimpleChannelUpstreamHandler {

	private static final Logger logger = Logger.getLogger(ClientHandler.class
			.getName());

	private final ChannelBuffer firstMessage;
	private final AtomicLong transferredBytes = new AtomicLong();

	/**
	 * Creates a client-side handler.
	 */
	public ClientHandler(int firstMessageSize) {
		if (firstMessageSize <= 0) {
			throw new IllegalArgumentException("firstMessageSize: "
					+ firstMessageSize);
		}
		firstMessage = ChannelBuffers.buffer(firstMessageSize);
	}

	public long getTransferredBytes() {
		return transferredBytes.get();
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		// Send the first message. Server will not send anything here
		// because the firstMessage's capacity is 0.

		ArrayList<Object> args = new ArrayList<Object>();
		args.add("qiaquancom@qq.com");
		args.add("0f86c6ca50c13d6e2a6a05b3cc0dffcd");
		Message msg = new Message("BUSUBELOGIN", args);
		String msgJson = msg.packup();
		e.getChannel().write(msgJson);
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		// Send back the received message to the remote peer.
		// transferredBytes.addAndGet(((ChannelBuffer) e.getMessage())
		// .readableBytes());
		// e.getChannel().write(e.getMessage());

		// System.out.println(transferredBytes.get());

		logger.info((String) e.getMessage());
		// e.getChannel().write(e.getMessage());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		// Close the connection when an exception is raised.
		logger.log(Level.WARNING, "Unexpected exception from downstream.",
				e.getCause());
		e.getChannel().close();
	}
}