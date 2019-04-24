package cn.hecenjie.summerioc.beans.factory.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * @author cenjieHo
 * @since 2019/4/24
 */
public class DefaultDocumentLoader implements DocumentLoader {

    private final static Logger logger = LoggerFactory.getLogger(DefaultDocumentLoader.class);

    /**
     * JAXP attribute used to configure the schema language for validation.
     */
    private static final String SCHEMA_LANGUAGE_ATTRIBUTE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";

    /**
     * JAXP attribute value indicating the XSD schema language.
     */
    private static final String XSD_SCHEMA_LANGUAGE = "http://www.w3.org/2001/XMLSchema";

    @Override
    public Document loadDocument(InputSource inputSource, EntityResolver entityResolver, ErrorHandler errorHandler, int validationMode, boolean namespaceAware) throws Exception {
        // 创建 DocumentBuilderFactory
        DocumentBuilderFactory factory = createDocumentBuilderFactory(validationMode, namespaceAware);
        // 创建 DocumentBuilder
        DocumentBuilder builder = createDocumentBuilder(factory, entityResolver, errorHandler);
        // 由上面创建好的 DocumentBuilder 解析 inputSource 返回 Document
        return builder.parse(inputSource);
    }

    protected DocumentBuilderFactory createDocumentBuilderFactory(int validationMode, boolean namespaceAware)
            throws ParserConfigurationException {

        // 创建 DocumentBuilderFactory
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        // 设置命名空间支持
        factory.setNamespaceAware(namespaceAware);

        if (validationMode != XmlBeanDefinitionReader.VALIDATION_NONE) {		//如果验证模式不为“不验证”
            factory.setValidating(true);										//开启验证
            if (validationMode == XmlBeanDefinitionReader.VALIDATION_XSD) {	    //如果验证模式为XSD
                factory.setNamespaceAware(true);	//强制设置命名空间支持
                try {
                    factory.setAttribute(SCHEMA_LANGUAGE_ATTRIBUTE, XSD_SCHEMA_LANGUAGE);
                } catch (IllegalArgumentException ex) {
                    ParserConfigurationException pcex = new ParserConfigurationException(
                            "Unable to validate using XSD: Your JAXP provider [" + factory +
                                    "] does not support XML Schema. Are you running on Java 1.4 with Apache Crimson? " +
                                    "Upgrade to Apache Xerces (or Java 1.5) for full XSD support.");
                    pcex.initCause(ex);
                    throw pcex;
                }
            }
        }
        //返回创建好的DocumentBuilderFactory
        return factory;
    }

    protected DocumentBuilder createDocumentBuilder(DocumentBuilderFactory factory,
                                                    EntityResolver entityResolver, ErrorHandler errorHandler)
            throws ParserConfigurationException {
        // 由之前创建好的DocumentBuilderFactory生产出DocumentBuilder
        DocumentBuilder docBuilder = factory.newDocumentBuilder();
        if (entityResolver != null) {
            // 设置 EntityResolver 属性
            docBuilder.setEntityResolver(entityResolver);
        }
        if (errorHandler != null) {
            // 设置 ErrorHandler 属性
            docBuilder.setErrorHandler(errorHandler);
        }
        // 返回创建好的DocumentBuilder
        return docBuilder;
    }
}
