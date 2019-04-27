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

    public MutablePropertyValues(List<PropertyValue> propertyValueList) {
        this.propertyValueList =
                (propertyValueList != null ? propertyValueList : new ArrayList<>());
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

    public boolean contains(String propertyName) {
        return getPropertyValue(propertyName) != null;
    }

    public MutablePropertyValues add(String propertyName, Object propertyValue) {
        addPropertyValue(new PropertyValue(propertyName, propertyValue));
        return this;
    }

    public MutablePropertyValues addPropertyValue(PropertyValue pv) {
        for (int i = 0; i < this.propertyValueList.size(); i++) {
            PropertyValue currentPv = this.propertyValueList.get(i);
            if (currentPv.getName().equals(pv.getName())) {
                setPropertyValueAt(pv, i);
                return this;
            }
        }
        this.propertyValueList.add(pv);
        return this;
    }

    public void setPropertyValueAt(PropertyValue pv, int i) {
        this.propertyValueList.set(i, pv);
    }

    public List<PropertyValue> getPropertyValueList() {
        return this.propertyValueList;
    }
}
