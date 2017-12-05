package bocai.com.yanghuaji.model;

/**
 * 作者 yuanfei on 2017/12/5.
 * 邮箱 yuanfei221@126.com
 */

public class LedSetRspModel {


    /**
     * CODE : 0
     * SWTICH : On
     * BRIGHTNESS : 100
     */

    private int CODE;
    private String SWTICH;
    private int BRIGHTNESS;

    public int getCODE() {
        return CODE;
    }

    public void setCODE(int CODE) {
        this.CODE = CODE;
    }

    public String getSWTICH() {
        return SWTICH;
    }

    public void setSWTICH(String SWTICH) {
        this.SWTICH = SWTICH;
    }

    public int getBRIGHTNESS() {
        return BRIGHTNESS;
    }

    public void setBRIGHTNESS(int BRIGHTNESS) {
        this.BRIGHTNESS = BRIGHTNESS;
    }
}
