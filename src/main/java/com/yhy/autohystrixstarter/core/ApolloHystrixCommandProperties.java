package com.yhy.autohystrixstarter.core;

import com.ctrip.framework.apollo.Config;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.strategy.properties.HystrixProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

  public ApolloHystrixCommandProperties(HystrixCommandKey key, Setter builder, Config config) {
    super(key, builder);
    this.config = config;
    this.key = key;
  }

  private final static Logger logger = LoggerFactory.getLogger(ApolloHystrixCommandProperties.class);


  private static HystrixProperty<Boolean> getProperty(String propertyPrefix, HystrixCommandKey key, String instanceProperty, Config config, Boolean defaultValue) {
    String commandKey = propertyPrefix + ".command." + key.name() + "." + instanceProperty;
    String defaultKey = propertyPrefix + ".command.default." + instanceProperty;
    Boolean value = config.getBooleanProperty(commandKey, config.getBooleanProperty(defaultKey, defaultValue));
    return HystrixProperty.Factory.asProperty(value);
  }

  private static HystrixProperty<Integer> getProperty(String propertyPrefix, HystrixCommandKey key, String instanceProperty, Config config, Integer defaultValue) {
    String commandKey = propertyPrefix + ".command." + key.name() + "." + instanceProperty;
    String defaultKey = propertyPrefix + ".command.default." + instanceProperty;
    Integer value = config.getIntProperty(commandKey, config.getIntProperty(defaultKey, defaultValue));
    return HystrixProperty.Factory.asProperty(value);
  }

  @SuppressWarnings("unused")
  private static HystrixProperty<String> getProperty(String propertyPrefix, HystrixCommandKey key, String instanceProperty, Config config, String defaultValue) {
    String commandKey = propertyPrefix + ".command." + key.name() + "." + instanceProperty;
    String defaultKey = propertyPrefix + ".command.default." + instanceProperty;
    String value = config.getProperty(commandKey, config.getProperty(defaultKey, defaultValue));
    return HystrixProperty.Factory.asProperty(value);
  }

  @Override
  public HystrixProperty<Boolean> circuitBreakerEnabled() {
    return getProperty(propertyPrefix, key, "circuitBreaker.enabled", config, default_circuitBreakerEnabled);
  }

  @Override
  public HystrixProperty<Integer> circuitBreakerErrorThresholdPercentage() {
    return getProperty(propertyPrefix, key, "circuitBreaker.errorThresholdPercentage", config, default_circuitBreakerErrorThresholdPercentage);
  }

  @Override
  public HystrixProperty<Boolean> circuitBreakerForceClosed() {
    return getProperty(propertyPrefix, key, "circuitBreaker.forceClosed", config, default_circuitBreakerForceClosed);
  }

  @Override
  public HystrixProperty<Boolean> circuitBreakerForceOpen() {
    return getProperty(propertyPrefix, key, "circuitBreaker.forceOpen", config, default_circuitBreakerForceOpen);

  }

  @Override
  public HystrixProperty<Integer> circuitBreakerRequestVolumeThreshold() {
//    logger.info("requestVolumeThreshold=>" + getProperty(propertyPrefix, key, "circuitBreaker.requestVolumeThreshold", config, default_circuitBreakerRequestVolumeThreshold).get());
    return getProperty(propertyPrefix, key, "circuitBreaker.requestVolumeThreshold", config, default_circuitBreakerRequestVolumeThreshold);
  }

  @Override
  public HystrixProperty<Integer> circuitBreakerSleepWindowInMilliseconds() {
    return getProperty(propertyPrefix, key, "circuitBreaker.sleepWindowInMilliseconds", config, default_circuitBreakerSleepWindowInMilliseconds);

  }

  @Override
  public HystrixProperty<Integer> executionIsolationSemaphoreMaxConcurrentRequests() {
    return getProperty(propertyPrefix, key, "execution.isolation.semaphore.maxConcurrentRequests", config, default_executionIsolationSemaphoreMaxConcurrentRequests);

  }

  @Override
  public HystrixProperty<ExecutionIsolationStrategy> executionIsolationStrategy() {
    String strategy = getProperty(propertyPrefix, key, "execution.isolation.strategy", config, "THREAD").get();
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
    return getProperty(propertyPrefix, key, "execution.isolation.thread.interruptOnTimeout", config, default_executionIsolationThreadInterruptOnTimeout);

  }

  @Override
  public HystrixProperty<Boolean> executionIsolationThreadInterruptOnFutureCancel() {
    return getProperty(propertyPrefix, key, "execution.isolation.thread.interruptOnFutureCancel", config, default_executionIsolationThreadInterruptOnFutureCancel);

  }


  @Override
  public HystrixProperty<Integer> executionIsolationThreadTimeoutInMilliseconds() {
    return getProperty(propertyPrefix, key, "execution.isolation.thread.timeoutInMilliseconds", config, default_executionTimeoutInMilliseconds);

  }

  @Override
  public HystrixProperty<Integer> executionTimeoutInMilliseconds() {
    return getProperty(propertyPrefix, key, "execution.isolation.thread.timeoutInMilliseconds", config, default_executionTimeoutInMilliseconds);

  }

  @Override
  public HystrixProperty<Boolean> executionTimeoutEnabled() {
    return getProperty(propertyPrefix, key, "execution.timeout.enabled", config, default_executionTimeoutEnabled);

  }

  @Override
  public HystrixProperty<Integer> fallbackIsolationSemaphoreMaxConcurrentRequests() {
    return getProperty(propertyPrefix, key, "fallback.isolation.semaphore.maxConcurrentRequests", config, default_fallbackIsolationSemaphoreMaxConcurrentRequests);

  }

  @Override
  public HystrixProperty<Boolean> fallbackEnabled() {
    return getProperty(propertyPrefix, key, "fallback.enabled", config, default_fallbackEnabled);

  }

  @Override
  public HystrixProperty<Integer> metricsHealthSnapshotIntervalInMilliseconds() {
    return getProperty(propertyPrefix, key, "metrics.healthSnapshot.intervalInMilliseconds", config, default_metricsHealthSnapshotIntervalInMilliseconds);

  }

  @Override
  public HystrixProperty<Integer> metricsRollingPercentileBucketSize() {
    return getProperty(propertyPrefix, key, "metrics.rollingPercentile.bucketSize", config, default_metricsRollingPercentileBucketSize);

  }

  @Override
  public HystrixProperty<Boolean> metricsRollingPercentileEnabled() {
    return getProperty(propertyPrefix, key, "metrics.rollingPercentile.enabled", config, default_metricsRollingPercentileEnabled);

  }

  @Override
  public HystrixProperty<Integer> metricsRollingPercentileWindow() {
    return getProperty(propertyPrefix, key, "metrics.rollingPercentile.timeInMilliseconds", config, default_metricsRollingPercentileWindow);

  }

  @Override
  public HystrixProperty<Integer> metricsRollingPercentileWindowInMilliseconds() {
    return getProperty(propertyPrefix, key, "metrics.rollingPercentile.timeInMilliseconds", config, default_metricsRollingPercentileWindow);

  }

  @Override
  public HystrixProperty<Integer> metricsRollingPercentileWindowBuckets() {
    return getProperty(propertyPrefix, key, "metrics.rollingPercentile.numBuckets", config, default_metricsRollingPercentileWindowBuckets);
  }

  @Override
  public HystrixProperty<Integer> metricsRollingStatisticalWindowInMilliseconds() {
    return getProperty(propertyPrefix, key, "metrics.rollingStats.timeInMilliseconds", config, default_metricsRollingStatisticalWindow);

  }

  @Override
  public HystrixProperty<Integer> metricsRollingStatisticalWindowBuckets() {
    return getProperty(propertyPrefix, key, "metrics.rollingStats.numBuckets", config, default_metricsRollingStatisticalWindowBuckets);

  }

  @Override
  public HystrixProperty<Boolean> requestCacheEnabled() {
    return getProperty(propertyPrefix, key, "requestCache.enabled", config, default_requestCacheEnabled);

  }

  @Override
  public HystrixProperty<Boolean> requestLogEnabled() {
    return getProperty(propertyPrefix, key, "requestLog.enabled", config, default_requestLogEnabled);

  }


  public void enableListener() {
    config.addChangeListener(
      changeEvent -> changeEvent.changedKeys().forEach(key -> logger.info("key:{},value:{}", key, changeEvent.getChange(key))));
  }


}
