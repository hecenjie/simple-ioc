package cn.hecenjie.simpleioc.beans.factory.support;

import cn.hecenjie.simpleioc.beans.factory.BeansException;
import cn.hecenjie.simpleioc.beans.factory.ObjectFactory;
import cn.hecenjie.simpleioc.beans.factory.config.SingletonBeanRegistry;
import cn.hecenjie.simpleioc.core.SimpleAliasRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author cenjieHo
 * @since 2019/4/25
 */
public class DefaultSingletonBeanRegistry extends SimpleAliasRegistry implements SingletonBeanRegistry {

    private static final Logger logger = LoggerFactory.getLogger(DefaultSingletonBeanRegistry.class);

    /** 一级缓存：存放的是单例的 beanName 到 bean 实例的映射 */
    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);

    /**
     * 二级缓存：存放的也是 beanName 到 bean 实例的映射关系，它与 {@link #singletonObjects} 的区别在于它存放的 bean 不一定完整。
     * 从 {@link #getSingleton(String)} 方法中，我们可以了解到 bean 在创建过程就已经加入到 earlySingletonObjects 中了，
     * 所以在 bean 的创建过程中，就可以通过 getBean() 方法获取。它也是解决循环依赖的关键所在。
     */
    private final Map<String, Object> earlySingletonObjects = new HashMap<>(16);

    /** 三级缓存：存放的是单例的 beanName 到创建 bean 实例的 factory 的映射 */
    private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap<>(16);

    /** 正在创建中的单例 bean 的名字集合 */
    private final Set<String> singletonsCurrentlyInCreation =
            Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>(16));

    /** 已经注册的单例缓存 */
    private final Set<String> registeredSingletons = new LinkedHashSet<>(256);

    @Override
    public void registerSingleton(String beanName, Object singletonObject) {

    }

    @Override
    public Object getSingleton(String beanName) {
        return getSingleton(beanName, true);
    }

    /**
     * 尝试从缓存中获取 Bean，此方法流程：
     * 1. 首先，从一级缓存 singletonObjects 获取
     * 2. 如果一级缓存中没有且当前指定的 beanName 正在创建，就再从二级缓存 earlySingletonObjects 中获取
     * 3. 如果二级缓存中没有且允许 singletonFactories 通过 getObject() 方法获取，则从三级缓存 singletonFactories 获取该 beanName 的 ObjectFactory，
     * 	  如果获取到则通过其 getObject() 方法获取对象，并将其加入到二级缓存 earlySingletonObjects 中，并从三级缓存 singleFactories 删除。
     * 	  这样，就从三级缓存升级到二级缓存了。所以，二级缓存存在的意义就是缓存三级缓存中的 ObjectFactory 的 getObject() 方法的执行结果，
     * 	  提早曝光的单例 Bean 对象。
     *
     * @param beanName
     * @param allowEarlyReference
     * @return
     */
    protected Object getSingleton(String beanName, boolean allowEarlyReference) {
        // 从单例缓存（一级缓存）中加载 bean
        Object singletonObject = this.singletonObjects.get(beanName);
        // 如果当前缓存中的 bean 为空，且当前 bean 正在创建
        if (singletonObject == null && isSingletonCurrentlyInCreation(beanName)) {
            // 加锁
            synchronized (this.singletonObjects) {
                // 从 earlySingletonObjects（二级缓存）中获取
                singletonObject = this.earlySingletonObjects.get(beanName);
                // 如果 earlySingletonObjects（二级缓存）中没有，并且允许提前创建
                if (singletonObject == null && allowEarlyReference) {    // allowEarlyReference：是否允许从 singletonFactories 缓存中通过 getObject() 方法拿到对象
                    // 从 singletonFactories（三级缓存）中获取对应的 ObjectFactory
                    ObjectFactory<?> singletonFactory = this.singletonFactories.get(beanName);
                    if (singletonFactory != null) {
                        // 通过 ObjectFactory 获得 bean
                        singletonObject = singletonFactory.getObject();
                        // 添加 bean 到 earlySingletonObjects，注意这里还没有添加到 singletonObjects 中，因为并未创建结束
                        this.earlySingletonObjects.put(beanName, singletonObject);
                        // 从 singletonFactories（三级缓存）中移除对应的 ObjectFactory，
                        // 这样之后的线程在判断 singletonFactory != null 为 false 时就不会在进入到这里执行
                        this.singletonFactories.remove(beanName);
                    }
                }
            }
        }
        return singletonObject;
    }

    /**
     * 不同于重载的方法，这里不是从缓存中获取 Bean，而是从头创建一个 Bean
     * @param beanName
     * @param singletonFactory
     * @return
     */
    public Object getSingleton(String beanName, ObjectFactory<?> singletonFactory) {
        // 双重校验锁
        synchronized (this.singletonObjects) {
            // 再次从缓存中检查一遍，如果加载了则直接返回
            Object singletonObject = this.singletonObjects.get(beanName);
            // 缓存中没有，开始加载过程
            if (singletonObject == null) {
                logger.debug("Creating shared instance of singleton bean '" + beanName + "'");
            }
            // 加载前置处理，其实就是将其标记为正在创建中
            beforeSingletonCreation(beanName);
            boolean newSingleton = false;

            try {
                // 初始化 bean
                // 这个过程其实就是调用 ObjectFactory#createBean() 方法
                singletonObject = singletonFactory.getObject();
                newSingleton = true;
            } finally {
                // 后置处理，其实就是移除正在创建中的标记
                afterSingletonCreation(beanName);
            }
            // 此时已经正式创建完成，加入缓存中（正是在此处加入到一级缓存的）
            if (newSingleton) {
                addSingleton(beanName, singletonObject);
            }
            return singletonObject;
        }
    }

    protected void addSingleton(String beanName, Object singletonObject) {
        synchronized (this.singletonObjects) {
            // 加入到一级缓存，并删除二、三级缓存
            this.singletonObjects.put(beanName, singletonObject);
            this.singletonFactories.remove(beanName);
            this.earlySingletonObjects.remove(beanName);
            this.registeredSingletons.add(beanName);
        }
    }

    protected void addSingletonFactory(String beanName, ObjectFactory<?> singletonFactory) {
        synchronized (this.singletonObjects) {
            if (!this.singletonObjects.containsKey(beanName)) {
                // 加入到三级缓存，并删除二级缓存
                this.singletonFactories.put(beanName, singletonFactory);
                this.earlySingletonObjects.remove(beanName);
                this.registeredSingletons.add(beanName);
            }
        }
    }

    protected void beforeSingletonCreation(String beanName) {
        if (!this.singletonsCurrentlyInCreation.add(beanName)) {	// 如果添加失败
            throw new BeansException("Singleton bean '" + beanName + "' is currently in creation");		//	抛出异常
        }
    }

    protected void afterSingletonCreation(String beanName) {
        if (!this.singletonsCurrentlyInCreation.remove(beanName)) {	// 如果移除失败
            throw new BeansException("Singleton bean '" + beanName + "' isn't currently in creation");//	抛出异常
        }
    }

    public boolean isSingletonCurrentlyInCreation(String beanName) {
        return this.singletonsCurrentlyInCreation.contains(beanName);
    }

    @Override
    public boolean containsSingleton(String beanName) {
        return false;
    }

    @Override
    public String[] getSingletonNames() {
        return new String[0];
    }

    @Override
    public int getSingletonCount() {
        return 0;
    }

    @Override
    public Object getSingletonMutex() {
        return null;
    }
}
