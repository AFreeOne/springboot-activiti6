package org.freeatalk.freeone.springboot.activiti6.controller;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.freeatalk.freeone.springboot.activiti6.service.LeaveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags = "流程相关")
@Controller
@RequestMapping("myActiviti")
public class MyActivitiController {
	
	
	private static final Logger log = LoggerFactory.getLogger(MyActivitiController.class);


	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private FormService formService;
	@Autowired
	private IdentityService identityService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private HistoryService histiryService;
	@Autowired
	private LeaveService leaveService;
	/**
	 * 追踪流程
	 * @param processInstanceId 流程的实例ID
	 */
	@ApiOperation(value = "通过流程的id查询流程，并通过图片的形式显示")
	@ApiParam(name="processInstanceId",value="流程的实例ID",required = true)
	@RequestMapping(value="traceProcess/{processInstanceId}",method = RequestMethod.GET)
	public void traceProcess(@PathVariable(name = "processInstanceId",required = true) String processInstanceId,HttpServletResponse response ) {
		 try {
	            InputStream is = leaveService.getDiagram(processInstanceId);
	            if (is == null)
	                return;

	            response.setContentType("image/png");

	            BufferedImage image = ImageIO.read(is);
	            OutputStream out = response.getOutputStream();
	            ImageIO.write(image, "png", out);

	            is.close();
	            out.close();
	        } catch (Exception ex) {
	            log.error("查看流程图失败", ex);
	        }
	}
	
	 
}
