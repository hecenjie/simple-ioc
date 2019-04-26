package cn.hecenjie.simpleioc.beans;

/**
 * @author cenjieHo
 * @since 2019/4/26
 */
public interface PropertyValues {

    PropertyValue[] getPropertyValues();

    PropertyValue getPropertyValue(String propertyName);

    boolean isEmpty();

}
