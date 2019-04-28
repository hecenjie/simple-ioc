package cn.hecenjie.simpleioc.beans.factory.config;

/**
 * @author cenjieHo
 * @since 2019/4/27
 */
public class TypedStringValue {

    private String value;

    private volatile Object targetType;

    private Object source;

    private String specifiedTypeName;

    private volatile boolean dynamic;

}
