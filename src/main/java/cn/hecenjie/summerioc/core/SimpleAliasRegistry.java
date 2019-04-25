package cn.hecenjie.summerioc.core;

import cn.hecenjie.summerioc.beans.factory.BeansException;
import cn.hecenjie.summerioc.beans.factory.support.DefaultListableBeanFactory;
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
}
