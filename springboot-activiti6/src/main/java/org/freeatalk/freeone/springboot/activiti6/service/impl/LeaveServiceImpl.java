package org.freeatalk.freeone.springboot.activiti6.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
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
	IdentityService identityservice;
	
	@Autowired
	RuntimeService runtimeservice;
	
	@Autowired
	TaskService taskservice;
	
	@Transactional(rollbackFor = {Exception.class})
	@Override
	public boolean addLeave(String id, String type, Date startTime, Date endTime, String remark,String userid) {
		
		if(null == id || StringUtils.isBlank(id)) {
			id = BaseKit.uuid();
		}
		TLeave record = new TLeave().setId(id).setType(type).setStartTime(startTime).setEndTime(endTime).setRemark(remark);
		boolean success = leaveMapper.insertSelective(record );
		if (!success) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		identityservice.setAuthenticatedUserId(userid);
		ProcessInstance processInstance = runtimeservice.startProcessInstanceByKey("leave", id);
		String processInstanceId = processInstance.getId();
		record.setProcessInstanceId(processInstanceId);
		return leaveMapper.updateByPrimaryKeySelective(record);
	}

	
	@Override
	public List<TLeave> listMyLeaves(String userid, Map<String, Object> variables) {
		return leaveMapper.listUserLeaves(userid, variables);
	}
}
