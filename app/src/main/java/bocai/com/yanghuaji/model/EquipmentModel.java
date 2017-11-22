package bocai.com.yanghuaji.model;

import com.google.gson.annotations.SerializedName;

/**
 * 作者 yuanfei on 2017/11/22.
 * 邮箱 yuanfei221@126.com
 */

public class EquipmentModel {


    /**
     * LTID : 1.1.2353.29.152
     * Name : EMW3080B Module#10C460
     * Manufacturer : MXCHIP Inc.
     * Hardware Rev : 3080B
     * Port : 8000
     * Model : EMW3080B
     * IP : 192.168.23.5
     * Seed : 0
     * Firmware Rev : MICO_BASIC_1_0
     * MAC : B0:F8:93:10:C4:60
     * MICO OS Rev : 3080B002.013
     * Protocol : com.mxchip.basic
     */

    private String LTID;
    private String Name;
    private String Manufacturer;
    @SerializedName("Hardware Rev")
    private String _$HardwareRev172; // FIXME check this code
    private int Port;
    private String Model;
    private String IP;
    private String Seed;
    @SerializedName("Firmware Rev")
    private String _$FirmwareRev196; // FIXME check this code
    private String MAC;
    @SerializedName("MICO OS Rev")
    private String _$MICOOSRev276; // FIXME check this code
    private String Protocol;

    public String getLTID() {
        return LTID;
    }

    public void setLTID(String LTID) {
        this.LTID = LTID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getManufacturer() {
        return Manufacturer;
    }

    public void setManufacturer(String Manufacturer) {
        this.Manufacturer = Manufacturer;
    }

    public String get_$HardwareRev172() {
        return _$HardwareRev172;
    }

    public void set_$HardwareRev172(String _$HardwareRev172) {
        this._$HardwareRev172 = _$HardwareRev172;
    }

    public int getPort() {
        return Port;
    }

    public void setPort(int Port) {
        this.Port = Port;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String Model) {
        this.Model = Model;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getSeed() {
        return Seed;
    }

    public void setSeed(String Seed) {
        this.Seed = Seed;
    }

    public String get_$FirmwareRev196() {
        return _$FirmwareRev196;
    }

    public void set_$FirmwareRev196(String _$FirmwareRev196) {
        this._$FirmwareRev196 = _$FirmwareRev196;
    }

    public String getMAC() {
        return MAC;
    }

    public void setMAC(String MAC) {
        this.MAC = MAC;
    }

    public String get_$MICOOSRev276() {
        return _$MICOOSRev276;
    }

    public void set_$MICOOSRev276(String _$MICOOSRev276) {
        this._$MICOOSRev276 = _$MICOOSRev276;
    }

    public String getProtocol() {
        return Protocol;
    }

    public void setProtocol(String Protocol) {
        this.Protocol = Protocol;
    }

}
