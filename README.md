# Simple IoC

![](https://img.shields.io/badge/Version-1.0-blue.svg)

仿照 Spring 实现的一个简易版的 IoC 容器。

## Features

- 支持根据指定的路径从文件系统中获取资源
- 支持XSD模式的XML验证
- 支持`<property/>`标签以及`value`与`ref`属性（即字符串与引用类型的属性注入）
- 支持单例与原型Bean，并通过三级缓存解决了单例Bean的循环依赖问题

## Blog

- [资源加载](http://hecenjie.cn/2019/05/19/Simple-IoC%E5%BC%80%E5%8F%91%E6%97%A5%E5%BF%97%EF%BC%9A%E8%B5%84%E6%BA%90%E5%8A%A0%E8%BD%BD/)
- [注册BeanDefinition](http://hecenjie.cn/2019/05/19/Simple-IoC%E5%BC%80%E5%8F%91%E6%97%A5%E5%BF%97%EF%BC%9ABeanDefinition%E5%8A%A0%E8%BD%BD/)
- [Bean的加载](http://hecenjie.cn/2019/05/21/Simple-IoC%E5%BC%80%E5%8F%91%E6%97%A5%E5%BF%97%EF%BC%9AgetBean/)

## Quick start

第一个测试是模拟登陆接口的场景，分为`Controller`、`Service`、`Dao`三层，XML配置文件如下：
```xml
<?xml version="1.0" encoding="UTF-8"?>

<beans xmlns="http://www.springframework.org/schema/beans" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.springframework.org/schema/beans https://www.springframework.org/schema/beans/spring-beans-2.0.xsd">


	<bean id="loginController" class="cn.hecenjie.simpleioc.tests.objects.login.LoginController">
		<property name="loginService" ref="loginService" />
	</bean>
	
	<bean id="loginService" class="cn.hecenjie.simpleioc.tests.objects.login.LoginServiceImpl">
		<property name="userDao" ref="userDao" />
	</bean>
	
	<bean id="userDao" class="cn.hecenjie.simpleioc.tests.objects.login.UserDao"/>
	
</beans>
```

测试代码如下：
```java
    @Test
    public void testGetBean() {
        ResourceLoader resourceLoader = new FileSystemResourceLoader();
        Resource resource = resourceLoader.getResource("C:\\Users\\canjie\\Desktop\\login.xml");
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        BeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(factory);
        beanDefinitionReader.loadBeanDefinitions(resource);
        LoginController loginController = (LoginController) factory.getBean("loginController");
        assertEquals(loginController.login("Lihua", "123456789"), true);
    }
```

第二个测试是针对循环依赖问题的，XML配置文件如下：
```xml
<?xml version="1.0" encoding="UTF-8"?>

<beans xmlns="http://www.springframework.org/schema/beans" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.springframework.org/schema/beans https://www.springframework.org/schema/beans/spring-beans-2.0.xsd">


	<bean id="person" class="cn.hecenjie.simpleioc.tests.objects.persons.Person">
		<property name="name" value="Lihua" />
		<property name="age" value="18" />
		<property name="idCard" ref="idCard" />
	</bean>
	
	<bean id="idCard" class="cn.hecenjie.simpleioc.tests.objects.persons.IdCard">
		<property name="id" value="441301188875468912" />
		<property name="owner" ref="person" />
	</bean>
	
</beans>
```

测试代码如下：
```java
    @Test
    public void testCyclicDependence(){
        ResourceLoader resourceLoader = new FileSystemResourceLoader();
        Resource resource = resourceLoader.getResource("C:\\Users\\canjie\\Desktop\\persons.xml");
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        BeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(factory);
        beanDefinitionReader.loadBeanDefinitions(resource);
        Person person = (Person) factory.getBean("person");
        IdCard idCard = (IdCard) factory.getBean("idCard");
        assertEquals(person.getName(), "Lihua");
        assertEquals(person.getAge(), 18);
        assertEquals(person.getIdCard(), idCard);
        assertEquals(idCard.getId(), 441301188875468912L);
        assertEquals(idCard.getOwner(), person);
    }
```

## TODO

- 扩展资源加载策略，如支持类路径与URL形式的资源
- 提供`byName`与`byType`方式的自动装配，以及`@Autowired`注解
- 实现`ApplicationContext`体系
