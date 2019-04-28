package cn.hecenjie.simpleioc.core.io;

/**
 * @author cenjieHo
 * @since 2019/4/23
 */
public class DefaultResourceLoader implements ResourceLoader {

    /**
     * todo：目前只有FileSystemResource类型的资源，Spring在此处的实现更为复杂一些，日后需要进行扩展
     * @param location
     * @return
     */
    @Override
    public Resource getResource(String location) {
        return new FileSystemResource(location);
    }
}
