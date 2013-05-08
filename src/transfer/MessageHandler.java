package transfer;

import java.util.ArrayList;

import db.DBHelper;

public class MessageHandler {
	private static final String BUSUBELOGIN = "BUSUBELOGIN";

	public static Message handler(Message msg) {
		switch (msg.getMsgName()) {
		case BUSUBELOGIN:

			ArrayList<Object> result = new DBHelper().busineLogin((String) msg
					.getMsgArgs().get(0), (String) msg.getMsgArgs().get(1));
			Message backMsg = new Message();
			backMsg.setMsgName(BUSUBELOGIN);
			backMsg.setMsgArgs(result);
			return backMsg;
		default:
			return null;
		}

		// 执行指令方法
	}
}
