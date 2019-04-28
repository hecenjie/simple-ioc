# simple-ioc

此项目为仿照 Spring 实现的一个简易版的 IoC 容器。

## V0.9版本

- 支持根据指定的路径从文件系统中获取资源
- 支持XSD模式的XML验证
- 支持`<property/>`标签以及`value`与`ref`属性（字符串与引用类型的属性注入）
- 支持单例Bean并解决了单例Bean下的循环依赖问题

## 预期实现

- 扩展资源加载策略，支持类路径与URL形式的资源
- ~~提供prototype的Bean~~
- 提供简单的类型转换（将`String`转换为`Integer`）
- 提供`byName`与`byType`方式的自动装配
- 提供Bean的后置处理
- 实现`ApplicationContext`体系