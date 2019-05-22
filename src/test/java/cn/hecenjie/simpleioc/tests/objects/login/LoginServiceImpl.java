package cn.hecenjie.simpleioc.tests.objects.login;

/**
 * @author cenjieHo
 * @since 2019/5/22
 */
public class LoginServiceImpl implements LoginService{

    private UserDao userDao;

    @Override
    public Boolean login(String username, String password) {
        return userDao.select(username, password);
    }
}
