package cn.hecenjie.summerioc.beans.factory.xml;

import cn.hecenjie.summerioc.beans.factory.BeansException;
import cn.hecenjie.summerioc.beans.factory.config.BeanDefinitionHolder;
import cn.hecenjie.summerioc.beans.factory.support.BeanDefinitionRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author cenjieHo
 * @since 2019/4/24
 */
public class DefaultBeanDefinitionDocumentReader implements BeanDefinitionDocumentReader {

    private static final Logger logger = LoggerFactory.getLogger(DefaultBeanDefinitionDocumentReader.class);

    public static final String BEAN_ELEMENT = "bean";

    private BeanDefinitionRegistry registry;

//    public static final String IMPORT_ELEMENT = "import";
//
//    public static final String ALIAS_ELEMENT = "alias";

//    public static final String NESTED_BEANS_ELEMENT = "beans";

    @Override
    public void registerBeanDefinitions(Document doc, BeanDefinitionRegistry registry) throws BeansException {
        this.registry = registry;
        Element root = doc.getDocumentElement();	// 获得 XML Document Root Element
        logger.debug("XML Document Root Element: " + root.getTagName());
        doRegisterBeanDefinitions(root);            // 真正的注册逻辑
    }

    private void doRegisterBeanDefinitions(Element root) {
        BeanDefinitionParserDelegate delegate = new BeanDefinitionParserDelegate();
        // 解析前处理，空，交由子类实现
        preProcessXml(root);
        // 解析，委托给 BeanDefinitionParserDelegate 类来完成
        parseBeanDefinitions(root, delegate);
        // 解析后处理，空，交由子类实现
        postProcessXml(root);
    }

    /**
     * 解析每一个{@link Element}
     * @param root 根节点{@code <beans/>
     * @param delegate 负责解析的一个委托类
     */
    private void parseBeanDefinitions(Element root, BeanDefinitionParserDelegate delegate) {
        NodeList nl = root.getChildNodes();
        // 遍历子节点
        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            if(node instanceof Element) {
                Element ele = (Element) node;
                parseDefaultElement(ele, delegate);
            }
        }
    }

    /**
     * 解析{@code <bean/>}标签（Spring 还解析 import、alias 与嵌套的 beans 标签，这里将其省去）
     * @param ele
     * @param delegate
     */
    private void parseDefaultElement(Element ele, BeanDefinitionParserDelegate delegate) {
        if (BEAN_ELEMENT.equals(ele.getNodeName())) {   // 如果是 <bean/> 标签
            BeanDefinitionHolder bdHolder = delegate.parseBeanDefinitionElement(ele);   // 委托给 BeanDefinitionParserDelegate 解析
            registerBeanDefinition(bdHolder, this.registry);
        }
    }

    public static void registerBeanDefinition(BeanDefinitionHolder definitionHolder, BeanDefinitionRegistry registry)
            throws BeansException {
        // 注册 beanName
        String beanName = definitionHolder.getBeanName();
        registry.registerBeanDefinition(beanName, definitionHolder.getBeanDefinition());

        // 注册 alias，建立 alias 和 beanName 之间的映射
        String[] aliases = definitionHolder.getAliases();
        if (aliases != null) {
            for (String alias : aliases) {
                registry.registerAlias(beanName, alias);
            }
        }
        // 到这里beanName已经成功注册，并且alias与beanName之间的映射关系也建立完毕，下一步则是等待初始化使用了
    }

    private void preProcessXml(Element root) {
    }

    private void postProcessXml(Element root) {
    }
}
