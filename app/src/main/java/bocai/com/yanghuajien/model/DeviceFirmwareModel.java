package bocai.com.yanghuajien.model;

/**
 * Created by zhanghuanhuan on 2018/8/20.
 */

public class DeviceFirmwareModel {
    private int type;
    private int progress;
    private int state;
    private String deviceName;
    private String uuid;
    private String ltid;
    private String picUrl;
    private String oldVerSoft;
    private String newVerSoft;
    private boolean isChecked;
    private boolean isUpgradeSuccess;

    public DeviceFirmwareModel(int type, int progress, int state, String deviceName, String uuid, String ltid, String picUrl, String oldVerSoft, String newVerSoft, boolean isChecked, boolean isUpgradeSuccess) {
        this.type = type;
        this.progress = progress;
        this.state = state;
        this.deviceName = deviceName;
        this.uuid = uuid;
        this.ltid = ltid;
        this.picUrl = picUrl;
        this.oldVerSoft = oldVerSoft;
        this.newVerSoft = newVerSoft;
        this.isChecked = isChecked;
        this.isUpgradeSuccess = isUpgradeSuccess;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getLtid() {
        return ltid;
    }

    public void setLtid(String ltid) {
        this.ltid = ltid;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getOldVerSoft() {
        return oldVerSoft;
    }

    public void setOldVerSoft(String oldVerSoft) {
        this.oldVerSoft = oldVerSoft;
    }

    public String getNewVerSoft() {
        return newVerSoft;
    }

    public void setNewVerSoft(String newVerSoft) {
        this.newVerSoft = newVerSoft;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isUpgradeSuccess() {
        return isUpgradeSuccess;
    }

    public void setUpgradeSuccess(boolean upgradeSuccess) {
        isUpgradeSuccess = upgradeSuccess;
    }
}
