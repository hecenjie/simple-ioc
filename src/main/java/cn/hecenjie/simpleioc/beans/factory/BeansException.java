package cn.hecenjie.simpleioc.beans.factory;

/**
 * @author cenjieHo
 * @since 2019/4/24
 */
public class BeansException extends RuntimeException {

    public BeansException(String msg) {
        super(msg);
    }

    public BeansException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
