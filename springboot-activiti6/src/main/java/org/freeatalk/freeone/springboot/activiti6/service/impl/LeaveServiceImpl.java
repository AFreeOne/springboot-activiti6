package org.freeatalk.freeone.springboot.activiti6.service.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.lang3.StringUtils;
import org.freeatalk.freeone.springboot.activiti6.dao.TLeaveMapper;
import org.freeatalk.freeone.springboot.activiti6.entity.TLeave;
import org.freeatalk.freeone.springboot.activiti6.service.LeaveService;
import org.freeatalk.freeone.springboot.activiti6.utils.BaseKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

@Service
public class LeaveServiceImpl implements LeaveService {

	@Autowired
	private TLeaveMapper leaveMapper;
	
	@Autowired
	private IdentityService identityservice;
	
	@Autowired
	private RuntimeService runtimeservice;
	
	@Autowired
	private TaskService taskservice;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private RepositoryService repositoryService;
	
	
	@Transactional(rollbackFor = {Exception.class})
	@Override
	public boolean addLeave(String id, String type, Date startTime, Date endTime, String remark,String userid) {
		
		if(null == id || StringUtils.isBlank(id)) {
			id = BaseKit.uuid();
		}
		TLeave record = new TLeave().setId(id).setType(type).setStartTime(startTime).setEndTime(endTime).setRemark(remark).setUserid(userid).setStatus("review");
		boolean success = leaveMapper.insertSelective(record );
		if (!success) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		/*身份认证*/
		identityservice.setAuthenticatedUserId(userid);
		/*会从系统中获取id是leave的流程，注意，不是名字前缀是leave的流程，流程的id通过点击流程的空白处，在IDE，如eclipse的properties中显示并设置*/
		ProcessInstance processInstance = runtimeservice.startProcessInstanceByKey("leave", id);
		/*获取流程的主键，保存到假条的相关表中*/
		String processInstanceId = processInstance.getId();
		record.setProcessInstanceId(processInstanceId);
		return leaveMapper.updateByPrimaryKeySelective(record);
	}

	
	@Override
	public List<TLeave> listMyLeaves(String userid, Map<String, Object> variables) {
		return leaveMapper.listUserLeaves(userid, variables);
	}
	@Override
	public InputStream getDiagram(String processInstanceId) {
 	    //获得流程实例
 	    ProcessInstance processInstance = runtimeservice.createProcessInstanceQuery()
 	            .processInstanceId(processInstanceId).singleResult();
 	    String processDefinitionId = StringUtils.EMPTY;
 	    if (processInstance == null) {
 	        //查询已经结束的流程实例
 	        HistoricProcessInstance processInstanceHistory =
 	                historyService.createHistoricProcessInstanceQuery()
 	                        .processInstanceId(processInstanceId).singleResult();
 	        if (processInstanceHistory == null)
 	            return null;
 	        else
 	            processDefinitionId = processInstanceHistory.getProcessDefinitionId();
 	    } else {
 	        processDefinitionId = processInstance.getProcessDefinitionId();
 	    }

 	    //使用宋体
 	    String fontName = "宋体";
 	    //获取BPMN模型对象
 	    BpmnModel model = repositoryService.getBpmnModel(processDefinitionId);
 	    //获取流程实例当前的节点，需要高亮显示
 	    List<String> currentActs = Collections.EMPTY_LIST;
 	    if (processInstance != null)
 	        currentActs = runtimeservice.getActiveActivityIds(processInstance.getId());

 	   ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
 	    
 	    return processEngine.getProcessEngineConfiguration()
 	            .getProcessDiagramGenerator()
 	            .generateDiagram(model, "png", currentActs, new ArrayList<String>(),
 	                    fontName, fontName, fontName, null, 1.0);
 	}
}
