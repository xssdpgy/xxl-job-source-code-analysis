package com.xxl.job.admin.core.model;

import java.util.Date;

/**
 * xxl-job log, used to track trigger process
 * 对应 XXL_JOB_QRTZ_TRIGGER_LOG 表
 * 该表记录的是调度日志，后台管理系统对应【调度日志】部分
 *
 * @author xuxueli  2015-12-19 23:19:09
 */
public class XxlJobLog {
	
	private int id;
	
	// job info
	private int jobGroup; //执行器主键ID
	private int jobId; //任务，主键ID

	// execute info
	private String executorAddress; //执行器地址，本次执行的地址
	private String executorHandler; //执行器任务handler
	private String executorParam; //执行器任务参数
	private String executorShardingParam; //执行器任务分片参数，格式如 1/2
	private int executorFailRetryCount; //失败重试次数
	
	// trigger info
	private Date triggerTime; //调度-时间
	private int triggerCode; //调度-结果
	private String triggerMsg; //调度-日志
	
	// handle info
	private Date handleTime; //执行-时间
	private int handleCode; //执行-状态
	private String handleMsg; //执行-日志

	// alarm info
	private int alarmStatus; //告警状态 0-默认、-1=锁定状态、1-无需告警、2-告警成功、3-告警失败

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getJobGroup() {
		return jobGroup;
	}

	public void setJobGroup(int jobGroup) {
		this.jobGroup = jobGroup;
	}

	public int getJobId() {
		return jobId;
	}

	public void setJobId(int jobId) {
		this.jobId = jobId;
	}

	public String getExecutorAddress() {
		return executorAddress;
	}

	public void setExecutorAddress(String executorAddress) {
		this.executorAddress = executorAddress;
	}

	public String getExecutorHandler() {
		return executorHandler;
	}

	public void setExecutorHandler(String executorHandler) {
		this.executorHandler = executorHandler;
	}

	public String getExecutorParam() {
		return executorParam;
	}

	public void setExecutorParam(String executorParam) {
		this.executorParam = executorParam;
	}

	public String getExecutorShardingParam() {
		return executorShardingParam;
	}

	public void setExecutorShardingParam(String executorShardingParam) {
		this.executorShardingParam = executorShardingParam;
	}

	public int getExecutorFailRetryCount() {
		return executorFailRetryCount;
	}

	public void setExecutorFailRetryCount(int executorFailRetryCount) {
		this.executorFailRetryCount = executorFailRetryCount;
	}

	public Date getTriggerTime() {
		return triggerTime;
	}

	public void setTriggerTime(Date triggerTime) {
		this.triggerTime = triggerTime;
	}

	public int getTriggerCode() {
		return triggerCode;
	}

	public void setTriggerCode(int triggerCode) {
		this.triggerCode = triggerCode;
	}

	public String getTriggerMsg() {
		return triggerMsg;
	}

	public void setTriggerMsg(String triggerMsg) {
		this.triggerMsg = triggerMsg;
	}

	public Date getHandleTime() {
		return handleTime;
	}

	public void setHandleTime(Date handleTime) {
		this.handleTime = handleTime;
	}

	public int getHandleCode() {
		return handleCode;
	}

	public void setHandleCode(int handleCode) {
		this.handleCode = handleCode;
	}

	public String getHandleMsg() {
		return handleMsg;
	}

	public void setHandleMsg(String handleMsg) {
		this.handleMsg = handleMsg;
	}

	public int getAlarmStatus() {
		return alarmStatus;
	}

	public void setAlarmStatus(int alarmStatus) {
		this.alarmStatus = alarmStatus;
	}

}
