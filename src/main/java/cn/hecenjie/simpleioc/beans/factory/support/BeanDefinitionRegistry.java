package cn.hecenjie.simpleioc.beans.factory.support;

import cn.hecenjie.simpleioc.beans.factory.BeansException;
import cn.hecenjie.simpleioc.beans.factory.config.BeanDefinition;
import cn.hecenjie.simpleioc.core.AliasRegistry;

/**
 * @author cenjieHo
 * @since 2019/4/23
 */
public interface BeanDefinitionRegistry extends AliasRegistry {

    /**
     * 往注册表中注册一个新的 BeanDefinition 实例
     * @param beanName
     * @param beanDefinition
     * @throws BeansException
     */
    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition)
            throws BeansException;

    /**
     * @return 返回注册表中 BeanDefinition 实例的数量
     */
    int getBeanDefinitionCount();
}
