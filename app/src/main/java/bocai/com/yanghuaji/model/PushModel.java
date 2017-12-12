package bocai.com.yanghuaji.model;

/**
 * 作者 yuanfei on 2017/12/12.
 * 邮箱 yuanfei221@126.com
 */

public class PushModel {

    /**
     *  CMD  :  push
     * pushcode :  sys101
     */

    private String CMD;
    private String pushcode;

    public PushModel(String CMD, String pushcode) {
        this.CMD = CMD;
        this.pushcode = pushcode;
    }

    public String getCMD() {
        return CMD;
    }

    public void setCMD(String CMD) {
        this.CMD = CMD;
    }

    public String getPushcode() {
        return pushcode;
    }

    public void setPushcode(String pushcode) {
        this.pushcode = pushcode;
    }
}
