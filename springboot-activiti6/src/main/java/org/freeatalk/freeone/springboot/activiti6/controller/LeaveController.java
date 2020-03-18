package org.freeatalk.freeone.springboot.activiti6.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.freeatalk.freeone.springboot.activiti6.entity.TLeave;
import org.freeatalk.freeone.springboot.activiti6.entity.TUser;
import org.freeatalk.freeone.springboot.activiti6.service.LeaveService;
import org.freeatalk.freeone.springboot.activiti6.utils.BaseKit;
import org.freeatalk.freeone.springboot.activiti6.utils.ResultBack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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
	

    @Autowired
    private IdentityService identityservice;

    @Autowired
    private RuntimeService runtimeservice;

    @Autowired
    private TaskService taskService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private RepositoryService repositoryService;
	
	
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
	
	@RequestMapping("pageListLeavesWithCourseTeacher")
    @ResponseBody
    public ResultBack<Object> pageListLeavesWithCourseTeacher(Integer pageNum, Integer pageSize,HttpSession session) {
	    PageHelper.startPage(pageNum, pageSize);
        TUser user = (TUser) session.getAttribute("user");
        List<TLeave> list = leaveService.listLeavesWithCourseTeacher();
        return new ResultBack<>(new PageInfo<>(list));
    }
	

    @RequestMapping(value = "/complete/{taskid}")
    @ResponseBody
    public ResultBack<Object> deptcomplete( @PathVariable("taskid") String taskid, String agree,HttpSession session) {
        String userid = (String) session.getAttribute("username");
        Map<String, Object> variables = new HashMap<>();
        variables.put("agree", agree);
        taskService.claim(taskid, userid);
        taskService.complete(taskid, variables);
        return new ResultBack<>("处理完成");
    }
    
    @RequestMapping(value = "/finish/{taskid}")
    @ResponseBody
    public ResultBack<Object> finish( @PathVariable("taskid") String taskid,HttpSession session) {
    	Map<String, Object> variables = new HashMap<>();
        variables.put("know", "true");
        taskService.complete(taskid, variables);
        return new ResultBack<>("处理完成");
    }
    
    @RequestMapping("pageListLeavesWithMainTeacher")
    @ResponseBody
    public ResultBack<Object> pageListLeavesWithMainTeacher(Integer pageNum, Integer pageSize,HttpSession session) {
        PageHelper.startPage(pageNum, pageSize);
        TUser user = (TUser) session.getAttribute("user");
        List<TLeave> list = leaveService.pageListLeavesWithMainTeacher();
        return new ResultBack<>(new PageInfo<>(list));
    }	

	@RequestMapping("rede")
	@ResponseBody
	public ResultBack<Deployment> rede() {

		RepositoryService repositoryService = ProcessEngines.getDefaultProcessEngine().getRepositoryService();
		DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
		// 设置部署名称并将两个流程定义文件添加到部署构建器中
		deploymentBuilder.name("测试部署资源").addClasspathResource("processes/leave.bpmn");

		// 由部署构建器生成一个部署对象，即将两个定义文件部署到数据库中
		Deployment deploy = deploymentBuilder.deploy();
		return new ResultBack<>(deploy);
	}
}
