package cn.hecenjie.simpleioc.tests.objects.login;

/**
 * @author cenjieHo
 * @since 2019/5/22
 */
public class LoginController {

    private LoginService loginService;

    public Boolean login(String username, String password){
        return loginService.login(username, password);
    }

}
