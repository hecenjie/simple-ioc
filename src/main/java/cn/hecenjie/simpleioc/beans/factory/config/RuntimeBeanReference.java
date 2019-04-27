package cn.hecenjie.simpleioc.beans.factory.config;

/**
 * @author cenjieHo
 * @since 2019/4/27
 */
public class RuntimeBeanReference implements BeanReference {

    private final String beanName;

    private Object source;

    public RuntimeBeanReference(String beanName) {
        this.beanName = beanName;
    }

    @Override
    public String getBeanName() {
        return null;
    }

    public void setSource(Object source) {
        this.source = source;
    }

    public Object getSource() {
        return this.source;
    }
}
