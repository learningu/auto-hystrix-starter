基于apollo的hystrix动态刷新的starter
=============

## 介绍
* 依赖环境：apollo
* 更改apollo配置即可动态刷新hystrix的配置,实现动态调整超时时间、起停断路器等功能
* command相关的配置修改可动态生效
* threadpool的配置也要放在hystrix.yml里面,线程池相关的配置更改不会更改线程池
* 在/actuator/srch/hystrix/command中可看到当前hystrix comman的全局配置,带上参数key=${key}查看comand key的配置
* 在/actuator/srch/hystrix/threadpool中可看到当前hystrix threadpool的全局配置,带上参数key=${key}查看threadpool key的配置
* thread pool 默认的core size改成64,其余默认配置与原生hystrix一致
## 使用
~~~
auto.hystrix.enabled=true
hystrix.namespace=hystrix.yml
~~~
