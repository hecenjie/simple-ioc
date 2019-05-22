package cn.hecenjie.simpleioc.tests.objects.persons;

/**
 * @author cenjieHo
 * @since 2019/5/22
 */
public class Person {
    private String name;
    private int age;
    private IdCard idCard;

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public IdCard getIdCard() {
        return idCard;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", idCard=" + idCard +
                '}';
    }
}
