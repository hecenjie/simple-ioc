package cn.hecenjie.simpleioc.util.xml;

import org.slf4j.Logger;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
/**
 * @author cenjieHo
 * @since 2019/5/20
 */
public class SimpleSaxErrorHandler implements ErrorHandler {

    private final Logger logger;

    public SimpleSaxErrorHandler(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void warning(SAXParseException exception) throws SAXException {
        logger.warn("Ignored XML validation warning", exception);
    }

    @Override
    public void error(SAXParseException exception) throws SAXException {
        throw exception;
    }

    @Override
    public void fatalError(SAXParseException exception) throws SAXException {
        throw exception;
    }
}
