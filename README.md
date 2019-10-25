基于apollo的hystrix动态刷新的starter
=============

## 介绍
* 依赖环境：apollo
* 更改apollo配置即可动态刷新hystrix的配置,实现动态调整超时时间、起停断路器等功能

## 使用
~~~
auto.hystrix.enabled=true
hystrix.namespace=hystrix.yml
~~~
