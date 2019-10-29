package com.xxl.job.admin.core.model;

import java.util.Date;

/**
 * 对应 XXL_JOB_QRTZ_TRIGGER_REGISTRY 表
 * 该表记录的是执行器进行自动注册时的信息
 *
 * Created by xuxueli on 16/9/30.
 */
public class XxlJobRegistry {

    // 主键id
    private int id;
    // 注册类型（EXECUTOR：自动注册 ADMIN：手动录入）
    private String registryGroup;
    // 执行器标识（即执行器项目定义的appName）
    private String registryKey;
    // 执行器地址（即执行器项目的地址信息）
    private String registryValue;
    // 更新时间
    private Date updateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRegistryGroup() {
        return registryGroup;
    }

    public void setRegistryGroup(String registryGroup) {
        this.registryGroup = registryGroup;
    }

    public String getRegistryKey() {
        return registryKey;
    }

    public void setRegistryKey(String registryKey) {
        this.registryKey = registryKey;
    }

    public String getRegistryValue() {
        return registryValue;
    }

    public void setRegistryValue(String registryValue) {
        this.registryValue = registryValue;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
