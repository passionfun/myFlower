package bocai.com.yanghuaji.model;

/**
 * Created by apple on 17-11-27.
 */

public class PlantSettingModel {

    /**
     * Id : 3
     * PlantMode : 智能
     * PlantName : 含羞草
     * LifeCycle : 种子期
     */

    private String Id;
    private String PlantMode;
    private String PlantName;
    private String LifeCycle;

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getPlantMode() {
        return PlantMode;
    }

    public void setPlantMode(String PlantMode) {
        this.PlantMode = PlantMode;
    }

    public String getPlantName() {
        return PlantName;
    }

    public void setPlantName(String PlantName) {
        this.PlantName = PlantName;
    }

    public String getLifeCycle() {
        return LifeCycle;
    }

    public void setLifeCycle(String LifeCycle) {
        this.LifeCycle = LifeCycle;
    }
}
