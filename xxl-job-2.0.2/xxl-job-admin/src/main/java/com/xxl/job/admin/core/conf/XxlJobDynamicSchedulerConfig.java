package com.xxl.job.admin.core.conf;

import com.xxl.job.admin.core.schedule.XxlJobDynamicScheduler;
import org.quartz.Scheduler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;

/**
 * xxl-job的JavaConfig配置，底层基于对quartz的二次开发
 * 创建了2个Bean：SchedulerFactoryBean和XxlJobDynamicScheduler
 * @author xuxueli 2018-10-28 00:18:17
 */
@Configuration
public class XxlJobDynamicSchedulerConfig {

    /**
     * 容器初始化org.springframework.scheduling.quartz.SchedulerFactoryBean
     *   --  底层基于Quartz  --
     * @param dataSource
     * @return
     */
    @Bean
    public SchedulerFactoryBean getSchedulerFactoryBean(DataSource dataSource){

        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
        schedulerFactory.setDataSource(dataSource);
        schedulerFactory.setAutoStartup(true);                  // 自动启动
        schedulerFactory.setStartupDelay(20);                   // 延时启动，应用启动成功后在启动
        schedulerFactory.setOverwriteExistingJobs(true);        // 覆盖DB中JOB：true、以数据库中已经存在的为准：false
        schedulerFactory.setApplicationContextSchedulerContextKey("applicationContext");
        schedulerFactory.setConfigLocation(new ClassPathResource("quartz.properties"));

        return schedulerFactory;
    }

    /**
     * 容器初始化XxlJobDynamicScheduler时会调用start方法，销毁Bean时会调用destroy方法
     * 具体需跟踪 com.xxl.job.admin.core.schedule.XxlJobDynamicScheduler#start()  #destory()
     * @param schedulerFactory
     * @return
     */
    @Bean(initMethod = "start", destroyMethod = "destroy")
    public XxlJobDynamicScheduler getXxlJobDynamicScheduler(SchedulerFactoryBean schedulerFactory){

        Scheduler scheduler = schedulerFactory.getScheduler();

        //创建xxl job调度中心，在容器初始化bean后调用start方法
        XxlJobDynamicScheduler xxlJobDynamicScheduler = new XxlJobDynamicScheduler();
        /**
         * xxl-job的动态调度中心底层使用quartz的调度中心，通过RemoteHttpJobBean这个类
           进行任务的触发（XxlJobDynamicScheduler类中的addJob方法）
         */
        xxlJobDynamicScheduler.setScheduler(scheduler);

        return xxlJobDynamicScheduler;
    }

}
