package com.xxl.job.admin.core.thread;

import com.xxl.job.admin.core.conf.XxlJobAdminConfig;
import com.xxl.job.admin.core.model.XxlJobGroup;
import com.xxl.job.admin.core.model.XxlJobRegistry;
import com.xxl.job.core.enums.RegistryConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 剔除90s内没有进行健康检查的执行器信息
 * job registry instance
 * @author xuxueli 2016-10-02 19:10:24
 */
public class JobRegistryMonitorHelper {
	private static Logger logger = LoggerFactory.getLogger(JobRegistryMonitorHelper.class);

	private static JobRegistryMonitorHelper instance = new JobRegistryMonitorHelper();
	public static JobRegistryMonitorHelper getInstance(){
		return instance;
	}

	private Thread registryThread;
	//toStop是用volatile修饰的
	private volatile boolean toStop = false;
	public void start(){
		//启动一个线程
		registryThread = new Thread(new Runnable() {
			@Override
			public void run() {
				//当toStop为false时进入该循环  toStop为原子变量
				while (!toStop) {
					try {
						// auto registry group
						//获取类型为自动注册的执行器（XxlJobGroup）地址列表
						List<XxlJobGroup> groupList = XxlJobAdminConfig.getAdminConfig().getXxlJobGroupDao().findByAddressType(0);
						if (groupList!=null && !groupList.isEmpty()) {

							// remove dead address (admin/executor)
							//如果90秒没有心跳信息返回代表机器已经出现问题，移除这些注册机器信息
							XxlJobAdminConfig.getAdminConfig().getXxlJobRegistryDao().removeDead(RegistryConfig.DEAD_TIMEOUT);

							// fresh online address (admin/executor)
							HashMap<String, List<String>> appAddressMap = new HashMap<String, List<String>>();
							//查询90秒内有更新的注册机器信息列表
							List<XxlJobRegistry> list = XxlJobAdminConfig.getAdminConfig().getXxlJobRegistryDao().findAll(RegistryConfig.DEAD_TIMEOUT);
							if (list != null) {
								for (XxlJobRegistry item: list) {
									//遍历注册信息列表，得到自动注册类型的执行器与其对应的地址信息关系Map
									if (RegistryConfig.RegistType.EXECUTOR.name().equals(item.getRegistryGroup())) {
										String appName = item.getRegistryKey();
										List<String> registryList = appAddressMap.get(appName);
										if (registryList == null) {
											registryList = new ArrayList<String>();
										}

										if (!registryList.contains(item.getRegistryValue())) {
											registryList.add(item.getRegistryValue());
										}
										//收集执行器信息，根据执行器appName做区分：appAddressMap的key为appName，value为此执行器的注册地址列表（集群环境下会有多个注册地址）
										appAddressMap.put(appName, registryList);
									}
								}
							}

							// fresh group address
							//遍历所有的自动注册的执行器
							for (XxlJobGroup group: groupList) {
								//通过执行器的appName从刚刚区分的Map中拿到该执行器下的集群机器注册地址
								List<String> registryList = appAddressMap.get(group.getAppName());
								String addressListStr = null;
								if (registryList!=null && !registryList.isEmpty()) {
									Collections.sort(registryList);
									addressListStr = "";
									//集群的多个注册地址通过逗号拼接转为字符串
									for (String item:registryList) {
										addressListStr += item + ",";
									}
									addressListStr = addressListStr.substring(0, addressListStr.length()-1);
								}
								//集群的多个注册地址通过逗号拼接转成的字符串设置进执行器的addressList属性中，并更新执行器信息，保存到数据库
								group.setAddressList(addressListStr);
								XxlJobAdminConfig.getAdminConfig().getXxlJobGroupDao().update(group);
							}
						}
					} catch (Exception e) {
						if (!toStop) {
							logger.error(">>>>>>>>>>> xxl-job, job registry monitor thread error:{}", e);
						}
					}
					try {
						//线程停顿30秒
						TimeUnit.SECONDS.sleep(RegistryConfig.BEAT_TIMEOUT);
					} catch (InterruptedException e) {
						if (!toStop) {
							logger.error(">>>>>>>>>>> xxl-job, job registry monitor thread error:{}", e);
						}
					}
				}
				logger.info(">>>>>>>>>>> xxl-job, job registry monitor thread stop");
			}
		});
		//将该线程设置成守护线程
		registryThread.setDaemon(true);
		registryThread.setName("xxl-job, admin JobRegistryMonitorHelper");
		//执行该线程
		registryThread.start();
	}

	public void toStop(){
		toStop = true;
		// interrupt and wait
		registryThread.interrupt();
		try {
			registryThread.join();
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		}
	}

}
