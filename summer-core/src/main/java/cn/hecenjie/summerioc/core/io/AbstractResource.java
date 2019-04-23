package cn.hecenjie.summerioc.core.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

/**
 * {@link Resource}接口的默认实现
 *
 * @author cenjieHo
 * @since 2019/4/23
 */
public class AbstractResource implements Resource {

    /**
     * 判断资源是否存在，若产生异常则关闭对应的流
     * @return
     */
    public boolean exists() {
        try {
            return getFile().exists();
        }
        catch (IOException ex) {
            try {
                getInputStream().close();
                return true;
            }
            catch (Throwable isEx) {
                return false;
            }
        }
    }

    public boolean isReadable() {
        return true;
    }

    public boolean isOpen() {
        return false;
    }

    public boolean isFile() {
        return false;
    }

    public URL getURL() throws IOException {
        throw new FileNotFoundException(getDescription() + " cannot be resolved to URL");   // 交由子类实现
    }

    public URI getURI() throws IOException {
        throw new FileNotFoundException(getDescription() + " cannot be resolved to URI");   // 交由子类实现
    }

    public File getFile() throws IOException {
        throw new FileNotFoundException(getDescription() + " cannot be resolved to absolute file path");
    }

    public long contentLength() throws IOException {
        InputStream is = getInputStream();  // 获取 InputStream
        try {
            long size = 0;
            byte[] buf = new byte[256];
            int read;
            while ((read = is.read(buf)) != -1) {
                size += read;
            }
            return size;
        }
        finally {
            try {
                is.close();
            }
            catch (IOException ex) {
            }
        }
    }

    public Resource createRelative(String relativePath) throws IOException {
        throw new FileNotFoundException("Cannot create a relative resource for " + getDescription());
    }

    public String getFilename() {
        return null;
    }

    public String getDescription() {
        return null;
    }

    public InputStream getInputStream() throws IOException {
        return null;
    }

    @Override
    public String toString() {
        return getDescription();
    }
}
