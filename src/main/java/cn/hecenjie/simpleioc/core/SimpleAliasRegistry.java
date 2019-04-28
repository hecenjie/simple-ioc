package cn.hecenjie.simpleioc.core;

import cn.hecenjie.simpleioc.beans.factory.support.DefaultListableBeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 别名注册类的简单实现，{@link DefaultListableBeanFactory}无需覆盖这些方法，直接使用即可
 * @author cenjieHo
 * @since 2019/4/25
 */
public class SimpleAliasRegistry implements AliasRegistry {

    private static final Logger logger = LoggerFactory.getLogger(SimpleAliasRegistry.class);

    /** 记录 alias -> beanName 的映射关系 */
    private final Map<String, String> aliasMap = new ConcurrentHashMap<>(16);

    @Override
    public void registerAlias(String name, String alias) {
        if(alias.equals(name)){	    // 如果alias == name，则去掉（忽略）alias
            this.aliasMap.remove(alias);
        } else{
            // 因为目前没有实现<alias/>标签，所以不存在别名循环指向的问题，不需要检测
            this.aliasMap.put(alias, name);		// 直接将映射关系加入即可
            logger.debug("Alias definition '" + alias + "' registered for name '" + name + "'");
        }
    }

    @Override
    public void removeAlias(String alias) {
        String name = this.aliasMap.remove(alias);
        if (name == null) {
            throw new IllegalStateException("No alias '" + alias + "' registered");
        }
    }

    @Override
    public boolean isAlias(String name) {
        return this.aliasMap.containsKey(name);
    }

    @Override
    public String[] getAliases(String name) {
        List<String> result = new ArrayList<>();
        for(Map.Entry<String, String> entry : aliasMap.entrySet()){
            String entryName = entry.getValue();
            if(name.equals(entryName)){ // 同样只考虑一种别名的情况
                String entryAlias = entry.getKey();
                result.add(entryAlias);
                break;
            }
        }
        return result.toArray(new String[0]);
    }

    /**
     * 循环获取 beanName 的过程，例如，别名A指向名称为B的bean则返回B，若别名A指向别名B，别名B指向名称为C的bean，则返回C
     * @param name
     * @return
     */
    public String canonicalName(String name) {
        String canonicalName = name;
        // Handle aliasing...
        String resolvedName;
        do {	// 循环，从 aliasMap 中，获取到最终的 beanName
            resolvedName = this.aliasMap.get(canonicalName);
            if (resolvedName != null) {
                canonicalName = resolvedName;
            }
        } while (resolvedName != null);
        return canonicalName;
    }
}
