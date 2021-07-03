package nguyenhuuvu.utils;

public class Constant {
    public static final String VERIFY_MAIL_TEMPLATE = "verify-template";
    public static final String VERIFY_ACCOUNT_SUBJECT = "Xác nhận tài khoản DevChat";
    public static final String VERIFY_ACCOUNT_TIME_EXPIRE = "5 giờ";
    public static final String RESET_PASSWORD_TIME_EXPIRE = "2 giờ";
    public static final String CODE_CHECK = "454545454e5454545454212315421a1a2d5af4ad4fs5afds4adsf5";

    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60 * 1000;
    /**
     * one day
     */
    public static final int TIME_VERIFY_SIGNUP = 60*5*60*1000;

    /**
     * one hour
     */
    public static final int TIME_VERIFY_FORGOT_PASSWORD = 5*60*60*1000;

    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String DMOAIN = "http://devchat.me";
}
