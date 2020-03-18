package org.freeatalk.freeone.springboot.activiti6.entity;

import java.io.Serializable;
import java.util.Date;

import org.activiti.engine.task.Task;

import com.fasterxml.jackson.annotation.JsonFormat;

public class TLeave implements Serializable,Cloneable{

	private static final long serialVersionUID = 1L;

	private String id;

    private String type;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date startTime;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date endTime;

    private String status;

    private String remark;

    private String failedReason;
    
    private String userid;
    
    private String processInstanceId;
    
    private String processDefinitionId;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date taskcreatetime;
    
    private String taskId;
    
    private String taskName;
    
    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public Date getTaskcreatetime() {
        return taskcreatetime;
    }

    public void setTaskcreatetime(Date taskcreatetime) {
        this.taskcreatetime = taskcreatetime;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getId() {
        return id;
    }

    public TLeave setId(String id) {
        this.id = id == null ? null : id.trim();
        return this;
    }

    public String getType() {
        return type;
    }

    public TLeave setType(String type) {
        this.type = type == null ? null : type.trim();
        return this;
    }

    public Date getStartTime() {
        return startTime;
    }

    public TLeave setStartTime(Date startTime) {
        this.startTime = startTime;
        return this;
    }

    public Date getEndTime() {
        return endTime;
    }

    public TLeave setEndTime(Date endTime) {
        this.endTime = endTime;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public TLeave setStatus(String status) {
        this.status = status == null ? null : status.trim();
        return this;
    }

    public String getRemark() {
        return remark;
    }

    public TLeave setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
        return this;
    }

    public String getFailedReason() {
        return failedReason;
    }

    public TLeave setFailedReason(String failedReason) {
        this.failedReason = failedReason == null ? null : failedReason.trim();
        return this;
    }

	public String getUserid() {
		return userid;
	}

	public TLeave setUserid(String userid) {
		this.userid = userid;
		return this;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
	
	
	
	
}