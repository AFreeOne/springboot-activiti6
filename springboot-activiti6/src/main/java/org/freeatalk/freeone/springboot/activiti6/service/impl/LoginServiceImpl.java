package org.freeatalk.freeone.springboot.activiti6.service.impl;

import org.freeatalk.freeone.springboot.activiti6.dao.TUserMapper;
import org.freeatalk.freeone.springboot.activiti6.entity.TUser;
import org.freeatalk.freeone.springboot.activiti6.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

	@Autowired
	private TUserMapper userMapper;
	
	@Override
	public TUser login(String username, String password) {
		return userMapper.getUserByUsernamePassword(username, password);
	}

	
}
