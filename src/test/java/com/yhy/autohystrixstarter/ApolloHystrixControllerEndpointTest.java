package com.yhy.autohystrixstarter;

import com.netflix.hystrix.strategy.HystrixPlugins;
import com.yhy.autohystrixstarter.actuate.ApolloHystrixControllerEndpoint;
import com.yhy.autohystrixstarter.core.ApolloHystrixConfPropertyStrategy;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApolloHystrixControllerEndpointTest {
  @Test
  public void test() throws Exception {
    ApolloHystrixControllerEndpoint endpoint = new ApolloHystrixControllerEndpoint();

    Map<String, String> configMap = new HashMap<>();
    configMap.put("hystrix.command." + "hello" + ".execution.isolation.strategy", "SEMAPHORE");
    configMap.put("hystrix.command." + "hello" + ".execution.isolation.thread.timeoutInMilliseconds", "99");
    configMap.put("hystrix.command." + "hello" + ".execution.isolation.semaphore.maxConcurrentRequests", "99");
    configMap.put("hystrix.command." + "hello" + ".fallback.isolation.semaphore.maxConcurrentRequests", "99");

    configMap.put("hystrix.command." + "default" + ".circuitBreaker.enabled", "false");
    configMap.put("hystrix.command." + "default" + ".circuitBreaker.requestVolumeThreshold", "99");
    configMap.put("hystrix.command." + "default" + ".circuitBreaker.sleepWindowInMilliseconds", "99");
//    configMap.put("hystrix.command." + "default" + ".circuitBreaker.errorThresholdPercentage", "99");
    configMap.put("hystrix.command." + "default" + ".circuitBreaker.forceOpen", "false");
    configMap.put("hystrix.command." + "default" + ".circuitBreaker.forceClosed", "false");


    configMap.put("hystrix.threadpool.default.coreSize", "33");
    configMap.put("hystrix.threadpool.default.maximumSize", "33");
    configMap.put("hystrix.threadpool.default.maxQueueSize", "33");
    configMap.put("hystrix.threadpool.hello.queueSizeRejectionThreshold", "44");
    configMap.put("hystrix.threadpool.hello.keepAliveTimeMinutes", "44");
    configMap.put("hystrix.threadpool.hello.metrics.rollingStats.timeInMilliseconds", "44");
    //default 10
//    configMap.put("hystrix.threadpool.default.metrics.rollingStats.numBuckets","33");
    configMap.put("hystrix.threadpool.hello.coreSize", "22");

    MockApolloConfig config = new MockApolloConfig(configMap);


    ApolloHystrixConfPropertyStrategy strategy = new ApolloHystrixConfPropertyStrategy(config);

    HystrixPlugins.getInstance().registerPropertiesStrategy(strategy);

    Map<String, Object> commandDefaultMap = endpoint.hystrixCommandConfig("default");
    assertEquals(false, commandDefaultMap.get("circuitBreakerEnabled"));
    assertEquals(50, (int) commandDefaultMap.get("circuitBreakerErrorThresholdPercentage"));
    assertEquals(99, (int) commandDefaultMap.get("circuitBreakerRequestVolumeThreshold"));

    Map<String, Object> commandKeyMap = endpoint.hystrixCommandConfig("hello");
    assertEquals(99, (int) commandKeyMap.get("executionTimeoutInMilliseconds"));
    assertEquals(99, (int) commandKeyMap.get("circuitBreakerSleepWindowInMilliseconds"));
    assertEquals(50, (int) commandKeyMap.get("circuitBreakerErrorThresholdPercentage"));

    Map<String, Object> threadDefaultMap = endpoint.hystrixThreadPoolConfig("default");
    assertEquals(33, (int) threadDefaultMap.get("coreSize"));
    assertEquals(10, (int) threadDefaultMap.get("metricsRollingStatisticalWindowBuckets"));

    Map<String, Object> threadKeyMap = endpoint.hystrixThreadPoolConfig("hello");
    assertEquals(22, (int) threadKeyMap.get("coreSize"));
    assertEquals(10, (int) threadKeyMap.get("metricsRollingStatisticalWindowBuckets"));
    assertEquals(33, (int) threadKeyMap.get("maximumSize"));


  }
}
