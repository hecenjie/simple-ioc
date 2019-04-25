package cn.hecenjie.summerioc.beans.factory.support;

import cn.hecenjie.summerioc.beans.factory.BeansException;
import cn.hecenjie.summerioc.beans.factory.ListableBeanFactory;
import cn.hecenjie.summerioc.beans.factory.config.BeanDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author cenjieHo
 * @since 2019/4/25
 */
public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory
        implements BeanDefinitionRegistry, ListableBeanFactory {

    private static final Logger logger = LoggerFactory.getLogger(DefaultListableBeanFactory.class);

    /** 注册表，记录了 beanName -> BeanDefinition 的映射关系 */
    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws BeansException {
        this.beanDefinitionMap.put(beanName, beanDefinition);
        logger.debug("The mapping from " + beanName + " to its BeanDefinition has been registered");
    }

    @Override
    public int getBeanDefinitionCount() {
        return this.beanDefinitionMap.size();
    }

}

