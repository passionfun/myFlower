package bocai.com.yanghuaji.model;

/**
 * 作者 yuanfei on 2017/11/23.
 * 邮箱 yuanfei221@126.com
 */

public class EquipmentCard {

    /**
     * Id : 2
     * EquipName : WG222+2222
     */

    private String Id;
    private String EquipName;
    private String LTID;
    private String PlantTime;

    public String getPlantTime() {
        return PlantTime;
    }

    public void setPlantTime(String plantTime) {
        PlantTime = plantTime;
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getEquipName() {
        return EquipName;
    }

    public void setEquipName(String EquipName) {
        this.EquipName = EquipName;
    }

    public String getLTID() {
        return LTID;
    }

    public void setLTID(String LTID) {
        this.LTID = LTID;
    }
}
