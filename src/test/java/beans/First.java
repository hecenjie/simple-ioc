package beans;

/**
 * @author cenjieHo
 * @since 2019/4/27
 */
public class First {
    String p1;

    Second p2;

    public void hello(){
        System.out.println("hello!!!");
    }

    public void testP1(){
        System.out.println(p1);
    }

    public Second getSecond() {
        return p2;
    }
}
