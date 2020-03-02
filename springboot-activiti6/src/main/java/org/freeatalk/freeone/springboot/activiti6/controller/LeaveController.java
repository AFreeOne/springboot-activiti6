package org.freeatalk.freeone.springboot.activiti6.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.freeatalk.freeone.springboot.activiti6.entity.TLeave;
import org.freeatalk.freeone.springboot.activiti6.entity.TUser;
import org.freeatalk.freeone.springboot.activiti6.service.LeaveService;
import org.freeatalk.freeone.springboot.activiti6.utils.BaseKit;
import org.freeatalk.freeone.springboot.activiti6.utils.ResultBack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@RequestMapping("leave")
@Controller
public class LeaveController{

	@Autowired
	private LeaveService leaveService;
	
	@RequestMapping("addLeave")
	@ResponseBody
	public ResultBack<Object>  addLeave(HttpSession session , @RequestParam String type,@RequestParam String remark,@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime) {
		TUser user = (TUser) session.getAttribute("user");
		boolean success = leaveService.addLeave(BaseKit.uuid(), type, startTime, endTime, remark, user.getId());
		return new ResultBack<>(success, success? "提交成功":"提交失败");
	}
	
	@RequestMapping("pagelistMyLeaves")
	@ResponseBody
	public ResultBack<Object> pagelistMyLeaves(Integer pageNum, Integer pageSize,HttpSession session) {
		PageHelper.startPage(pageNum, pageSize);
		TUser user = (TUser) session.getAttribute("user");
		List<TLeave> list = leaveService.listMyLeaves(user.getId(), null);
		return new ResultBack<>(new PageInfo<>(list));
	}
}
