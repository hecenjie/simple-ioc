package cn.hecenjie.summerioc.core.io;

/**
 * 统一的资源加载的抽象，默认实现为{@link DefaultResourceLoader}
 *
 * @author cenjieHo
 * @since 2019/4/23
 */
public interface ResourceLoader {

    String CLASSPATH_URL_PREFIX = "classpath:";

    /**
     * 要注意返回的资源不一定存在，需要使用{@link Resource#exists}作进一步判断
     * @param location
     * @return
     */
    Resource getResource(String location);
}
