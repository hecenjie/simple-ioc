# summer-ioc

此项目为仿照 Spring 实现的一个简易版的 IoC 容器。

## Resource

该接口定义了对资源的统一抽象。

目前暂时只实现`FileSystemResource`子类（Spring 提供了`UrlResource`、`ClassPathResource`等更多子类），日后需要扩展.

## ResourceLoader

该接口定义了加载资源的统一抽象。

目前暂时只实现了`FileSystemResourceLoader`从文件系统中加载资源（其父类`DefaultResourceLoader`目前也是这么实现的），
Spring 对此还提供了`ClassRelativeResourceLoader`从给定的class所在包或者所在包的子包下加载资源，`ResourcePatternResolver`支持
根据指定的资源路径匹配模式每次返回多个`Resource`实例。

## BeanDefinition