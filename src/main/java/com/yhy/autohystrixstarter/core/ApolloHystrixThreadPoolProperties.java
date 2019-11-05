package com.yhy.autohystrixstarter.core;

import com.ctrip.framework.apollo.Config;
import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.netflix.hystrix.strategy.properties.HystrixProperty;


//threadPool里有个touchConfig的方法可动态修改线程池，但是改动太大。。
public class ApolloHystrixThreadPoolProperties extends HystrixThreadPoolProperties {


  //默认值是10,这里我们设置成64
  private static int default_coreSize = 64;            // core size of thread pool
  private static int default_maximumSize = 10;         // maximum size of thread pool
  private static int default_keepAliveTimeMinutes = 1; // minutes to keep a thread alive
  private static int default_maxQueueSize = -1;        // size of queue (this can't be dynamically changed so we use 'queueSizeRejectionThreshold' to artificially limit and reject)
  // -1 turns it off and makes us use SynchronousQueue
  private static boolean default_allow_maximum_size_to_diverge_from_core_size = false; //should the maximumSize config value get read and used in configuring the threadPool
  //turning this on should be a conscious decision by the user, so we default it to false

  private static int default_queueSizeRejectionThreshold = 5; // number of items in queue
  private static int default_threadPoolRollingNumberStatisticalWindow = 10000; // milliseconds for rolling number
  private static int default_threadPoolRollingNumberStatisticalWindowBuckets = 10; // number of buckets in rolling number (10 1-second buckets)


  private final HystrixProperty<Integer> corePoolSize;
  private final HystrixProperty<Integer> maximumPoolSize;
  private final HystrixProperty<Integer> keepAliveTime;
  private final HystrixProperty<Integer> maxQueueSize;
  private final HystrixProperty<Integer> queueSizeRejectionThreshold;
  private final HystrixProperty<Boolean> allowMaximumSizeToDivergeFromCoreSize;

  private final HystrixProperty<Integer> threadPoolRollingNumberStatisticalWindowInMilliseconds;
  private final HystrixProperty<Integer> threadPoolRollingNumberStatisticalWindowBuckets;

  private static final String propertyPrefix = "hystrix";

  protected ApolloHystrixThreadPoolProperties(HystrixThreadPoolKey key, Config config) {
    super(key);
    this.allowMaximumSizeToDivergeFromCoreSize = ApolloHystrixPropertyUtil.getProperty(propertyPrefix, ApolloHystrixPropertyUtil.KeyType.threadpool, key.name(), "allowMaximumSizeToDivergeFromCoreSize", config, default_allow_maximum_size_to_diverge_from_core_size);
    this.corePoolSize = ApolloHystrixPropertyUtil.getProperty(propertyPrefix, ApolloHystrixPropertyUtil.KeyType.threadpool, key.name(), "coreSize", config, default_coreSize);
    this.maximumPoolSize = ApolloHystrixPropertyUtil.getProperty(propertyPrefix, ApolloHystrixPropertyUtil.KeyType.threadpool, key.name(), "maximumSize", config, default_maximumSize);
    this.keepAliveTime = ApolloHystrixPropertyUtil.getProperty(propertyPrefix, ApolloHystrixPropertyUtil.KeyType.threadpool, key.name(), "keepAliveTimeMinutes", config, default_keepAliveTimeMinutes);
    this.maxQueueSize = ApolloHystrixPropertyUtil.getProperty(propertyPrefix, ApolloHystrixPropertyUtil.KeyType.threadpool, key.name(), "maxQueueSize", config, default_maxQueueSize);
    this.queueSizeRejectionThreshold = ApolloHystrixPropertyUtil.getProperty(propertyPrefix, ApolloHystrixPropertyUtil.KeyType.threadpool, key.name(), "queueSizeRejectionThreshold", config, default_queueSizeRejectionThreshold);
    this.threadPoolRollingNumberStatisticalWindowInMilliseconds = ApolloHystrixPropertyUtil.getProperty(propertyPrefix, ApolloHystrixPropertyUtil.KeyType.threadpool, key.name(), "metrics.rollingStats.timeInMilliseconds", config, default_threadPoolRollingNumberStatisticalWindow);
    this.threadPoolRollingNumberStatisticalWindowBuckets = ApolloHystrixPropertyUtil.getProperty(propertyPrefix, ApolloHystrixPropertyUtil.KeyType.threadpool, key.name(), "metrics.rollingStats.numBuckets", config, default_threadPoolRollingNumberStatisticalWindowBuckets);

  }

  @Override
  public HystrixProperty<Integer> coreSize() {
    return corePoolSize;
  }

  @Override
  public HystrixProperty<Integer> maximumSize() {
    return maximumPoolSize;
  }

  @Override
  public Integer actualMaximumSize() {
    final int coreSize = coreSize().get();
    final int maximumSize = maximumSize().get();
    if (getAllowMaximumSizeToDivergeFromCoreSize().get()) {
      if (coreSize > maximumSize) {
        return coreSize;
      } else {
        return maximumSize;
      }
    } else {
      return coreSize;
    }
  }

  @Override
  public HystrixProperty<Integer> keepAliveTimeMinutes() {
    return keepAliveTime;
  }

  @Override
  public HystrixProperty<Integer> maxQueueSize() {
    return maxQueueSize;
  }

  @Override
  public HystrixProperty<Integer> queueSizeRejectionThreshold() {
    return queueSizeRejectionThreshold;
  }

  @Override
  public HystrixProperty<Boolean> getAllowMaximumSizeToDivergeFromCoreSize() {
    return allowMaximumSizeToDivergeFromCoreSize;
  }

  @Override
  public HystrixProperty<Integer> metricsRollingStatisticalWindowInMilliseconds() {
    return threadPoolRollingNumberStatisticalWindowInMilliseconds;
  }

  @Override
  public HystrixProperty<Integer> metricsRollingStatisticalWindowBuckets() {
    return threadPoolRollingNumberStatisticalWindowBuckets;
  }
}
