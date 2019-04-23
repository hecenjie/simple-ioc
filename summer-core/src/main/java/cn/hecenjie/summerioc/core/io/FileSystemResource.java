package cn.hecenjie.summerioc.core.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;

/**
 * 对{@link java.io.File}类型资源的封装
 *
 * @author cenjieHo
 * @since 2019/4/23
 */
public class FileSystemResource extends AbstractResource{

    private final File file;

    private final String path;

    public FileSystemResource(File file) {
        this.file = file;
        this.path = file.getAbsolutePath();
    }

    public FileSystemResource(String path) {
        this.file = new File(path);
        this.path = path;
    }

    public final String getPath() {
        return this.path;
    }

    public boolean exists() {
        return this.file.exists();
    }

    public boolean isReadable() {
        return (this.file.canRead() && !this.file.isDirectory());
    }

    public InputStream getInputStream() throws IOException {
        try {
            return Files.newInputStream(this.file.toPath());
        }
        catch (NoSuchFileException ex) {
            throw new FileNotFoundException(ex.getMessage());
        }
    }

    public boolean isWritable() {
        return (this.file.canWrite() && !this.file.isDirectory());
    }

    public URL getURL() throws IOException {
        return this.file.toURI().toURL();
    }

    public URI getURI() throws IOException {
        return this.file.toURI();
    }

    public boolean isFile() {
        return true;
    }

    public File getFile() {
        return this.file;
    }

    public long contentLength() throws IOException {
        return this.file.length();
    }

}
