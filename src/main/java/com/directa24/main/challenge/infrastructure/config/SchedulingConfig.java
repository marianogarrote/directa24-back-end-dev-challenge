package com.directa24.main.challenge.infrastructure.config;

import com.directa24.main.challenge.domain.repository.DirectorStatisticsRepository;
import com.directa24.main.challenge.domain.service.MovieStatusTracker;
import com.directa24.main.challenge.infrastructure.storage.inmemory.InMemoryDirectoryStatisticsRepository;
import com.directa24.main.challenge.infrastructure.scheduling.InMemoryMovieStatusTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
@EnableScheduling
public class SchedulingConfig implements AsyncConfigurer {

    private final Logger log = LoggerFactory.getLogger(SchedulingConfig.class);

    @Value("${scheduler.await-termination-in-seconds:60}")
    private int awaitTerminationInSeconds;

    private final TaskExecutionProperties taskExecutionProperties;

    public SchedulingConfig(TaskExecutionProperties taskExecutionProperties) {
        this.taskExecutionProperties = taskExecutionProperties;
    }

    @Bean
    public DirectorStatisticsRepository directorStatisticsRepository() {
        return new InMemoryDirectoryStatisticsRepository();
    }

    @Bean
    public MovieStatusTracker movieTrackingStatus() {
        return new InMemoryMovieStatusTracker();
    }

    @Override
    @Bean(name = "threadPoolTaskExecutor")
    public Executor getAsyncExecutor() {
        log.debug("Creating Async Task Executor");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(taskExecutionProperties.getPool().getCoreSize());
        executor.setMaxPoolSize(taskExecutionProperties.getPool().getMaxSize());
        executor.setQueueCapacity(taskExecutionProperties.getPool().getQueueCapacity());
        executor.setThreadNamePrefix(taskExecutionProperties.getThreadNamePrefix());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(awaitTerminationInSeconds);
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SimpleAsyncUncaughtExceptionHandler();
    }
}
