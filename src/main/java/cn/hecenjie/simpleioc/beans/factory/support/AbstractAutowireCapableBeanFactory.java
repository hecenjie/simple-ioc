package cn.hecenjie.simpleioc.beans.factory.support;

import cn.hecenjie.simpleioc.beans.factory.config.AbstractBeanDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author cenjieHo
 * @since 2019/4/25
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory{

    private static final Logger logger = LoggerFactory.getLogger(AbstractAutowireCapableBeanFactory.class);

    /** 实例化 Bean 的策略类 */
    private InstantiationStrategy instantiationStrategy = new SimpleInstantiationStrategy();

    /** 是否允许循环依赖，默认允许 */
    private boolean allowCircularReferences = true;

    protected Object createBean(String beanName, AbstractBeanDefinition bd, Object[] args){
        logger.debug("Creating instance of bean '" + beanName + "'");
        try {
            Class<?> resolvedClass = resolveBeanClass(bd, beanName);
        } catch (ClassNotFoundException e) {
            logger.error("Class of bean '" + beanName + "' not found");
        }

        // 省略了method overrides
        // 省略了resolveBeforeInstantiation创建代理对象

        // 开始 Bean 的创建
        Object beanInstance = doCreateBean(beanName, bd, args);
        logger.debug("Finished creating instance of bean '" + beanName + "'");
        return beanInstance;
    }

    protected Object doCreateBean(String beanName, AbstractBeanDefinition bd, Object[] args){
        // 使用合适的实例化策略来创建新的实例
        Object bean = createBeanInstance(beanName, bd, args);

        boolean earlySingletonExposure = (bd.isSingleton() 	    // 如果为单例模式
                && this.allowCircularReferences 				// 允许循环依赖
                && isSingletonCurrentlyInCreation(beanName));	// 当前单例 Bean 正在被创建
        // 如果以上三个条件都满足，那么就将bean加入到三级缓存中
        // 这是解决单例模式循环依赖的关键
        if (earlySingletonExposure) {
            logger.debug("Eagerly caching bean '" + beanName + "' to allow for resolving potential circular references");
            // 这个方法的调用发生在 createBeanInstance() 方法之后，也就是说这个 bean 其实已经创建出来了，
            // 但是没有进行属性填充和初始化，但是此时已经可以根据对象引用定位到堆中该对象了，
            // 所以将该对象提前曝光出来，加入到三级缓存 singletonFactories 中
            addSingletonFactory(beanName, () -> bean);
        }

        // 开始初始化 Bean 实例对象
        Object exposedObject = bean;

        // 对 bean 进行填充，将各个属性值注入，其中可能存在依赖于其它 bean 的属性
        // 递归初始那些依赖 bean
        populateBean(beanName, mbd, instanceWrapper);
        // 调用初始化方法
        exposedObject = initializeBean(beanName, exposedObject, bd);

        // 注册 bean 的销毁方法
        // 与 InitializingBean 和 init-method 用于对象的自定义初始化工作相似，DisposableBean 和 destroy-method 用于对象的自定义销毁工作，
        // 但是并不是对象完成调用后就会立刻执行销毁方法，而是等到Spring容器关闭的时候才去调用，并且需要我们主动去告知Spring容器，
        // 对于 BeanFactory 容器需要调用 destroySingletons()方法，对于 ApplicationContext 容器需要调用 registerShutdownHook() 方法
        registerDisposableBeanIfNecessary(beanName, bean, bd);

        return exposedObject;
    }

    protected Object createBeanInstance(String beanName, AbstractBeanDefinition bd, Object[] args) {
        try {
            Class<?> beanClass = resolveBeanClass(bd, beanName);
        } catch (ClassNotFoundException e) {
            logger.error("resolve bean class failed");
        }
        // 这里省略了 Supplier 回调、工厂方法实例化、构造函数自动注入实例化的实现
        // 直接使用默认的构造函数实例化 Bean
        return instantiateBean(beanName, bd);
    }

    protected Object instantiateBean(final String beanName, final AbstractBeanDefinition mbd) {
        Object beanInstance = getInstantiationStrategy().instantiate(mbd, beanName);
        return beanInstance;
    }

    protected InstantiationStrategy getInstantiationStrategy() {
        return this.instantiationStrategy;
    }

}
