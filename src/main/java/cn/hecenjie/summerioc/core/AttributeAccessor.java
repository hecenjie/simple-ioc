package cn.hecenjie.summerioc.core;

/**
 * 定义了设置和访问属性的通用接口
 *
 * @author cenjieHo
 * @since 2019/4/24
 */
public interface AttributeAccessor {

    /** 设置某个属性值 */
    void setAttribute(String name, Object value);

    /** 获取某个属性值 */
    Object getAttribute(String name);

    /** 删除某个属性值 */
    Object removeAttribute(String name);

    /** 判断某个属性值是否存在 */
    boolean hasAttribute(String name);

    /** 返回所有属性名 */
    String[] attributeNames();

}
