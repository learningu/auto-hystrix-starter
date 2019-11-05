package com.yhy.autohystrixstarter.core;

import com.ctrip.framework.apollo.Config;
import com.netflix.hystrix.strategy.properties.HystrixProperty;

public class ApolloHystrixPropertyUtil {

  public static HystrixProperty<Boolean> getProperty(String propertyPrefix, KeyType keyType, String keyName, String instanceProperty, Config config, Boolean defaultValue) {
    String commandKey = propertyPrefix + "." + keyType.toString() + "." + keyName + "." + instanceProperty;
    String defaultKey = propertyPrefix + "." + keyType.toString() + ".default." + instanceProperty;
    Boolean value = config.getBooleanProperty(commandKey, config.getBooleanProperty(defaultKey, defaultValue));
    return HystrixProperty.Factory.asProperty(value);
  }

  public static HystrixProperty<Integer> getProperty(String propertyPrefix, KeyType keyType, String keyName, String instanceProperty, Config config, Integer defaultValue) {
    String commandKey = propertyPrefix + "." + keyType.toString() + "." + keyName + "." + instanceProperty;
    String defaultKey = propertyPrefix + "." + keyType.toString() + ".default." + instanceProperty;
    Integer value = config.getIntProperty(commandKey, config.getIntProperty(defaultKey, defaultValue));
    return HystrixProperty.Factory.asProperty(value);
  }

  @SuppressWarnings("unused")
  public static HystrixProperty<String> getProperty(String propertyPrefix, KeyType keyType, String keyName, String instanceProperty, Config config, String defaultValue) {
    String commandKey = propertyPrefix + "." + keyType.toString() + "." + keyName + "." + instanceProperty;
    String defaultKey = propertyPrefix + "." + keyType.toString() + ".default." + instanceProperty;
    String value = config.getProperty(commandKey, config.getProperty(defaultKey, defaultValue));
    return HystrixProperty.Factory.asProperty(value);
  }

  public enum KeyType {
    command, threadpool
  }
}
