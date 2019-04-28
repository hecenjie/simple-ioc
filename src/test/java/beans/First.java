package beans;

/**
 * @author cenjieHo
 * @since 2019/4/27
 */
public class First {
    Second second;

    public void test(){
        second.sayHello();
    }

    public void sayHello(){
        System.out.println("hello");
    }
}
