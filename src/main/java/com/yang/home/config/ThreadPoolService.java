package com.yang.home.config;

import com.yang.home.utils.LogReqIdUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.*;

@Configuration
public class ThreadPoolService {
	@Bean("TestPool")
	public ThreadPoolExecutor threadPoolExecutor() {
		ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor
				(4, // 核心线程池数
						8, // 最大线程池数
						10, // 空闲线程存活时间,超出核心线程数后，多出来的空闲线程会超时终止
						TimeUnit.SECONDS, // 时间单位
						new LinkedBlockingQueue<>(5536), // 任务队列
						Executors.defaultThreadFactory(), // 创建线程的工厂
						new ThreadPoolExecutor.CallerRunsPolicy()); // 拒接策略

		// AbortPolicy：直接抛出 RejectedExecutionException 异常（默认策略)
		// CallerRunsPolicy：由调用线程处理该任务,谁提交谁处理
		// DiscardPolicy：直接丢弃任务
		// DiscardOldestPolicy：丢弃队列中最老的任务，然后重新提交当前任务

		return threadPoolExecutor;
	}

	@Bean("TestPool2")
	public ThreadPoolTaskExecutor threadPoolTaskExecutor() {


		ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
		threadPoolTaskExecutor.setCorePoolSize(4);
		threadPoolTaskExecutor.setMaxPoolSize(8);
		threadPoolTaskExecutor.setQueueCapacity(5536);
		threadPoolTaskExecutor.setKeepAliveSeconds(5);
		threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

		threadPoolTaskExecutor.setTaskDecorator(new LogReqIdUtil.MdcTaskDecorator()); // 设置装饰器

		threadPoolTaskExecutor.setThreadNamePrefix("spring-pool-");
		threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true); // 关闭程序时等待等待线程池中的任务执行完成再关闭
		threadPoolTaskExecutor.initialize();


		return threadPoolTaskExecutor;
	}
}
