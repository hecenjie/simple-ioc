package cn.hecenjie.summerioc.beans.factory.config;

/**
 * @author cenjieHo
 * @since 2019/4/25
 */
public interface SingletonBeanRegistry {

    void registerSingleton(String beanName, Object singletonObject);

    Object getSingleton(String beanName);

    boolean containsSingleton(String beanName);

    String[] getSingletonNames();

    int getSingletonCount();

    Object getSingletonMutex();
}
