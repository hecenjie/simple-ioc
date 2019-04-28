package cn.hecenjie.simpleioc.beans.factory.support;

import cn.hecenjie.simpleioc.beans.factory.BeansException;
import cn.hecenjie.simpleioc.beans.factory.ListableBeanFactory;
import cn.hecenjie.simpleioc.beans.factory.config.AbstractBeanDefinition;
import cn.hecenjie.simpleioc.beans.factory.config.BeanDefinition;
import cn.hecenjie.simpleioc.beans.factory.config.BeanReference;
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

    public BeanDefinition getBeanDefinition(String beanName) {
        BeanDefinition bd = this.beanDefinitionMap.get(beanName);
        if (bd == null) {
            logger.error("No bean named '" + beanName + "' found in " + this);
        }
        return bd;
    }

}

