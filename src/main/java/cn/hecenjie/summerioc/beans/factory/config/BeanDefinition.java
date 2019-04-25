package cn.hecenjie.summerioc.beans.factory.config;

import cn.hecenjie.summerioc.core.AttributeAccessor;

/**
 * @author cenjieHo
 * @since 2019/4/24
 */
public interface BeanDefinition extends AttributeAccessor {

    String SCOPE_SINGLETON = "singleton";

    String SCOPE_PROTOTYPE = "prototype";

    void setBeanClass(Object beanClass);

    void setBeanClassName(String beanClassName);

    String getBeanClassName();

    void setScope(String scope);

    String getScope();

    boolean isSingleton();

    boolean isPrototype();

}
