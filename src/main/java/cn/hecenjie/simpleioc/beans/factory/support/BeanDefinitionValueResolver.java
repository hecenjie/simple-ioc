package cn.hecenjie.simpleioc.beans.factory.support;

import cn.hecenjie.simpleioc.beans.factory.BeansException;
import cn.hecenjie.simpleioc.beans.factory.config.BeanDefinition;
import cn.hecenjie.simpleioc.beans.factory.config.RuntimeBeanReference;
import cn.hecenjie.simpleioc.beans.factory.config.TypedStringValue;

/**
 * @author cenjieHo
 * @since 2019/4/27
 */
public class BeanDefinitionValueResolver {

    private final AbstractBeanFactory beanFactory;

    private final String beanName;

    private final BeanDefinition beanDefinition;

    public BeanDefinitionValueResolver(
            AbstractBeanFactory beanFactory, String beanName, BeanDefinition beanDefinition) {
        this.beanFactory = beanFactory;
        this.beanName = beanName;
        this.beanDefinition = beanDefinition;
    }

    public Object resolveValueIfNecessary(Object argName, Object value) {
        // 这里省略了List、Array、Map、Set等的处理
        if (value instanceof RuntimeBeanReference) {
            RuntimeBeanReference ref = (RuntimeBeanReference) value;
            return resolveReference(argName, ref);
        } else if (value instanceof String) {
            return value;
        } else{
            return value;
        }
    }

    private Object resolveReference(Object argName, RuntimeBeanReference ref) {
        try {
            Object bean;
            String refName = ref.getBeanName();
            bean = this.beanFactory.getBean(refName);
            this.beanFactory.registerDependentBean(refName, this.beanName);
            return bean;
        } catch (BeansException ex) {
            throw new BeansException(
                    "Cannot resolve reference to bean '" + ref.getBeanName() + "' while setting " + argName, ex);
        }
    }
}
