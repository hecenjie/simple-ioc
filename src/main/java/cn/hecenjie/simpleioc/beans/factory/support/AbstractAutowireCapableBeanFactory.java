package cn.hecenjie.simpleioc.beans.factory.support;

import cn.hecenjie.simpleioc.beans.MutablePropertyValues;
import cn.hecenjie.simpleioc.beans.PropertyValue;
import cn.hecenjie.simpleioc.beans.PropertyValues;
import cn.hecenjie.simpleioc.beans.factory.BeansException;
import cn.hecenjie.simpleioc.beans.factory.config.AbstractBeanDefinition;
import cn.hecenjie.simpleioc.beans.factory.config.BeanDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URL;
import java.util.*;

import static cn.hecenjie.simpleioc.beans.factory.config.AbstractBeanDefinition.AUTOWIRE_BY_NAME;
import static cn.hecenjie.simpleioc.beans.factory.config.AbstractBeanDefinition.AUTOWIRE_BY_TYPE;

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
        populateBean(beanName, bd, bean);
        // todo: 调用初始化方法
//        exposedObject = initializeBean(beanName, exposedObject, bd);

        // todo: 注册 bean 的销毁方法
        // 与 InitializingBean 和 init-method 用于对象的自定义初始化工作相似，DisposableBean 和 destroy-method 用于对象的自定义销毁工作，
        // 但是并不是对象完成调用后就会立刻执行销毁方法，而是等到Spring容器关闭的时候才去调用，并且需要我们主动去告知Spring容器，
        // 对于 BeanFactory 容器需要调用 destroySingletons()方法，对于 ApplicationContext 容器需要调用 registerShutdownHook() 方法
//        registerDisposableBeanIfNecessary(beanName, bean, bd);

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

    protected Object instantiateBean(final String beanName, final AbstractBeanDefinition bd) {
        Object beanInstance = getInstantiationStrategy().instantiate(bd, beanName);

        return beanInstance;
    }

    protected InstantiationStrategy getInstantiationStrategy() {
        return this.instantiationStrategy;
    }

    protected void populateBean(String beanName, AbstractBeanDefinition bd, Object bean) {
        // 获取 bean 的属性值
        MutablePropertyValues pvs = (bd.hasPropertyValues() ? bd.getPropertyValues() : null);

        // 根据名称或类型解析依赖，这里不再处理按照构造函数自动装配的模式
        // 该逻辑只会解析依赖，并不会将解析出的依赖立即注入到 bean 对象中,
        // 所有的属性值是在 applyPropertyValues 方法中统一被注入到 bean 对象中的
        int resolvedAutowireMode = bd.getResolvedAutowireMode();
        if (resolvedAutowireMode == AUTOWIRE_BY_NAME || resolvedAutowireMode == AUTOWIRE_BY_TYPE) {
//
//            Field[] fields = bean.getClass().getDeclaredFields(); // todo：这里没考虑从父类继承的属性
//            for(Field field : fields){
//                String propertyName = field.getName();
//                if(pvs.contains(propertyName))
//                    continue;
//                Object propertyType = field.getType();
//                pvs.add(propertyName, propertyType);    // 因为是自动注入，所以将未用<property>声明的属性也加入到pvs中
//            }
//
//            // 根据名称解析依赖
//            if (bd.getResolvedAutowireMode() == AUTOWIRE_BY_NAME) {
//                autowireByName(beanName, bd, bean, pvs);
//            }
//            // 根据类型解析依赖
//            if (bd.getResolvedAutowireMode() == AUTOWIRE_BY_TYPE) {
//                autowireByType(beanName, bd, bean, pvs);
//            }
        }

        if (pvs != null) {
            applyPropertyValues(beanName, bd, bean, pvs);
        }

    }

    protected void applyPropertyValues(String beanName, BeanDefinition bd, Object bean, PropertyValues pvs) {
        if (pvs.isEmpty()) {
            return;
        }

        // 获取对应的解析器
        BeanDefinitionValueResolver valueResolver = new BeanDefinitionValueResolver(this, beanName, bd);

        MutablePropertyValues mpvs = (MutablePropertyValues) pvs;
        List<PropertyValue> original = mpvs.getPropertyValueList();		// 原始类型
        List<PropertyValue> deepCopy = new ArrayList<>(original.size());
        // 遍历属性列表
        for (PropertyValue pv : original) {
            String propertyName = pv.getName();
            Object originalValue = pv.getValue();	// 原始的属性值，即转换之前的属性值

            // 将 ref 解析为具体的对象，将 <list> 标签转换为 List 对象等。
            // 对于 int 类型的配置，这里并未做转换，所以还是字符串,
            // 除了解析上面几种类型，该方法还会解析 <set/>、<map/>、<array/> 等集合配置
            Object resolvedValue = valueResolver.resolveValueIfNecessary(pv, originalValue);
            Object convertedValue = resolvedValue;	// 转换之后的属性值

            // todo: 对属性值的类型进行转换，比如将 String 类型的属性值"123"转换为 Integer 类型的 123
//          convertedValue = convertForProperty(resolvedValue, propertyName, bw, converter);

            pv.setConvertedValue(convertedValue);
            deepCopy.add(pv);

            // 进行属性依赖注入，依赖注入真真正正实现在这
            try {
                setPropertyValues(new MutablePropertyValues(deepCopy), bean);
            } catch (Exception ex) {
                throw new BeansException("Error setting '" + beanName + "' property values", ex);
            }
        }
    }

    public void setPropertyValues(PropertyValues pvs, Object bean) throws IllegalAccessException {
        List<PropertyValue> propertyValues = ((MutablePropertyValues) pvs).getPropertyValueList();
        for (PropertyValue pv : propertyValues) {
            Object propertyName = pv.getName();
            Object convertedValue = pv.getConvertedValue();
            Field[] fields = bean.getClass().getFields();
            for(Field field : fields){  // Spring是使用setter完成注入的，这里直接使用了Field反射注入
                if(field.getName().equals(propertyName)){
                    field.setAccessible(true); //设置些属性是可以访问的
                    field.set(bean, convertedValue);
                }
            }
        }

    }


//    protected void autowireByName(
//            String beanName, AbstractBeanDefinition bd, Object bean, MutablePropertyValues pvs) {
//        // 获取 Bean 对象中的非简单属性，即类型为对象类型的属性，如下为简单属性：
//        // String、Enum、Date、URI/URL、Number的继承类如Integer/Long、byte/short/int等基本类型、Locale、以上所有类型的数组形式
//        String[] propertyNames = unsatisfiedNonSimpleProperties(bd, bean);
//        // 遍历 propertyNames 数组
//        for (String propertyName : propertyNames) {
//            // 如果容器中包含指定名称的 bean
//            if (containsBean(propertyName)) {
//                // 递归初始化相关 bean
//                Object dbean = getBean(propertyName);
//                // 将递归获取到的 bean 存入到属性值列表 pvs 中
//                pvs.add(propertyName, bean);
//                // 注册依赖
//                registerDependentBean(propertyName, beanName);
//                logger.debug("Added autowiring by name from bean name '" + beanName +
//                        "' via property '" + propertyName + "' to bean named '" + propertyName + "'");
//            } else{	    // 如果容器中不包含指定名称的 bean，抛出异常
//                logger.error("Not autowiring property '" + propertyName + "' of bean '" + beanName +
//                        "' by name: no matching bean found");
//            }
//        }
//    }

//    protected String[] unsatisfiedNonSimpleProperties(AbstractBeanDefinition bd, Object bean) {
//        // 创建 result 集合
//        Set<String> result = new TreeSet<>();
//        PropertyValues pvs = bd.getPropertyValues();
//    }
//
//    public static boolean isSimpleProperty(Class<?> clazz) {
//        return isSimpleValueType(clazz) || (clazz.isArray() && isSimpleValueType(clazz.getComponentType()));
//    }
//
//    public static boolean isSimpleValueType(Class<?> clazz) {
//        return (clazz.isPrimitive() ||
//                Enum.class.isAssignableFrom(clazz) ||
//                CharSequence.class.isAssignableFrom(clazz) ||
//                Number.class.isAssignableFrom(clazz) ||
//                Date.class.isAssignableFrom(clazz) ||
//                URI.class == clazz || URL.class == clazz ||
//                Locale.class == clazz || Class.class == clazz);
//    }

}
