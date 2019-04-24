package cn.hecenjie.summerioc.core.io.support;

import cn.hecenjie.summerioc.core.io.InputStreamSource;
import cn.hecenjie.summerioc.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * @author cenjieHo
 * @since 2019/4/24
 */
public class EncodedResource implements InputStreamSource {

    /** 持有的资源抽象 */
    private final Resource resource;

    /** 编码 */
    private final String encoding;

    /** 字符集 */
    private final Charset charset;

    public InputStream getInputStream() throws IOException {
        return this.resource.getInputStream();
    }

    private EncodedResource(Resource resource, String encoding, Charset charset) {
        this.resource = resource;
        this.encoding = encoding;
        this.charset = charset;
    }

    public EncodedResource(Resource resource, String encoding) {
        this(resource, encoding, null);
    }

    public EncodedResource(Resource resource, Charset charset) {
        this(resource, null, charset);
    }

    public EncodedResource(Resource resource) {
        this(resource, null, null);
    }

    public String getEncoding() {
        return encoding;
    }

    public Charset getCharset() {
        return charset;
    }

    public Resource getResource() {
        return resource;
    }
}
