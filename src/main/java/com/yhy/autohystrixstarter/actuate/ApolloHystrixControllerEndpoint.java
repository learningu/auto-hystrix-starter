package com.yhy.autohystrixstarter.actuate;


import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.netflix.hystrix.strategy.HystrixPlugins;
import org.springframework.boot.actuate.endpoint.web.annotation.RestControllerEndpoint;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.Constructor;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerEndpoint(id = "hystrix")
public class ApolloHystrixControllerEndpoint {


  public ApolloHystrixControllerEndpoint() {

  }

  @GetMapping("command")

  public Map hystrixCommandConfig(@RequestParam(defaultValue = "default") String key) throws Exception {
    Map<String, Object> configMap = new LinkedHashMap<>();
    Class c = HystrixCommandProperties.Setter.class;
    //反射创建setter对象
    Constructor constructor = c.getDeclaredConstructor();
    constructor.setAccessible(true);
    HystrixCommandProperties.Setter setter = (HystrixCommandProperties.Setter) constructor.newInstance();
    HystrixCommandProperties properties = HystrixPlugins.getInstance().getPropertiesStrategy().getCommandProperties(HystrixCommandKey.Factory.asKey(key), setter);
    //断路器相关
    configMap.put("circuitBreakerEnabled", properties.circuitBreakerEnabled().get());
    configMap.put("circuitBreakerErrorThresholdPercentage", properties.circuitBreakerErrorThresholdPercentage().get());
    configMap.put("circuitBreakerForceClosed", properties.circuitBreakerForceClosed().get());
    configMap.put("circuitBreakerRequestVolumeThreshold", properties.circuitBreakerRequestVolumeThreshold().get());
    configMap.put("circuitBreakerSleepWindowInMilliseconds", properties.circuitBreakerSleepWindowInMilliseconds().get());
    //策略相关
    configMap.put("executionIsolationStrategy", properties.executionIsolationStrategy().get());
    configMap.put("executionIsolationSemaphoreMaxConcurrentRequests", properties.executionIsolationSemaphoreMaxConcurrentRequests().get());
    configMap.put("executionIsolationThreadInterruptOnFutureCancel", properties.executionIsolationThreadInterruptOnFutureCancel().get());
    configMap.put("executionIsolationThreadInterruptOnTimeout", properties.executionIsolationThreadInterruptOnTimeout().get());
    configMap.put("executionIsolationThreadPoolKeyOverride", properties.executionIsolationThreadPoolKeyOverride().get());
    configMap.put("executionTimeoutEnabled", properties.executionTimeoutEnabled().get());
    configMap.put("executionTimeoutInMilliseconds", properties.executionTimeoutInMilliseconds().get());
    configMap.put("executionIsolationThreadTimeoutInMilliseconds", properties.executionIsolationThreadTimeoutInMilliseconds().get());
    //fallback相关
    configMap.put("fallbackEnabled", properties.fallbackEnabled().get());
    configMap.put("fallbackIsolationSemaphoreMaxConcurrentRequests", properties.fallbackIsolationSemaphoreMaxConcurrentRequests().get());
    //metric相关
    configMap.put("metricsHealthSnapshotIntervalInMilliseconds", properties.metricsHealthSnapshotIntervalInMilliseconds().get());
    configMap.put("metricsRollingStatisticalWindowBuckets", properties.metricsRollingStatisticalWindowBuckets().get());
    configMap.put("metricsRollingStatisticalWindowInMilliseconds", properties.metricsRollingStatisticalWindowInMilliseconds().get());
    configMap.put("metricsRollingPercentileBucketSize", properties.metricsRollingPercentileBucketSize().get());
    configMap.put("metricsRollingPercentileEnabled", properties.metricsRollingPercentileEnabled().get());
    configMap.put("metricsRollingPercentileWindowBuckets", properties.metricsRollingPercentileWindowBuckets().get());
    configMap.put("metricsRollingPercentileWindow", properties.metricsRollingPercentileWindow().get());
    configMap.put("metricsRollingPercentileWindowInMilliseconds", properties.metricsRollingPercentileWindowInMilliseconds().get());
    //request相关
    configMap.put("requestCacheEnabled", properties.requestCacheEnabled().get());
    configMap.put("requestLogEnabled", properties.requestLogEnabled().get());

    return configMap;
  }

  @GetMapping("threadpool")
  public Map hystrixThreadPoolConfig(@RequestParam(defaultValue = "default") String key) throws Exception {
    Map<String, Object> configMap = new LinkedHashMap<>();
    Class c = HystrixThreadPoolProperties.Setter.class;
    //反射创建setter对象
    Constructor constructor = c.getDeclaredConstructor();
    constructor.setAccessible(true);
    HystrixThreadPoolProperties.Setter setter = (HystrixThreadPoolProperties.Setter) constructor.newInstance();
    HystrixThreadPoolProperties properties = HystrixPlugins.getInstance().getPropertiesStrategy().getThreadPoolProperties(HystrixThreadPoolKey.Factory.asKey(key), setter);
    configMap.put("coreSize", properties.coreSize().get());
    configMap.put("maximumSize", properties.maximumSize().get());
    configMap.put("maxQueueSize", properties.maxQueueSize().get());
    configMap.put("actualMaximumSize", properties.actualMaximumSize());
    configMap.put("getAllowMaximumSizeToDivergeFromCoreSize", properties.getAllowMaximumSizeToDivergeFromCoreSize().get());
    configMap.put("keepAliveTimeMinutes", properties.keepAliveTimeMinutes().get());
    configMap.put("queueSizeRejectionThreshold", properties.queueSizeRejectionThreshold().get());
    configMap.put("metricsRollingStatisticalWindowBuckets", properties.metricsRollingStatisticalWindowBuckets().get());
    configMap.put("metricsRollingStatisticalWindowInMilliseconds", properties.metricsRollingStatisticalWindowInMilliseconds().get());
    return configMap;
  }

}
