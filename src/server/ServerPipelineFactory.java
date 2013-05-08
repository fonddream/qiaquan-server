package server;

import static org.jboss.netty.channel.Channels.pipeline;

import java.nio.charset.Charset;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;
import org.jboss.netty.handler.timeout.IdleStateHandler;
import org.jboss.netty.util.Timer;

public class ServerPipelineFactory implements ChannelPipelineFactory {

	private Timer timer;

	public ServerPipelineFactory(Timer timer) {
		this.timer = timer;
	}

	public ChannelPipeline getPipeline() throws Exception {

		ChannelPipeline p = pipeline();
		p.addLast("encode", new StringEncoder(Charset.forName("UTF-8")));
		p.addLast("decode", new StringDecoder(Charset.forName("UTF-8")));
		p.addLast("timeout", new IdleStateHandler(timer, 10, 10, 0));
		p.addLast("hearbeat", new ServerHeartBeat());
		p.addLast("handler", new ServerHandler());

		return p;
	};

}
