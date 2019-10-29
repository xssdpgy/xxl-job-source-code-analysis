package com.xxl.job.admin.core.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 对应XXL_JOB_QRTZ_TRIGGER_GROUP表
 * 该表记录的是执行器信息，后台管理系统对应【执行器管理】部分
 * Created by xuxueli on 16/9/30.
 */
public class XxlJobGroup {

    // 主键ID
    private int id;
    // 执行器AppName
    private String appName;
    // 执行器名称
    private String title;
    // 排序
    private int order;
    // 执行器地址类型：0=自动注册、1=手动录入
    private int addressType;
    // 执行器地址列表，多地址逗号分隔(手动录入)
    private String addressList;

    // registry list
    private List<String> registryList;  // 执行器地址列表(系统注册)
    public List<String> getRegistryList() {
        if (addressList!=null && addressList.trim().length()>0) {
            // addressList中有值（String类型），则将addressList以,分隔为List<String> 设置进registryList
            registryList = new ArrayList<String>(Arrays.asList(addressList.split(",")));
        }
        return registryList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getAddressType() {
        return addressType;
    }

    public void setAddressType(int addressType) {
        this.addressType = addressType;
    }

    public String getAddressList() {
        return addressList;
    }

    public void setAddressList(String addressList) {
        this.addressList = addressList;
    }

}
