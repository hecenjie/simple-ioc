package cn.hecenjie.simpleioc.beans;

import cn.hecenjie.simpleioc.core.AttributeAccessor;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author cenjieHo
 * @since 2019/4/26
 */
public class PropertyValue implements AttributeAccessor {

    private final String name;

    private final Object value;

    private final Map<String, Object> attributes = new LinkedHashMap<>();

    public PropertyValue(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public Object getValue() {
        return this.value;
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
}
