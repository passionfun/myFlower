package bocai.com.yanghuajien.model;

/**
 * Created by zhanghuanhuan on 2018/8/9.
 */

public class OldEquipmentInfo {
    private String deviceName;
    private String uuid;
    private String id;
    private String mac;
    private String ltid;
    private String version;

    public OldEquipmentInfo(String deviceName, String uuid, String id, String mac, String ltid, String version) {
        this.deviceName = deviceName;
        this.uuid = uuid;
        this.id = id;
        this.mac = mac;
        this.ltid = ltid;
        this.version = version;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getLtid() {
        return ltid;
    }

    public void setLtid(String ltid) {
        this.ltid = ltid;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
