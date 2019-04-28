import beans.First;
import beans.Second;
import cn.hecenjie.simpleioc.beans.factory.support.DefaultListableBeanFactory;
import cn.hecenjie.simpleioc.beans.factory.xml.XmlBeanDefinitionReader;
import cn.hecenjie.simpleioc.core.io.FileSystemResourceLoader;
import cn.hecenjie.simpleioc.core.io.Resource;
import cn.hecenjie.simpleioc.core.io.ResourceLoader;

import java.io.IOException;

/**
 * @author cenjieHo
 * @since 2019/4/27
 */
public class GetBeanTest {
    public static void main(String[] args) throws IOException {
        ResourceLoader resourceLoader = new FileSystemResourceLoader();
        Resource resource = resourceLoader.getResource("C:\\Users\\canjie\\Desktop\\simple.xml");
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        reader.loadBeanDefinitions(resource);

        First f = (First) factory.getBean("first");
        Second s = (Second) factory.getBean("second");
        f.test();
        s.test();
//        s.testInt();
    }
}
