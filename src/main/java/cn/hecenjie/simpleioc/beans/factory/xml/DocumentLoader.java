package cn.hecenjie.simpleioc.beans.factory.xml;

import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;

/**
 * @author cenjieHo
 * @since 2019/4/24
 */
public interface DocumentLoader {

    /**
     * 从{@link InputSource source}解析一个{@link Document document}实例
     *
     * @param inputSource
     * @param entityResolver
     * @param errorHandler
     * @param validationMode
     * @param namespaceAware
     * @return
     * @throws Exception
     */
    Document loadDocument(
            InputSource inputSource, EntityResolver entityResolver,
            ErrorHandler errorHandler, int validationMode, boolean namespaceAware)
            throws Exception;
}
