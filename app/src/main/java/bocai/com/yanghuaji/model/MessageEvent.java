package bocai.com.yanghuaji.model;

/**
 * 作者 yuanfei on 2017/11/30.
 * 邮箱 yuanfei221@126.com
 */

public class MessageEvent {
    private String message;

    public MessageEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
