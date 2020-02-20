package org.freeatalk.freeone.springboot.activiti6.controller;

import javax.servlet.http.HttpSession;

import org.freeatalk.freeone.springboot.activiti6.entity.TUser;
import org.freeatalk.freeone.springboot.activiti6.service.LoginService;
import org.freeatalk.freeone.springboot.activiti6.utils.ResultBack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/login")
public class LoginController {

	@Autowired
	private LoginService loginService;
	
	@RequestMapping({""})
	public ModelAndView login(ModelAndView mv) {
		mv.setViewName("login");
		return mv;
	}
	
	
	 /**
	  * 
	  * @param username
	  * @param password
	  * @param session
	  * @return
	  */
	@RequestMapping(value= {"login"},method = RequestMethod.POST)
	@ResponseBody
	public ResultBack<Object> login(@RequestParam String username,@RequestParam String password,HttpSession session) {
		TUser user = loginService.login(username, password);
		if(user == null) {
			return new ResultBack<>(false, "login failed");
		}else {
			session.setAttribute("user", user);
			return new ResultBack<>(true, "login success");
		}
	}
	
}
