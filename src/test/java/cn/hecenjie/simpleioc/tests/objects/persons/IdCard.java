package cn.hecenjie.simpleioc.tests.objects.persons;

/**
 * @author cenjieHo
 * @since 2019/5/22
 */
public class IdCard {
    private long id;
    private Person owner;

    public long getId() {
        return id;
    }

    public Person getOwner() {
        return owner;
    }

    @Override
    public String toString() {
        return "IdCard{" +
                "id=" + id +
                ", person=" + owner +
                '}';
    }
}
