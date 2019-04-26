package cn.hecenjie.simpleioc.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cenjieHo
 * @since 2019/4/26
 */
public class MutablePropertyValues implements PropertyValues {

    private final List<PropertyValue> propertyValueList;

    public MutablePropertyValues() {
        this.propertyValueList = new ArrayList<>(0);
    }

    @Override
    public PropertyValue[] getPropertyValues() {
        return this.propertyValueList.toArray(new PropertyValue[0]);
    }

    @Override
    public PropertyValue getPropertyValue(String propertyName) {
        for (PropertyValue pv : this.propertyValueList) {
            if (pv.getName().equals(propertyName)) {
                return pv;
            }
        }
        return null;
    }

    @Override
    public boolean isEmpty() {
        return this.propertyValueList.isEmpty();
    }
}
