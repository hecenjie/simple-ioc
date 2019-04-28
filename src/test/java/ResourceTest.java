import cn.hecenjie.simpleioc.core.io.FileSystemResource;
import cn.hecenjie.simpleioc.core.io.Resource;

import java.io.IOException;

/**
 * @author cenjieHo
 * @since 2019/4/23
 */
public class ResourceTest {
    public static void main(String[] args) throws IOException {
        Resource resource = new FileSystemResource("C:\\Users\\canjie\\Desktop\\test.png");
        System.out.println(resource.exists());
        System.out.println(resource.contentLength());
    }
}
