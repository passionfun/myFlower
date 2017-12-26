package bocai.com.yanghuaji.model;

import com.google.gson.annotations.SerializedName;

/**
 * 作者 yuanfei on 2017/11/22.
 * 邮箱 yuanfei221@126.com
 */

public class EquipmentModel {


    /**
     "Name": "EMW3080B Module#10CFE6",
     "IP": "192.168.23.4",
     "Port": 8000,
     "MAC": "B0:F8:93:10:CF:E6",
     "Firmware Rev": "1.0.14",
     "Hardware Rev": "3080B",
     "MICO OS Rev": "3080B002.013",
     "BOUND STATUS": "notBound",
     "Model": "EMW3080B",
     "Protocol": "com.mxchip.basic",
     "LTID": "1.1.2353.31.798",
     "Manufacturer": "MXCHIP Inc.",
     "DEVNAME": "WG101_CFE6",
     "Seed": "0"
     */




    private String LTID;
    private String Name;
    private String DEVNAME;
    private String Manufacturer;
    @SerializedName("Hardware Rev")
    private String _$HardwareRev172; // FIXME check this code
    private int Port;
    private String Model;
    private String IP;
    private String Seed;
    @SerializedName("Firmware Rev")
    private String _$FirmwareRev196; // FIXME check this code
    @SerializedName("BOUND STATUS")
    private String _$BOUNDSTATUS310;
    private String MAC;
    @SerializedName("MICO OS Rev")
    private String _$MICOOSRev276; // FIXME check this code
    private String Protocol;


    public String get_$BOUNDSTATUS310() {
        return _$BOUNDSTATUS310;
    }

    public void set_$BOUNDSTATUS310(String _$BOUNDSTATUS310) {
        this._$BOUNDSTATUS310 = _$BOUNDSTATUS310;
    }


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

    public String getDEVNAME() {
        return DEVNAME;
    }

    public void setDEVNAME(String DEVNAME) {
        this.DEVNAME = DEVNAME;
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


    @Override
    public String toString() {
        return "EquipmentModel{" +
                "LTID='" + LTID + '\'' +
                ", Name='" + Name + '\'' +
                ", DEVNAME='" + DEVNAME + '\'' +
                ", Manufacturer='" + Manufacturer + '\'' +
                ", _$HardwareRev172='" + _$HardwareRev172 + '\'' +
                ", Port=" + Port +
                ", Model='" + Model + '\'' +
                ", IP='" + IP + '\'' +
                ", Seed='" + Seed + '\'' +
                ", _$FirmwareRev196='" + _$FirmwareRev196 + '\'' +
                ", MAC='" + MAC + '\'' +
                ", _$MICOOSRev276='" + _$MICOOSRev276 + '\'' +
                ", Protocol='" + Protocol + '\'' +
                '}';
    }
}
