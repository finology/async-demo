package gy.finolo.asyncdemo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @description: 实现多线程和并发编程
 * @author: Simon
 * @date: 2020-06-11 17:37
 */
@Configuration
// 利用@EnableAsync注解开启异步任务支持
@EnableAsync
public class TaskExecutorConfig implements AsyncConfigurer {

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(10);
        taskExecutor.setMaxPoolSize(20);
        taskExecutor.setQueueCapacity(500);
        taskExecutor.initialize();
        return taskExecutor;
    }
}
