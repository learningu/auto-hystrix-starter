package com.yhy.autohystrixstarter.core;

import com.ctrip.framework.apollo.Config;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.strategy.properties.HystrixProperty;

public class ApolloHystrixCommandProperties extends HystrixCommandProperties {


  private static final Integer default_metricsRollingStatisticalWindowBuckets = 10;// default => statisticalWindowBuckets: 10 = 10 buckets in a 10 second window so each bucket is 1 second
  private static final Integer default_circuitBreakerRequestVolumeThreshold = 20;// default => statisticalWindowVolumeThreshold: 20 requests in 10 seconds must occur before statistics matter
  private static final Integer default_circuitBreakerSleepWindowInMilliseconds = 5000;// default => sleepWindow: 5000 = 5 seconds that we will sleep before trying again after tripping the circuit
  private static final Integer default_circuitBreakerErrorThresholdPercentage = 50;// default => errorThresholdPercentage = 50 = if 50%+ of requests in 10 seconds are failures or latent then we will trip the circuit
  private static final Boolean default_circuitBreakerForceOpen = false;// default => forceCircuitOpen = false (we want to allow traffic)
  /* package */ static final Boolean default_circuitBreakerForceClosed = false;// default => ignoreErrors = false
  private static final Integer default_executionTimeoutInMilliseconds = 1000; // default => executionTimeoutInMilliseconds: 1000 = 1 second
  private static final Boolean default_executionTimeoutEnabled = true;
  private static final Integer default_metricsRollingStatisticalWindow = 1000;
  private static final ExecutionIsolationStrategy default_executionIsolationStrategy = ExecutionIsolationStrategy.THREAD;
  private static final Boolean default_executionIsolationThreadInterruptOnTimeout = true;
  private static final Boolean default_executionIsolationThreadInterruptOnFutureCancel = false;
  private static final Boolean default_metricsRollingPercentileEnabled = true;
  private static final Boolean default_requestCacheEnabled = true;
  private static final Integer default_fallbackIsolationSemaphoreMaxConcurrentRequests = 10;
  private static final Boolean default_fallbackEnabled = true;
  private static final Integer default_executionIsolationSemaphoreMaxConcurrentRequests = 10;
  private static final Boolean default_requestLogEnabled = true;
  private static final Boolean default_circuitBreakerEnabled = true;
  private static final Integer default_metricsRollingPercentileWindow = 60000; // default to 1 minute for RollingPercentile
  private static final Integer default_metricsRollingPercentileWindowBuckets = 6; // default to 6 buckets (10 seconds each in 60 second window)
  private static final Integer default_metricsRollingPercentileBucketSize = 100; // default to 100 values max per bucket
  private static final Integer default_metricsHealthSnapshotIntervalInMilliseconds = 500; // default to 500ms as max frequency between allowing snapshots of health (error percentage etc)

  private final Config config;
  private final HystrixCommandKey key;

  private static final String propertyPrefix = "hystrix";

  public ApolloHystrixCommandProperties(HystrixCommandKey key, Config config) {
    super(key);
    this.config = config;
    this.key = key;
  }


  @Override
  public HystrixProperty<Boolean> circuitBreakerEnabled() {
    return ApolloHystrixPropertyUtil.getProperty(propertyPrefix, ApolloHystrixPropertyUtil.KeyType.command, key.name(), "circuitBreaker.enabled", config, default_circuitBreakerEnabled);
  }

  @Override
  public HystrixProperty<Integer> circuitBreakerErrorThresholdPercentage() {
    return ApolloHystrixPropertyUtil.getProperty(propertyPrefix, ApolloHystrixPropertyUtil.KeyType.command, key.name(), "circuitBreaker.errorThresholdPercentage", config, default_circuitBreakerErrorThresholdPercentage);
  }

  @Override
  public HystrixProperty<Boolean> circuitBreakerForceClosed() {
    return ApolloHystrixPropertyUtil.getProperty(propertyPrefix, ApolloHystrixPropertyUtil.KeyType.command, key.name(), "circuitBreaker.forceClosed", config, default_circuitBreakerForceClosed);
  }

  @Override
  public HystrixProperty<Boolean> circuitBreakerForceOpen() {
    return ApolloHystrixPropertyUtil.getProperty(propertyPrefix, ApolloHystrixPropertyUtil.KeyType.command, key.name(), "circuitBreaker.forceOpen", config, default_circuitBreakerForceOpen);

  }

  @Override
  public HystrixProperty<Integer> circuitBreakerRequestVolumeThreshold() {
    return ApolloHystrixPropertyUtil.getProperty(propertyPrefix, ApolloHystrixPropertyUtil.KeyType.command, key.name(), "circuitBreaker.requestVolumeThreshold", config, default_circuitBreakerRequestVolumeThreshold);
  }

  @Override
  public HystrixProperty<Integer> circuitBreakerSleepWindowInMilliseconds() {
    return ApolloHystrixPropertyUtil.getProperty(propertyPrefix, ApolloHystrixPropertyUtil.KeyType.command, key.name(), "circuitBreaker.sleepWindowInMilliseconds", config, default_circuitBreakerSleepWindowInMilliseconds);

  }

  @Override
  public HystrixProperty<Integer> executionIsolationSemaphoreMaxConcurrentRequests() {
    return ApolloHystrixPropertyUtil.getProperty(propertyPrefix, ApolloHystrixPropertyUtil.KeyType.command, key.name(), "execution.isolation.semaphore.maxConcurrentRequests", config, default_executionIsolationSemaphoreMaxConcurrentRequests);

  }

  @Override
  public HystrixProperty<ExecutionIsolationStrategy> executionIsolationStrategy() {
    String strategy = ApolloHystrixPropertyUtil.getProperty(propertyPrefix, ApolloHystrixPropertyUtil.KeyType.command, key.name(), "execution.isolation.strategy", config, "THREAD").get();
    switch (ExecutionIsolationStrategy.valueOf(strategy)) {
      case THREAD:
        return HystrixProperty.Factory.asProperty(ExecutionIsolationStrategy.THREAD);
      case SEMAPHORE:
        return HystrixProperty.Factory.asProperty(ExecutionIsolationStrategy.SEMAPHORE);
      default:
        return HystrixProperty.Factory.asProperty(ExecutionIsolationStrategy.THREAD);
    }
  }

  @Override
  public HystrixProperty<Boolean> executionIsolationThreadInterruptOnTimeout() {
    return ApolloHystrixPropertyUtil.getProperty(propertyPrefix, ApolloHystrixPropertyUtil.KeyType.command, key.name(), "execution.isolation.thread.interruptOnTimeout", config, default_executionIsolationThreadInterruptOnTimeout);

  }

  @Override
  public HystrixProperty<Boolean> executionIsolationThreadInterruptOnFutureCancel() {
    return ApolloHystrixPropertyUtil.getProperty(propertyPrefix, ApolloHystrixPropertyUtil.KeyType.command, key.name(), "execution.isolation.thread.interruptOnFutureCancel", config, default_executionIsolationThreadInterruptOnFutureCancel);

  }


  @Override
  public HystrixProperty<Integer> executionIsolationThreadTimeoutInMilliseconds() {
    return ApolloHystrixPropertyUtil.getProperty(propertyPrefix, ApolloHystrixPropertyUtil.KeyType.command, key.name(), "execution.isolation.thread.timeoutInMilliseconds", config, default_executionTimeoutInMilliseconds);

  }

  @Override
  public HystrixProperty<Integer> executionTimeoutInMilliseconds() {
    return ApolloHystrixPropertyUtil.getProperty(propertyPrefix, ApolloHystrixPropertyUtil.KeyType.command, key.name(), "execution.isolation.thread.timeoutInMilliseconds", config, default_executionTimeoutInMilliseconds);

  }

  @Override
  public HystrixProperty<Boolean> executionTimeoutEnabled() {
    return ApolloHystrixPropertyUtil.getProperty(propertyPrefix, ApolloHystrixPropertyUtil.KeyType.command, key.name(), "execution.timeout.enabled", config, default_executionTimeoutEnabled);

  }

  @Override
  public HystrixProperty<Integer> fallbackIsolationSemaphoreMaxConcurrentRequests() {
    return ApolloHystrixPropertyUtil.getProperty(propertyPrefix, ApolloHystrixPropertyUtil.KeyType.command, key.name(), "fallback.isolation.semaphore.maxConcurrentRequests", config, default_fallbackIsolationSemaphoreMaxConcurrentRequests);

  }

  @Override
  public HystrixProperty<Boolean> fallbackEnabled() {
    return ApolloHystrixPropertyUtil.getProperty(propertyPrefix, ApolloHystrixPropertyUtil.KeyType.command, key.name(), "fallback.enabled", config, default_fallbackEnabled);

  }

  @Override
  public HystrixProperty<Integer> metricsHealthSnapshotIntervalInMilliseconds() {
    return ApolloHystrixPropertyUtil.getProperty(propertyPrefix, ApolloHystrixPropertyUtil.KeyType.command, key.name(), "metrics.healthSnapshot.intervalInMilliseconds", config, default_metricsHealthSnapshotIntervalInMilliseconds);

  }

  @Override
  public HystrixProperty<Integer> metricsRollingPercentileBucketSize() {
    return ApolloHystrixPropertyUtil.getProperty(propertyPrefix, ApolloHystrixPropertyUtil.KeyType.command, key.name(), "metrics.rollingPercentile.bucketSize", config, default_metricsRollingPercentileBucketSize);

  }

  @Override
  public HystrixProperty<Boolean> metricsRollingPercentileEnabled() {
    return ApolloHystrixPropertyUtil.getProperty(propertyPrefix, ApolloHystrixPropertyUtil.KeyType.command, key.name(), "metrics.rollingPercentile.enabled", config, default_metricsRollingPercentileEnabled);

  }

  @Override
  public HystrixProperty<Integer> metricsRollingPercentileWindow() {
    return ApolloHystrixPropertyUtil.getProperty(propertyPrefix, ApolloHystrixPropertyUtil.KeyType.command, key.name(), "metrics.rollingPercentile.timeInMilliseconds", config, default_metricsRollingPercentileWindow);

  }

  @Override
  public HystrixProperty<Integer> metricsRollingPercentileWindowInMilliseconds() {
    return ApolloHystrixPropertyUtil.getProperty(propertyPrefix, ApolloHystrixPropertyUtil.KeyType.command, key.name(), "metrics.rollingPercentile.timeInMilliseconds", config, default_metricsRollingPercentileWindow);

  }

  @Override
  public HystrixProperty<Integer> metricsRollingPercentileWindowBuckets() {
    return ApolloHystrixPropertyUtil.getProperty(propertyPrefix, ApolloHystrixPropertyUtil.KeyType.command, key.name(), "metrics.rollingPercentile.numBuckets", config, default_metricsRollingPercentileWindowBuckets);
  }

  @Override
  public HystrixProperty<Integer> metricsRollingStatisticalWindowInMilliseconds() {
    return ApolloHystrixPropertyUtil.getProperty(propertyPrefix, ApolloHystrixPropertyUtil.KeyType.command, key.name(), "metrics.rollingStats.timeInMilliseconds", config, default_metricsRollingStatisticalWindow);

  }

  @Override
  public HystrixProperty<Integer> metricsRollingStatisticalWindowBuckets() {
    return ApolloHystrixPropertyUtil.getProperty(propertyPrefix, ApolloHystrixPropertyUtil.KeyType.command, key.name(), "metrics.rollingStats.numBuckets", config, default_metricsRollingStatisticalWindowBuckets);

  }

  @Override
  public HystrixProperty<Boolean> requestCacheEnabled() {
    return ApolloHystrixPropertyUtil.getProperty(propertyPrefix, ApolloHystrixPropertyUtil.KeyType.command, key.name(), "requestCache.enabled", config, default_requestCacheEnabled);

  }

  @Override
  public HystrixProperty<Boolean> requestLogEnabled() {
    return ApolloHystrixPropertyUtil.getProperty(propertyPrefix, ApolloHystrixPropertyUtil.KeyType.command, key.name(), "requestLog.enabled", config, default_requestLogEnabled);

  }

  @Override
  public HystrixProperty<String> executionIsolationThreadPoolKeyOverride() {
    return HystrixProperty.Factory.asProperty(config.getProperty(propertyPrefix + ".command." + key.name() + ".threadPoolKeyOverride", null));
  }


}
