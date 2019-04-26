package cn.hecenjie.simpleioc.beans.factory.support;

import cn.hecenjie.simpleioc.beans.factory.BeansException;
import cn.hecenjie.simpleioc.core.io.FileSystemResourceLoader;
import cn.hecenjie.simpleioc.core.io.Resource;
import cn.hecenjie.simpleioc.core.io.ResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author cenjieHo
 * @since 2019/4/24
 */
public abstract class AbstractBeanDefinitionReader implements BeanDefinitionReader{

    /** 让子类也可以使用该logger，注意不能声明为static */
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private final BeanDefinitionRegistry registry;

    private ResourceLoader resourceLoader;

    protected AbstractBeanDefinitionReader(BeanDefinitionRegistry registry) {
        this.registry = registry;
        if (this.registry instanceof ResourceLoader) {
            this.resourceLoader = (ResourceLoader) this.registry;
        }
        else {
            this.resourceLoader = new FileSystemResourceLoader();
        }
    }

    public final BeanDefinitionRegistry getRegistry() {
        return this.registry;
    }

    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public ResourceLoader getResourceLoader() {
        return this.resourceLoader;
    }

    public int loadBeanDefinitions(Resource... resources) throws BeansException {
        int counter = 0;
        for (Resource resource : resources) {
            counter += loadBeanDefinitions(resource);   // 模板方法的体现，将具体实现交由子类完成
        }
        return counter;
    }

    public int loadBeanDefinitions(String... locations) throws BeansException {
        int counter = 0;
        for (String location : locations) {
            counter += loadBeanDefinitions(location);
        }
        return counter;
    }

    public int loadBeanDefinitions(String location) throws BeansException {
        throw new BeansException("no support to load bean definitions by location");
    }
}
