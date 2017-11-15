package bocai.com.yanghuaji.model;

/**
 * 作者 yuanfei on 2017/11/15.
 * 邮箱 yuanfei221@126.com
 */

public class RegisterModel {
    private String Phone;
    private String SmsCode;
    private String Password;
    private String RePassword;

    public RegisterModel(String phone, String smsCode, String password, String rePassword) {

        Phone = phone;
        SmsCode = smsCode;
        Password = password;
        RePassword = rePassword;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getSmsCode() {
        return SmsCode;
    }

    public void setSmsCode(String smsCode) {
        SmsCode = smsCode;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getRePassword() {
        return RePassword;
    }

    public void setRePassword(String rePassword) {
        RePassword = rePassword;
    }
}
