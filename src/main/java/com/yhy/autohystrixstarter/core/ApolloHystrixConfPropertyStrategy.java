package com.yhy.autohystrixstarter.core;

import com.ctrip.framework.apollo.Config;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.netflix.hystrix.strategy.properties.HystrixPropertiesStrategy;

public class ApolloHystrixConfPropertyStrategy extends HystrixPropertiesStrategy {

  private final Config config;


  public ApolloHystrixConfPropertyStrategy(Config config) {
    this.config = config;
  }




  @Override
  public HystrixCommandProperties getCommandProperties(HystrixCommandKey commandKey, HystrixCommandProperties.Setter builder) {
    ApolloHystrixCommandProperties commandProperties = new ApolloHystrixCommandProperties(commandKey, config);
    return commandProperties;
  }


  @Override
  public HystrixThreadPoolProperties getThreadPoolProperties(HystrixThreadPoolKey threadPoolKey, HystrixThreadPoolProperties.Setter builder) {
    return new ApolloHystrixThreadPoolProperties(threadPoolKey, config);
  }

}
