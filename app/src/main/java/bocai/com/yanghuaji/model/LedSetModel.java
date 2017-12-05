package bocai.com.yanghuaji.model;

/**
 * 设置 LED 灯
 * 作者 yuanfei on 2017/12/5.
 * 邮箱 yuanfei221@126.com
 */

public class LedSetModel {


    /**
     * CMD :  LedSet
     * SWTICH : On On/Off 非必传，需要注意的是，为保证植物正常生长该设置仅当天生效。
     * BRIGHTNESS : 100 亮度，百分比形式（非必传）
     * UUID : 1504608600
     */

    private String CMD;
    private String SWTICH;
    private String BRIGHTNESS;
    private String UUID;

    public LedSetModel() {
        this.CMD="LedSet";
        this.BRIGHTNESS="100";
    }

    public LedSetModel(String SWTICH, String UUID) {
        this.CMD="LedSet";
        this.BRIGHTNESS="100";
        this.SWTICH = SWTICH;
        this.UUID = UUID;
    }

    public String getCMD() {
        return CMD;
    }

    public void setCMD(String CMD) {
        this.CMD = CMD;
    }

    public String getSWTICH() {
        return SWTICH;
    }

    public void setSWTICH(String SWTICH) {
        this.SWTICH = SWTICH;
    }

    public String getBRIGHTNESS() {
        return BRIGHTNESS;
    }

    public void setBRIGHTNESS(String BRIGHTNESS) {
        this.BRIGHTNESS = BRIGHTNESS;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }
}
