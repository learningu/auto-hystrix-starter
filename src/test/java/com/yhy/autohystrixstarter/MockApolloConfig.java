package com.yhy.autohystrixstarter;

import com.ctrip.framework.apollo.enums.ConfigSourceType;
import com.ctrip.framework.apollo.internals.AbstractConfig;

import java.util.Map;
import java.util.Set;


public class MockApolloConfig extends AbstractConfig {

  private volatile Map<String, String> configMap;


  public MockApolloConfig(Map<String, String> configMap) {
    this.configMap = configMap;
  }

  public void refreshConfig() {
    this.clearConfigCache();

  }

  @Override
  public String getProperty(String key, String defaultValue) {
    return configMap.getOrDefault(key, defaultValue);
  }

  @Override
  public Set<String> getPropertyNames() {
    return configMap.keySet();
  }

  @Override
  public ConfigSourceType getSourceType() {
    return ConfigSourceType.NONE;
  }
}
