package cn.hecenjie.simpleioc.core.io;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author cenjieHo
 * @since 2019/5/19
 */
public class ResourceTests {

    @Test
    public void testFileSystemResource() throws IOException {
        Resource resource = new FileSystemResource("C:\\Users\\canjie\\Desktop\\test.xml");
        assertTrue(resource.exists());
        assertEquals(resource.contentLength(), 992);
    }
}
