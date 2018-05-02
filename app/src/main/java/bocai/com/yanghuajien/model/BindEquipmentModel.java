package bocai.com.yanghuajien.model;

/**
 * 作者 yuanfei on 2017/12/1.
 * 邮箱 yuanfei221@126.com
 */

public class BindEquipmentModel {
    private String CMD ;
    private String UUID;

    public BindEquipmentModel(String CMD, String UUID) {
        this.CMD = CMD;
        this.UUID = UUID;
    }

    public String getCMD() {
        return CMD;
    }

    public void setCMD(String CMD) {
        this.CMD = CMD;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }
}
