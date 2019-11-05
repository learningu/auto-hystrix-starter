package com.yhy.autohystrixstarter.core;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import com.netflix.hystrix.strategy.HystrixPlugins;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.netflix.hystrix.strategy.eventnotifier.HystrixEventNotifier;
import com.netflix.hystrix.strategy.executionhook.HystrixCommandExecutionHook;
import com.netflix.hystrix.strategy.metrics.HystrixMetricsPublisher;
import com.yhy.autohystrixstarter.actuate.ApolloHystrixControllerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "auto.hystrix.enabled", havingValue = "true")
@EnableConfigurationProperties
public class HystrixConfigAutoConfiguration {
  private Logger logger = LoggerFactory.getLogger(HystrixConfigAutoConfiguration.class);

  //放hystrix配置的namespace,建议单独拉出来,apollo的config本质是个map,key太多可能会影响性能
  @Value("${hystrix.namespace:hystrix.yml}")
  private String hysrixNameSpace;

  @Bean
  public ApolloHystrixControllerEndpoint getApolloHystrixControllerEndpoint() {
    return new ApolloHystrixControllerEndpoint();
  }

  @Bean
  public ApolloHystrixConfPropertyStrategy getApolloHystrixConfPropertyStrategy() {
    Config config = ConfigService.getConfig(hysrixNameSpace);
    enableListener(config);
    ApolloHystrixConfPropertyStrategy strategy = new ApolloHystrixConfPropertyStrategy(config);
    HystrixMetricsPublisher hystrixMetricsPublisher = HystrixPlugins.getInstance().getMetricsPublisher();
    HystrixEventNotifier eventNotifier = HystrixPlugins.getInstance().getEventNotifier();
    HystrixCommandExecutionHook commandExecutionHook = HystrixPlugins.getInstance().getCommandExecutionHook();
    HystrixConcurrencyStrategy concurrencyStrategy = HystrixPlugins.getInstance().getConcurrencyStrategy();
    HystrixPlugins.reset();
    HystrixPlugins.getInstance().registerMetricsPublisher(hystrixMetricsPublisher);
    HystrixPlugins.getInstance().registerConcurrencyStrategy(concurrencyStrategy);
    HystrixPlugins.getInstance().registerEventNotifier(eventNotifier);
    HystrixPlugins.getInstance().registerPropertiesStrategy(strategy);
    HystrixPlugins.getInstance().registerCommandExecutionHook(commandExecutionHook);
    logger.info("init ApolloHystrixConfPropertyStrategy ok");
    return strategy;
  }

  private void enableListener(Config config) {
    config.addChangeListener(
      changeEvent -> changeEvent.changedKeys().forEach(key -> logger.info("key:{},value:{}", key, changeEvent.getChange(key))));
  }
}
