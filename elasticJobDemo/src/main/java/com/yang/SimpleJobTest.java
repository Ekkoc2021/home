package com.yang;

import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.dataflow.DataflowJobConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.api.strategy.impl.AverageAllocationJobShardingStrategy;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;

public class SimpleJobTest {

    public static void main(String[] args) {
        // ZK注册中心
        CoordinatorRegistryCenter regCenter = new ZookeeperRegistryCenter(
                new ZookeeperConfiguration("192.168.169.223:2181", "ejob-standalone")
        );
        regCenter.init();

        // 数据源 , 事件执行持久化策略 : 可以不配
//        DruidDataSource dataSource =new DruidDataSource();
//        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
//        dataSource.setUrl("jdbc:mysql://192.168.169.223:3306/study?useUnicode=true&characterEncoding=utf-8");
//        dataSource.setUsername("root");
//        dataSource.setPassword("Asd456654.");
//        JobEventConfiguration jobEventConfig = new JobEventRdbConfiguration(dataSource);

//        simpleJob01(regCenter);
        DataFlowJob01(regCenter);
    }

    private static void simpleJob01(CoordinatorRegistryCenter regCenter) {
        // 定义作业核心配置==>修改cron要么清除zk数据，要么修改jobName
        JobCoreConfiguration coreConfig = JobCoreConfiguration
                .newBuilder("MySimpleJob1", "0/5 * * * * ?", 4)
                .shardingItemParameters("0=RDP, 1=CORE, 2=SIMS, 3=ECIF").failover(true).build();

        // 定义SIMPLE类型配置
        SimpleJobConfiguration simpleJobConfig = new SimpleJobConfiguration(
                coreConfig, MyJob.class.getCanonicalName());

        // 作业分片策略
        // 基于平均分配算法的分片策略
        String jobShardingStrategyClass = AverageAllocationJobShardingStrategy.class.getCanonicalName();

        // 定义Lite作业根配置
        LiteJobConfiguration simpleJobRootConfig = LiteJobConfiguration
                .newBuilder(simpleJobConfig)
                .jobShardingStrategyClass(jobShardingStrategyClass).build();
        // LiteJobConfiguration simpleJobRootConfig = LiteJobConfiguration.newBuilder(simpleJobConfig).overwrite(true).build();

        // 构建Job:多个job就重复创建jobScheduler进行配置
        new JobScheduler(regCenter, simpleJobRootConfig).init();
    }

    //
    private static void DataFlowJob01(CoordinatorRegistryCenter regCenter) {
        // 定义作业核心配置==>修改cron要么清除zk数据，要么修改jobName
        JobCoreConfiguration coreConfig = JobCoreConfiguration
                .newBuilder("MyDFJob4", "0/15 * * * * ?", 1)
                .shardingItemParameters("0=RDP, 1=CORE, 2=SIMS, 3=ECIF").failover(true).build();

        DataflowJobConfiguration dataflowJobConfiguration=new DataflowJobConfiguration(
                coreConfig,MyDataflowJob.class.getCanonicalName(),true
        );

        // 作业分片策略
        // 基于平均分配算法的分片策略
        String jobShardingStrategyClass = AverageAllocationJobShardingStrategy.class.getCanonicalName();

        LiteJobConfiguration dataflowJobRootConfig = LiteJobConfiguration
                .newBuilder(dataflowJobConfiguration)
                .jobShardingStrategyClass(jobShardingStrategyClass).build();

        new JobScheduler(regCenter, dataflowJobRootConfig).init();
    }

}