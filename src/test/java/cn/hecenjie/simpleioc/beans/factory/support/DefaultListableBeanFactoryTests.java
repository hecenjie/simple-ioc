package cn.hecenjie.simpleioc.beans.factory.support;

import cn.hecenjie.simpleioc.beans.factory.xml.XmlBeanDefinitionReader;
import cn.hecenjie.simpleioc.core.io.FileSystemResourceLoader;
import cn.hecenjie.simpleioc.core.io.Resource;
import cn.hecenjie.simpleioc.core.io.ResourceLoader;
import cn.hecenjie.simpleioc.tests.objects.login.LoginController;
import cn.hecenjie.simpleioc.tests.objects.persons.IdCard;
import cn.hecenjie.simpleioc.tests.objects.persons.Person;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author cenjieHo
 * @since 2019/5/22
 */
public class DefaultListableBeanFactoryTests {

    @Test
    public void testGetBean() {
        ResourceLoader resourceLoader = new FileSystemResourceLoader();
        Resource resource = resourceLoader.getResource("C:\\Users\\canjie\\Desktop\\login.xml");
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        BeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(factory);
        beanDefinitionReader.loadBeanDefinitions(resource);
        LoginController loginController = (LoginController) factory.getBean("loginController");
        assertEquals(loginController.login("Lihua", "123456789"), true);
    }

    @Test
    public void testCyclicDependence(){
        ResourceLoader resourceLoader = new FileSystemResourceLoader();
        Resource resource = resourceLoader.getResource("C:\\Users\\canjie\\Desktop\\persons.xml");
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        BeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(factory);
        beanDefinitionReader.loadBeanDefinitions(resource);
        Person person = (Person) factory.getBean("person");
        IdCard idCard = (IdCard) factory.getBean("idCard");
        assertEquals(person.getName(), "Lihua");
        assertEquals(person.getAge(), 18);
        assertEquals(person.getIdCard(), idCard);
        assertEquals(idCard.getId(), 441301188875468912L);
        assertEquals(idCard.getOwner(), person);
    }



}
