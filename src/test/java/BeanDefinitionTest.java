import cn.hecenjie.summerioc.beans.factory.support.BeanDefinitionReader;
import cn.hecenjie.summerioc.beans.factory.support.BeanDefinitionRegistry;
import cn.hecenjie.summerioc.beans.factory.support.DefaultListableBeanFactory;
import cn.hecenjie.summerioc.beans.factory.xml.XmlBeanDefinitionReader;
import cn.hecenjie.summerioc.core.io.FileSystemResourceLoader;
import cn.hecenjie.summerioc.core.io.Resource;
import cn.hecenjie.summerioc.core.io.ResourceLoader;

import java.io.IOException;

/**
 * @author cenjieHo
 * @since 2019/4/25
 */
public class BeanDefinitionTest {
    public static void main(String[] args) throws IOException {
        ResourceLoader resourceLoader = new FileSystemResourceLoader();
        Resource resource = resourceLoader.getResource("C:\\Users\\canjie\\Desktop\\test.xml");
        System.out.println("XML file exists: " + resource.exists());
        System.out.println("XML file size: " + resource.contentLength());
        BeanDefinitionRegistry registry = new DefaultListableBeanFactory();
        BeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(registry);
        int cnt = beanDefinitionReader.loadBeanDefinitions(resource);
        System.out.println("Bean definitions count: " + cnt);
    }
}
