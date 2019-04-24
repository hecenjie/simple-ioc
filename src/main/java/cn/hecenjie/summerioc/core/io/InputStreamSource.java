package cn.hecenjie.summerioc.core.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * {@link Resource}的基础接口
 *
 * @author cenjieHo
 * @since 2019/4/23
 */
public interface InputStreamSource {

    InputStream getInputStream() throws IOException;

}
