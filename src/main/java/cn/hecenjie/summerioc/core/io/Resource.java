package cn.hecenjie.summerioc.core.io;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;

/**
 * 对所有资源的统一抽象，定义一些通用的方法，由子类{@link AbstractResource}提供统一的默认实现
 *
 * @author cenjieHo
 * @since 2019/4/23
 */
public interface Resource extends InputStreamSource {

    /**
     * @return 该资源是否存在
     */
    boolean exists();

    /**
     * @return 该资源是否可读
     */
    boolean isReadable();

    /**
     * @return 是否打开
     */
    boolean isOpen();

    /**
     * @return 是否为 File
     */
    boolean isFile();

    /**
     * @return 资源的 URL 句柄
     * @throws IOException
     */
    URL getURL() throws IOException;

    /**
     * @return 资源的 URI 句柄
     * @throws IOException
     */
    URI getURI() throws IOException;

    /**
     * @return 资源的 File 句柄
     * @throws IOException
     */
    File getFile() throws IOException;

    /**
     * @return 资源的大小
     * @throws IOException
     */
    long contentLength() throws IOException;

    /**
     * 根据该资源的相对路径创建新资源
     * @param relativePath 相对于当前资源的路径
     * @return 创建的新资源
     * @throws IOException
     */
    Resource createRelative(String relativePath) throws IOException;

    /**
     * @return 文件名，如果没有则返回 null
     */
    String getFilename();

    /**
     * @return 资源的描述
     */
    String getDescription();
}
