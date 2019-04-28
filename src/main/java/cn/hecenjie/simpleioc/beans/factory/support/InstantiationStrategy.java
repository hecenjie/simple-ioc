package cn.hecenjie.simpleioc.beans.factory.support;

import cn.hecenjie.simpleioc.beans.factory.BeanFactory;
import cn.hecenjie.simpleioc.beans.factory.BeansException;
import cn.hecenjie.simpleioc.beans.factory.config.AbstractBeanDefinition;

/**
 * @author cenjieHo
 * @since 2019/4/26
 */
public interface InstantiationStrategy {

    Object instantiate(AbstractBeanDefinition bd, String beanName)
            throws BeansException;
}
