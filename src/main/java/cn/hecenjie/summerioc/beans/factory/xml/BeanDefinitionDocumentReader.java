package cn.hecenjie.summerioc.beans.factory.xml;

import cn.hecenjie.summerioc.beans.factory.BeansException;
import cn.hecenjie.summerioc.beans.factory.support.BeanDefinitionRegistry;
import org.w3c.dom.Document;

/**
 * @author cenjieHo
 * @since 2019/4/24
 */
public interface BeanDefinitionDocumentReader {

    /**
     * 根据获取到的{@link Document}的根节点{@code <beans/>}注册 BeanDefinition
     * @param doc
     * @throws BeansException
     */
    void registerBeanDefinitions(Document doc, BeanDefinitionRegistry registry)
            throws BeansException;

}
