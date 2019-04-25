package cn.hecenjie.summerioc.beans.factory.support;

import cn.hecenjie.summerioc.beans.factory.BeansException;
import cn.hecenjie.summerioc.beans.factory.config.AbstractBeanDefinition;
import cn.hecenjie.summerioc.beans.factory.config.BeanDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author cenjieHo
 * @since 2019/4/25
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory{

    private static final Logger logger = LoggerFactory.getLogger(AbstractAutowireCapableBeanFactory.class);

    protected Object createBean(String beanName, AbstractBeanDefinition bd, Object[] args){
        logger.debug("Creating instance of bean '" + beanName + "'");
        try {
            Class<?> resolvedClass = resolveBeanClass(bd, beanName);
        } catch (ClassNotFoundException e) {
            logger.error("Class of bean '" + beanName + "' not found");
        }

        // 省略了method overrides


    }

}
