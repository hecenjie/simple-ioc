import cn.hecenjie.summerioc.core.io.FileSystemResourceLoader;
import cn.hecenjie.summerioc.core.io.Resource;
import cn.hecenjie.summerioc.core.io.ResourceLoader;

import java.io.IOException;

/**
 * @author cenjieHo
 * @since 2019/4/23
 */
public class ResourceLoaderTest {
    public static void main(String[] args) throws IOException {
        ResourceLoader resourceLoader = new FileSystemResourceLoader();
        Resource resource = resourceLoader.getResource("C:\\Users\\canjie\\Desktop\\test.png");
        System.out.println(resource.exists());
        System.out.println(resource.contentLength());
    }
}
