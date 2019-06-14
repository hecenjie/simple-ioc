# Simple IoC

![](https://img.shields.io/badge/Version-1.0-blue.svg)

仿照 Spring 实现的一个简易版的 IoC 容器。

## Features

- 支持根据指定的路径从文件系统中获取资源
- 支持XSD模式的XML验证
- 支持`<property/>`标签以及`value`与`ref`属性（即字符串与引用类型的属性注入）
- 支持单例与原型Bean，并通过三级缓存解决了单例Bean的循环依赖问题

## Docs

- [资源加载](http://hecenjie.cn/2019/05/19/Simple-IoC%E5%BC%80%E5%8F%91%E6%97%A5%E5%BF%97%EF%BC%9A%E8%B5%84%E6%BA%90%E5%8A%A0%E8%BD%BD/)
- [注册BeanDefinition](http://hecenjie.cn/2019/05/19/Simple-IoC%E5%BC%80%E5%8F%91%E6%97%A5%E5%BF%97%EF%BC%9ABeanDefinition%E5%8A%A0%E8%BD%BD/)
- [Bean的加载](http://hecenjie.cn/2019/05/21/Simple-IoC%E5%BC%80%E5%8F%91%E6%97%A5%E5%BF%97%EF%BC%9AgetBean/)

## TODO

- 扩展资源加载策略，如支持类路径与URL形式的资源
- 提供`byName`与`byType`方式的自动装配，以及`@Autowired`注解
- 实现`ApplicationContext`体系