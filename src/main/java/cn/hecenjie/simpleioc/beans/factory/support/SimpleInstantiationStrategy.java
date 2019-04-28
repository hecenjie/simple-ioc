package cn.hecenjie.simpleioc.beans.factory.support;

import cn.hecenjie.simpleioc.beans.factory.BeanFactory;
import cn.hecenjie.simpleioc.beans.factory.BeansException;
import cn.hecenjie.simpleioc.beans.factory.config.AbstractBeanDefinition;

import java.lang.reflect.Constructor;

/**
 * @author cenjieHo
 * @since 2019/4/26
 */
public class SimpleInstantiationStrategy implements InstantiationStrategy {
    @Override
    public Object instantiate(AbstractBeanDefinition bd, String beanName) throws BeansException {
        // 这里省略了CGLIB相关的判断与操作
        // 直接使用反射创建对象
        final Class<?> clazz = bd.getBeanClass();	// 获得 Bean 的 class 对象
        if (clazz.isInterface()) {	// 如果是接口，则抛出 BeanInstantiationException 异常
            throw new BeansException("Specified class" + clazz + " is an interface");
        }
        try {
            Constructor<?> constructorToUse = clazz.getDeclaredConstructor(); // 从 class 对象中获得构造函数，赋给 constructorToUse
            return constructorToUse.newInstance();  // 直接使用反射创建实例，调用的是无参构造函数
        } catch (Exception ex){
            throw new BeansException("Failed to instantiate a new bean");
        }
    }
}
