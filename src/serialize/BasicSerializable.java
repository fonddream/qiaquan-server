package serialize;

import java.util.logging.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class BasicSerializable {
	// 可序列化对象基类，继承本类的实例需要参与通信或记录在数据库中
	private static Logger logger = Logger.getLogger(BasicSerializable.class
			.getName());

	protected static SerializeConfig serializeConfig = new SerializeConfig();
	protected static ParserConfig config = new ParserConfig();

	static {
		serializeConfig.setAsmEnable(false);
		config.setAsmEnable(false);
	}

	/**
	 * @param args
	 */
	public BasicSerializable() {
	}

	public String packup() {
		String jsonStr = JSON.toJSONString(this, serializeConfig,
				SerializerFeature.PrettyFormat);
		return jsonStr;
	}

	public static <T> T depack(String jsonStr, Class<T> clz, boolean encrypt) {

		T t = JSON.parseObject(jsonStr, clz, config, 0);
		if (t == null)
			logger.warning("Null as Depack Result:	" + jsonStr + "->" + clz);
		return t;
	}

	@Override
	public String toString() {
		return packup();
	}
}
