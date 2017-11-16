package bocai.com.yanghuaji.model;

/**
 * 作者 yuanfei on 2017/11/16.
 * 邮箱 yuanfei221@126.com
 */

public class GetSmsCodeModel {
    String Phone;
    String Type;

    public GetSmsCodeModel(String phone, String type) {
        Phone = phone;
        Type = type;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }
}
