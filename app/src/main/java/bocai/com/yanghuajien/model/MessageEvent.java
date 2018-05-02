package bocai.com.yanghuajien.model;

/**
 * 作者 yuanfei on 2017/11/30.
 * 邮箱 yuanfei221@126.com
 */

public class MessageEvent {
    public static final String SUCCESS = "SUCCESS";
    public static final String FAILED = "FAILED";
    private String message;
    private String type;
    private int position;

    public MessageEvent(String message, String type) {
        this.message = message;
        this.type = type;
    }

    public MessageEvent(String message, int position) {
        this.message = message;
        this.position = position;
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

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
