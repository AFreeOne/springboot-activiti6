package org.freeatalk.freeone.springboot.activiti6.service;

import org.freeatalk.freeone.springboot.activiti6.entity.TUser;

public interface  LoginService {

	/**
	 * 登陆的方法
	 * @param username 用户名
	 * @param password 
	 * @return
	 */
	TUser login(String username ,String password);
	
}
