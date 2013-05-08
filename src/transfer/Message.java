package transfer;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.logging.Logger;

import serialize.BasicSerializable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class Message extends BasicSerializable {

	private String msgName;

	private ArrayList<Object> msgArgs;

	private static Logger logger = Logger.getLogger(BasicSerializable.class
			.getName());

	public Message() {

	}

	public Message(String msgName, ArrayList<Object> msgArgs) {
		this.msgName = msgName;
		this.msgArgs = msgArgs;
	}

	@Override
	public String packup() {
		// ��д���л��������ֶ�����URL����
		ArrayList<Object> Args = new ArrayList<Object>();
		for (int i = 0; i < ((msgArgs != null) ? msgArgs.size() : 0); i++)
			try {
				Args.add(msgArgs.get(i)); // ��������
				if (msgArgs.get(i) instanceof String)
					msgArgs.set(
							i,
							java.net.URLEncoder.encode(
									msgArgs.get(i).toString(), "utf-8")
									.replace("+", "%20"));

			} catch (UnsupportedEncodingException e) {
				logger.warning("Fail to Packup DC:	" + e);
			}

		// ���л�
		// logger.debug("Serializing");
		String jsonStr = new String();
		if (Thread.currentThread().getContextClassLoader() != null) {
			jsonStr = JSON.toJSONString(this, serializeConfig,
					SerializerFeature.PrettyFormat);
		} else {
			// �����߳������classloadΪnull����ת���ֶ�ƴ����ʽ
			// ��ʹ��ǿ���÷�ʽ�������÷�����ƣ�
			// jsonStr =jsonStr();
		}

		for (int i = 0; i < ((msgArgs != null) ? msgArgs.size() : 0); i++)
			msgArgs.set(i, Args.get(i)); // �ָ�ԭ��

		// ����
		return jsonStr;
	}

	public String getMsgName() {
		return msgName;
	}

	public void setMsgName(String msgName) {
		this.msgName = msgName;
	}

	public ArrayList<Object> getMsgArgs() {
		return msgArgs;
	}

	public void setMsgArgs(ArrayList<Object> msgArgs) {
		this.msgArgs = msgArgs;
	}

	public static Message depack(String jsonStr) {
		// ����
		// jsonStr = encrypt ? (new CmdCipher().deCipher(jsonStr)) : jsonStr;

		// �����л�
		ParserConfig config = new ParserConfig();
		config.setAsmEnable(false);
		Message dc = JSON.parseObject(jsonStr, Message.class, config, 0);
		if (dc == null) {
			logger.warning("Null DC as Depack Result");
			return null;
		}

		// ��������
		ArrayList<Object> Args = dc.getMsgArgs();
		for (int i = 0; i < ((Args != null) ? Args.size() : 0); i++)
			try {
				if (Args.get(i) instanceof String)
					Args.set(i, java.net.URLDecoder.decode(Args.get(i)
							.toString(), "utf-8"));
			} catch (UnsupportedEncodingException e) {
				logger.warning("Fail to Depack DC:	" + e);
			}
		return dc;
	}

}
