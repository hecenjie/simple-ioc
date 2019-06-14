package cn.hecenjie.simpleioc.beans.factory.xml;

import cn.hecenjie.simpleioc.beans.factory.support.BeanDefinitionReader;
import cn.hecenjie.simpleioc.beans.factory.support.BeanDefinitionRegistry;
import cn.hecenjie.simpleioc.beans.factory.support.DefaultListableBeanFactory;
import cn.hecenjie.simpleioc.core.io.FileSystemResourceLoader;
import cn.hecenjie.simpleioc.core.io.Resource;
import cn.hecenjie.simpleioc.core.io.ResourceLoader;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author cenjieHo
 * @since 2019/5/20
 */
public class XmlBeanDefinitionReaderTests {

    @Test
    public void testLoadBeanDefinitions() {
        ResourceLoader resourceLoader = new FileSystemResourceLoader();
        Resource resource = resourceLoader.getResource("C:\\Users\\canjie\\Desktop\\simple.xml");
        BeanDefinitionRegistry registry = new DefaultListableBeanFactory();
        BeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(registry);
        assertEquals(beanDefinitionReader.loadBeanDefinitions(resource), 2);
        assertEquals(((DefaultListableBeanFactory) registry).getBeanDefinition("first").getBeanClassName(),
                "beans.First");
        assertEquals(((DefaultListableBeanFactory) registry).getBeanDefinition("second").getBeanClassName(),
                "beans.Second");
    }
}
