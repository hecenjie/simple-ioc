package cn.hecenjie.summerioc.beans.factory.config;

import cn.hecenjie.summerioc.beans.factory.support.MethodOverrides;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author cenjieHo
 * @since 2019/4/24
 */
public abstract class AbstractBeanDefinition implements BeanDefinition {

    private final Map<String, Object> attributes = new LinkedHashMap<>();

    /** 表示不使用自动装配 */
    public static final int AUTOWIRE_NO = 0;

    /** 表示按名字自动装配 Bean 属性 */
    public static final int AUTOWIRE_BY_NAME = 1;

    /** 表示按类型自动装配 Bean 属性 */
    public static final int AUTOWIRE_BY_TYPE = 2;

    /** 同样表示按类型自动装配 Bean 属性，只不过指的是构造函数的情况 */
    public static final int AUTOWIRE_CONSTRUCTOR = 3;

    private volatile Object beanClass;

    private String scope = "singleton";

    private boolean lazyInit = false;

    private MethodOverrides methodOverrides;

    private int autowireMode = AUTOWIRE_NO;

    private String initMethodName;

    private String destroyMethodName;

    private String factoryMethodName;

    private String factoryBeanName;

    public void setAutowireMode(int autowireMode) {
        this.autowireMode = autowireMode;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public void setLazyInit(boolean lazyInit) {
        this.lazyInit = lazyInit;
    }

    public void setInitMethodName(String initMethodName) {
        this.initMethodName = initMethodName;
    }

    public void setDestroyMethodName(String destroyMethodName) {
        this.destroyMethodName = destroyMethodName;
    }

    public void setFactoryMethodName(String factoryMethodName) {
        this.factoryMethodName = factoryMethodName;
    }

    public void setFactoryBeanName(String factoryBeanName) {
        this.factoryBeanName = factoryBeanName;
    }

    public MethodOverrides getMethodOverrides() {
        if (this.methodOverrides == null) {
            this.methodOverrides = new MethodOverrides();
        }
        return this.methodOverrides;
    }

    @Override
    public void setAttribute(String name, Object value) {
        if (value != null) {
            this.attributes.put(name, value);
        } else {
            removeAttribute(name);
        }
    }

    @Override
    public Object getAttribute(String name) {
        return this.attributes.get(name);
    }

    @Override
    public Object removeAttribute(String name) {
        return this.attributes.remove(name);
    }

    @Override
    public boolean hasAttribute(String name) {
        return this.attributes.containsKey(name);
    }

    @Override
    public String[] attributeNames() {
        return this.attributes.keySet().toArray(new String[0]);
    }

    public void setBeanClassName(String beanClassName) {
        this.beanClass = beanClassName;
    }
}
