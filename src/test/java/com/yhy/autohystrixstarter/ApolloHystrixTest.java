package com.yhy.autohystrixstarter;


import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.yhy.autohystrixstarter.core.ApolloHystrixConfPropertyStrategy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;


public class ApolloHystrixTest {
  @Test
  @DisplayName("mock的config应该与apollo config的共一致")
  public void MockApolloConfig() {
    Map<String, String> configMap = new HashMap<>();
    String key = "hystrix.command.default.circuitBreaker.requestVolumeThreshold";
    configMap.put(key, "30");


    MockApolloConfig config = new MockApolloConfig(configMap);
    int val1 = config.getIntProperty(key, 20);
    int val2 = config.getIntProperty(key + "a", 20);
    //有值的时候能取到值
    assertEquals(30, val1);
    //没值的时候取默认值
    assertEquals(20, val2);


    configMap.put(key, "99");
    config.refreshConfig();

    int val3 = config.getIntProperty(key, 20);
    //刷新后拿到新值
    assertEquals(99, val3);
  }


  @Test
  public void testApolloHystrix() {

    Map<String, String> configMap = new HashMap<>();
    configMap.put("hystrix.command." + "hello" + ".execution.isolation.strategy", "THREAD");
    configMap.put("hystrix.command." + "hello" + ".execution.isolation.thread.timeoutInMilliseconds", "1000");
    configMap.put("hystrix.command." + "hello" + ".execution.isolation.semaphore.maxConcurrentRequests", "10");
    configMap.put("hystrix.command." + "hello" + ".fallback.isolation.semaphore.maxConcurrentRequests", "10");
    configMap.put("hystrix.command." + "hello" + ".circuitBreaker.enabled", "true");
    configMap.put("hystrix.command." + "hello" + ".circuitBreaker.requestVolumeThreshold", "20");
    configMap.put("hystrix.command." + "hello" + ".circuitBreaker.sleepWindowInMilliseconds", "5000");
    configMap.put("hystrix.command." + "hello" + ".circuitBreaker.errorThresholdPercentage", "50");
    configMap.put("hystrix.command." + "default" + ".circuitBreaker.forceOpen", "true");
    configMap.put("hystrix.command." + "hello" + ".circuitBreaker.forceClosed", "true");
    configMap.put("hystrix.command." + "default" + ".circuitBreaker.forceClosed", "false");
    MockApolloConfig config = new MockApolloConfig(configMap);

    HystrixCommandProperties.Setter setter = mock(HystrixCommandProperties.Setter.class);

    HystrixCommandKey commandKey = HystrixCommandKey.Factory.asKey("hello");

    ApolloHystrixConfPropertyStrategy strategy = new ApolloHystrixConfPropertyStrategy(config);
    HystrixCommandProperties properties = strategy.getCommandProperties(commandKey, setter);


    //1.能正常取到comanndKey的值
    assertEquals(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD, properties.executionIsolationStrategy().get());
    assertEquals(Integer.valueOf(10), properties.executionIsolationSemaphoreMaxConcurrentRequests().get());
    assertEquals(Integer.valueOf(1000), properties.executionIsolationThreadTimeoutInMilliseconds().get());
    assertEquals(Integer.valueOf(10), properties.fallbackIsolationSemaphoreMaxConcurrentRequests().get());
    assertEquals(true, properties.circuitBreakerEnabled().get());
    assertEquals(Integer.valueOf(50), properties.circuitBreakerErrorThresholdPercentage().get());

    assertEquals(Integer.valueOf(20), properties.circuitBreakerRequestVolumeThreshold().get());
    assertEquals(Integer.valueOf(5000), properties.circuitBreakerSleepWindowInMilliseconds().get());
    //2.没有commandKey取default值
    assertEquals(true, properties.circuitBreakerForceOpen().get());
    //3.commandKey和default值都存在时取commandKey值
    assertEquals(true, properties.circuitBreakerForceClosed().get());
    //3.刷新的时候properties值也刷新
    configMap.put("hystrix.command." + "hello" + ".execution.isolation.strategy", "SEMAPHORE");
    configMap.put("hystrix.command." + "hello" + ".execution.isolation.thread.timeoutInMilliseconds", "99");
    configMap.put("hystrix.command." + "hello" + ".execution.isolation.semaphore.maxConcurrentRequests", "99");
    configMap.put("hystrix.command." + "hello" + ".fallback.isolation.semaphore.maxConcurrentRequests", "99");
    configMap.put("hystrix.command." + "hello" + ".circuitBreaker.enabled", "false");
    configMap.put("hystrix.command." + "hello" + ".circuitBreaker.requestVolumeThreshold", "99");
    configMap.put("hystrix.command." + "hello" + ".circuitBreaker.sleepWindowInMilliseconds", "99");
    configMap.put("hystrix.command." + "hello" + ".circuitBreaker.errorThresholdPercentage", "99");
    configMap.put("hystrix.command." + "hello" + ".circuitBreaker.forceOpen", "false");
    configMap.put("hystrix.command." + "hello" + ".circuitBreaker.forceClosed", "false");
    config.refreshConfig();

    assertEquals(HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE, properties.executionIsolationStrategy().get());
    assertEquals(Integer.valueOf(99), properties.executionIsolationSemaphoreMaxConcurrentRequests().get());
    assertEquals(Integer.valueOf(99), properties.executionIsolationThreadTimeoutInMilliseconds().get());
    assertEquals(Integer.valueOf(99), properties.fallbackIsolationSemaphoreMaxConcurrentRequests().get());
    assertEquals(false, properties.circuitBreakerEnabled().get());
    assertEquals(Integer.valueOf(99), properties.circuitBreakerErrorThresholdPercentage().get());
    assertEquals(Integer.valueOf(99), properties.circuitBreakerRequestVolumeThreshold().get());
    assertEquals(Integer.valueOf(99), properties.circuitBreakerSleepWindowInMilliseconds().get());
    assertEquals(false, properties.circuitBreakerForceOpen().get());
    assertEquals(false, properties.circuitBreakerForceClosed().get());


  }

  @Test
  public void testApolloThreadPool() {
    Map<String, String> configMap = new HashMap<>();
    configMap.put("hystrix.threadpool.default.coreSize", "33");
    configMap.put("hystrix.threadpool.default.maximumSize", "33");
    configMap.put("hystrix.threadpool.default.maxQueueSize", "33");
    configMap.put("hystrix.threadpool.default.queueSizeRejectionThreshold", "33");
    configMap.put("hystrix.threadpool.default.keepAliveTimeMinutes", "33");
    configMap.put("hystrix.threadpool.default.allowMaximumSizeToDivergeFromCoreSize", "false");
    configMap.put("hystrix.threadpool.default.metrics.rollingStats.timeInMilliseconds", "33");
    //default 10
//    configMap.put("hystrix.threadpool.default.metrics.rollingStats.numBuckets","33");
    configMap.put("hystrix.threadpool.hello.coreSize", "22");

    MockApolloConfig config = new MockApolloConfig(configMap);
    HystrixThreadPoolProperties.Setter setter = mock(HystrixThreadPoolProperties.Setter.class);
    ApolloHystrixConfPropertyStrategy strategy = new ApolloHystrixConfPropertyStrategy(config);
    HystrixThreadPoolKey key = HystrixThreadPoolKey.Factory.asKey("hello");
    HystrixThreadPoolProperties poolProperties = strategy.getThreadPoolProperties(key, setter);
    //有key值时取key值
    assertEquals(Integer.valueOf(22), poolProperties.coreSize().get());
    //没key时取default值
    assertEquals(Integer.valueOf(22), poolProperties.actualMaximumSize());
    assertEquals(Integer.valueOf(33), poolProperties.maxQueueSize().get());
    //当这个值设置成false时actualMaximumSize值为coreSize,为true时actualMaximumSize值为两者最大值
    assertEquals(false, poolProperties.getAllowMaximumSizeToDivergeFromCoreSize().get());
    assertEquals(Integer.valueOf(22), poolProperties.actualMaximumSize());
    assertEquals(Integer.valueOf(33), poolProperties.queueSizeRejectionThreshold().get());
    assertEquals(Integer.valueOf(33), poolProperties.keepAliveTimeMinutes().get());

    assertEquals(Integer.valueOf(33), poolProperties.metricsRollingStatisticalWindowInMilliseconds().get());
    //都没有时取默认值
    assertEquals(Integer.valueOf(10), poolProperties.metricsRollingStatisticalWindowBuckets().get());


  }
}
