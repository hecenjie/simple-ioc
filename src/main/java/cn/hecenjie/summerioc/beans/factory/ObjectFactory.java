package cn.hecenjie.summerioc.beans.factory;

/**
 * @author cenjieHo
 * @since 2019/4/25
 */
public interface ObjectFactory<T> {

    T getObject() throws BeansException;

}
