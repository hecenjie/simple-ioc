package cn.hecenjie.simpleioc.core;

/**
 * 管理别名的通用接口
 *
 * @author cenjieHo
 * @since 2019/4/23
 */
public interface AliasRegistry {

    void registerAlias(String name, String alias);

    void removeAlias(String alias);

    boolean isAlias(String name);

    String[] getAliases(String name);
}
