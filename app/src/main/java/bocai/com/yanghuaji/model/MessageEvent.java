package bocai.com.yanghuaji.model;

/**
 * 作者 yuanfei on 2017/11/30.
 * 邮箱 yuanfei221@126.com
 */

public class MessageEvent {
    public static final String SUCCESS = "SUCCESS";
    public static final String FAILED = "FAILED";
    private String message;
    private String type;

    public MessageEvent(String message, String type) {
        this.message = message;
        this.type = type;
    }


    public MessageEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
