package cn.hecenjie.summerioc.core.io;

/**
 * @author cenjieHo
 * @since 2019/4/23
 */
public class FileSystemResourceLoader extends DefaultResourceLoader {
    @Override
    public Resource getResource(String location) {
        return new FileSystemResource(location);
    }
}
