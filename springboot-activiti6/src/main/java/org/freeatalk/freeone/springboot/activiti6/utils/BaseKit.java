package org.freeatalk.freeone.springboot.activiti6.utils;

/**
 * 常用的工具类
 * @author freeone
 *
 */
public class BaseKit {
	
	private BaseKit() {}
	/**
	 * 获取UUID
	 * @return
	 */
	public static String uuid() {
		return java.util.UUID.randomUUID().toString().replace("-", "");
	}
	
}
