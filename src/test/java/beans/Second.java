package beans;

/**
 * @author cenjieHo
 * @since 2019/4/27
 */
public class Second {
    First first;

    Integer i;

    public void test(){
        first.sayHello();
    }

    public void sayHello(){
        System.out.println("hello");
    }

    public void testInt(){
        System.out.println(i);
    }
}
