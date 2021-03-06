package cn.hecenjie.simpleioc.beans.factory.xml;

import cn.hecenjie.simpleioc.beans.PropertyValue;
import cn.hecenjie.simpleioc.beans.factory.BeansException;
import cn.hecenjie.simpleioc.beans.factory.config.*;
import cn.hecenjie.simpleioc.beans.factory.support.MethodOverrides;
import cn.hecenjie.simpleioc.core.AttributeAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 解析{@link Element}的一个委托类
 *
 * @author cenjieHo
 * @since 2019/4/24
 */
public class BeanDefinitionParserDelegate {

    private static final Logger logger = LoggerFactory.getLogger(BeanDefinitionParserDelegate.class);

    public static final String AUTOWIRE_BY_NAME_VALUE = "byName";

    public static final String AUTOWIRE_BY_TYPE_VALUE = "byType";

    public static final String AUTOWIRE_CONSTRUCTOR_VALUE = "constructor";

    public static final String ID_ATTRIBUTE = "id";

    public static final String NAME_ATTRIBUTE = "name";

    public static final String CLASS_ATTRIBUTE = "class";

    public static final String SCOPE_ATTRIBUTE = "scope";

    public static final String LAZY_INIT_ATTRIBUTE = "lazy-init";

    public static final String AUTOWIRE_ATTRIBUTE = "autowire";

    public static final String INIT_METHOD_ATTRIBUTE = "init-method";

    public static final String DESTROY_METHOD_ATTRIBUTE = "destroy-method";

    public static final String FACTORY_METHOD_ATTRIBUTE = "factory-method";

    public static final String FACTORY_BEAN_ATTRIBUTE = "factory-bean";

    public static final String PROPERTY_ELEMENT = "property";

    public static final String REF_ATTRIBUTE = "ref";

    public static final String VALUE_ATTRIBUTE = "value";


    /** 存储已使用的 beanName 和 alias */
    private final Set<String> usedNames = new HashSet<>();

    public BeanDefinitionHolder parseBeanDefinitionElement(Element ele) {
        return parseBeanDefinitionElement(ele, null);
    }

    /**
     * 此方法主要针对 beanName 进行一些操作，然后将属性的解析交给另一个方法来实现，
     * 得到一个 BeanDefinition 后将其与 beanName、aliases 一起封装成一个 BeanDefinitionHolder 返回
     * @param ele
     * @param containingBean
     * @return
     */
    public BeanDefinitionHolder parseBeanDefinitionElement(Element ele, BeanDefinition containingBean) {
        String id = ele.getAttribute(ID_ATTRIBUTE);			// 解析id属性
        String nameAttr = ele.getAttribute(NAME_ATTRIBUTE);	// 解析name属性
        logger.debug("This <bean/> element's id is '" + id + "'");
        logger.debug("This <bean/> element's name is '" + nameAttr + "'");

        List<String> aliases = new ArrayList<>();
        if (nameAttr != null && nameAttr.length() != 0) {
            aliases.add(nameAttr);  // 将name属性的值作为别名，这里不考虑同时有多个别名的情况
        }

        // 优先使用id作为 beanName
        String beanName = id;
        // 如果id为空并且 aliases 不为空，那么使用 aliases 的第一个元素作为 beanName 并将其移除 aliases 集合
        if ((beanName == null || beanName.length() == 0) && !aliases.isEmpty()) {
            beanName = aliases.remove(0);
            logger.debug("No XML 'id' specified - using '" + beanName +
                    "' as bean name and " + aliases + " as aliases, now " + aliases.size() + " aliases exist");
        }

        if (containingBean == null) {
            // 检查 beanName 的唯一性，如果 beanName 或 aliases 已经存在了，那么将抛出异常
            checkNameUniqueness(beanName, aliases, ele);
        }

        // beanName 已经搞定了，开始解析属性，构造 AbstractBeanDefinition 对象
        AbstractBeanDefinition beanDefinition = parseBeanDefinitionElement(ele, beanName, containingBean);

        if (beanDefinition != null) {
            // 创建 BeanDefinitionHolder 对象
            String[] aliasesArray = aliases.toArray(new String[0]);
            return new BeanDefinitionHolder(beanDefinition, beanName, aliasesArray);
        }

        return null;
    }

    /**
     * 该方法完成属性的解析，将它们都设置到 BeanDefinition 中去
     * @param ele
     * @param beanName
     * @param containingBean
     * @return
     */
    public AbstractBeanDefinition parseBeanDefinitionElement(Element ele, String beanName, BeanDefinition containingBean) {
        // 解析 class 属性
        String className = null;
        if (ele.hasAttribute(CLASS_ATTRIBUTE)) {
            className = ele.getAttribute(CLASS_ATTRIBUTE).trim();
        }

        try {
            // 创建用于承载属性的 GenericBeanDefinition 实例
            AbstractBeanDefinition bd = new GenericBeanDefinition();
            bd.setBeanClassName(className);

            // todo：以下许多解析方法并未真正的实现
            parseBeanDefinitionAttributes(ele, beanName, containingBean, bd);	// 解析 bean 的各种属性
            parseMetaElements(ele, bd);		            // 解析元数据 <meta />
            parseLookupOverrideSubElements(ele, bd.getMethodOverrides());		// 解析 lookup-method 子元素 <lookup-method/>
            parseReplacedMethodSubElements(ele, bd.getMethodOverrides());		// 解析 replaced-method 子元素 <replaced-method/>
            parseConstructorArgElements(ele, bd);		// 解析构造函数参数 <constructor-arg/>
            parsePropertyElements(ele, bd);				// 解析 property 子元素 <property />
            parseQualifierElements(ele, bd);			// 解析 qualifier 子元素 <qualifier />
            return bd;
        } catch (Exception ex) {
            throw new BeansException("Unexpected failure during bean definition parsing");
        }
    }


    /**
     * beanName、alias 的唯一性检查，如果唯一则添加到记录中
     * @param beanName
     * @param aliases
     * @param beanElement
     * @throws BeansException
     */
    private void checkNameUniqueness(String beanName, List<String> aliases, Element beanElement) throws BeansException {
        // 如果该 beanName 已经被使用，则抛出异常
        if (beanName != null && beanName.length() != 0 && this.usedNames.contains(beanName)) {
            throw new BeansException(
                    "Bean name '" + beanName + "' is already used in '" + beanElement.getTagName() +  "' element");
        }
        // 如果该 aliases 不为空，则遍历所有别名，如果发现某个别名已经被使用，则抛出异常
        if (aliases != null && aliases.size() != 0) {
            for(String alias : aliases){
                if(this.usedNames.contains(alias)) {
                    throw new BeansException(
                            "Alias '" + alias + "' is already used in '" + beanElement.getTagName() + "' element");
                }
            }
        }

        this.usedNames.add(beanName);
        this.usedNames.addAll(aliases);
    }

    public AbstractBeanDefinition parseBeanDefinitionAttributes(Element ele, String beanName,
                                                                BeanDefinition containingBean, AbstractBeanDefinition bd) {
        // 解析 scope 属性
        if (ele.hasAttribute(SCOPE_ATTRIBUTE)) {
            bd.setScope(ele.getAttribute(SCOPE_ATTRIBUTE));
        }

        // 解析 lazy-init 属性
        if (ele.hasAttribute(LAZY_INIT_ATTRIBUTE)) {
            String lazyInit = ele.getAttribute(LAZY_INIT_ATTRIBUTE);
            bd.setLazyInit(Boolean.valueOf(lazyInit));
        }

        // 解析 autowire 属性
        String autowire = ele.getAttribute(AUTOWIRE_ATTRIBUTE);
        bd.setAutowireMode(getAutowireMode(autowire));

        // 解析 init-method 属性
        if (ele.hasAttribute(INIT_METHOD_ATTRIBUTE)) {
            String initMethodName = ele.getAttribute(INIT_METHOD_ATTRIBUTE);
            bd.setInitMethodName(initMethodName);
        }

        // 解析 destroy-method 属性
        if (ele.hasAttribute(DESTROY_METHOD_ATTRIBUTE)) {
            String destroyMethodName = ele.getAttribute(DESTROY_METHOD_ATTRIBUTE);
            bd.setDestroyMethodName(destroyMethodName);
        }

        // 解析 factory-method 属性
        if (ele.hasAttribute(FACTORY_METHOD_ATTRIBUTE)) {
            bd.setFactoryMethodName(ele.getAttribute(FACTORY_METHOD_ATTRIBUTE));
        }

        // 解析 factory-bean 属性
        if (ele.hasAttribute(FACTORY_BEAN_ATTRIBUTE)) {
            bd.setFactoryBeanName(ele.getAttribute(FACTORY_BEAN_ATTRIBUTE));
        }

        // ...
        // 省略了 depends-on、autowire-candidate、primary 等属性

        return bd;
    }

    public int getAutowireMode(String attValue) {
        String att = attValue;
        int autowire = AbstractBeanDefinition.AUTOWIRE_NO;
        if (AUTOWIRE_BY_NAME_VALUE.equals(att)) {
            autowire = AbstractBeanDefinition.AUTOWIRE_BY_NAME;
        }
        else if (AUTOWIRE_BY_TYPE_VALUE.equals(att)) {
            autowire = AbstractBeanDefinition.AUTOWIRE_BY_TYPE;
        }
        else if (AUTOWIRE_CONSTRUCTOR_VALUE.equals(att)) {
            autowire = AbstractBeanDefinition.AUTOWIRE_CONSTRUCTOR;
        }
        return autowire;
    }

    public void parseMetaElements(Element ele, AttributeAccessor attributeAccessor) {
        // 暂未实现
    }

    public void parseLookupOverrideSubElements(Element beanEle, MethodOverrides overrides) {
        // 暂未实现
    }

    public void parseReplacedMethodSubElements(Element beanEle, MethodOverrides overrides) {
        // 暂未实现
    }

    public void parseConstructorArgElements(Element beanEle, BeanDefinition bd) {
        // 暂未实现
    }

    public void parsePropertyElements(Element beanEle, BeanDefinition bd) {
        NodeList nl = beanEle.getChildNodes();
        // 遍历<bean>标签下的所有子元素
        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            if (node instanceof Element && PROPERTY_ELEMENT.equals(node.getNodeName())) {
                // 如果该 Node 类型为 Element 且名为 property
                parsePropertyElement((Element) node, bd);
            }
        }
    }

    public void parsePropertyElement(Element ele, BeanDefinition bd) {
        String propertyName = ele.getAttribute(NAME_ATTRIBUTE);		// 获取 name 属性值
        if(propertyName == null || propertyName.length() == 0){
            logger.error("Element '" + ele.getTagName() + "' is null");
            return;
        }
        if (bd.getPropertyValues().contains(propertyName)) {    // 如果存在相同的 name，报错
            logger.error("Multiple 'property' definitions for property '" + propertyName + "'");
            return;
        }
        Object val = parsePropertyValue(ele, bd, propertyName);		// 解析属性值
        PropertyValue pv = new PropertyValue(propertyName, val);	// 创建 PropertyValue 对象
        bd.getPropertyValues().addPropertyValue(pv);				// 添加到 PropertyValue 集合中
    }

    public Object parsePropertyValue(Element ele, BeanDefinition bd, String propertyName) {
        NodeList nl = ele.getChildNodes();

        // 省略了单独的<value>、<ref>、<list>子节点

        boolean hasRefAttribute = ele.hasAttribute(REF_ATTRIBUTE);		// 是否有 ref 属性
        boolean hasValueAttribute = ele.hasAttribute(VALUE_ATTRIBUTE);	// 是否有 value 属性
        if (hasRefAttribute && hasValueAttribute) {	// 不允许同时存在 ref 和 value 属性
            logger.error("<property> element for property '" + propertyName +
                    "' is only allowed to contain either 'ref' attribute OR 'value' attribute OR sub-element");
        }

        if (hasRefAttribute) {             // 如果存在 ref 属性
            String refName = ele.getAttribute(REF_ATTRIBUTE);
            if(refName == null || refName.length() == 0) {
                logger.error("<property> element for property '" + propertyName + "' contains empty 'ref' attribute");
            }
            // 将 ref 属性值构造为 RuntimeBeanReference 实例对象
            RuntimeBeanReference ref = new RuntimeBeanReference(refName);
            return ref;
        } else if (hasValueAttribute) {    // 如果存在 value 属性
            String valueHolder = ele.getAttribute(VALUE_ATTRIBUTE);
            return valueHolder;
        } else {
            // Neither child element nor "ref" or "value" attribute found.
            // 既没发现子元素也没发现ref或value，那么这时候将报错
            logger.error("<property> element for property '" + propertyName + "' must specify a ref or value");
            return null;
        }
    }

    public void parseQualifierElements(Element beanEle, AbstractBeanDefinition bd) {
        // 暂未实现
    }
}
