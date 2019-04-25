package cn.hecenjie.summerioc.beans.factory.support;

import cn.hecenjie.summerioc.beans.factory.config.BeanDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author cenjieHo
 * @since 2019/4/25
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory{

    private static final Logger logger = LoggerFactory.getLogger(AbstractAutowireCapableBeanFactory.class);

    protected Object createBean(String beanName, BeanDefinition bd, Object[] args){
        logger.debug("Creating instance of bean '" + beanName + "'");

    }

}
