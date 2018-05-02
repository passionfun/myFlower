package bocai.com.yanghuajien.model;

import java.io.Serializable;
import java.util.List;

/**
 * 作者 yuanfei on 2017/11/24.
 * 邮箱 yuanfei221@126.com
 */

public class PlantRspModel {

    /**
     * List : [{"Id":"2","PlantName":"满天星","Photo":"http://121.41.128.239:8082/yhj/web/upload/2017/11/14/15106633749053zp9dm.jpg"}]
     * CountPage : 1
     * Count : 1
     */

    private int CountPage;
    private int Count;
    private List<PlantCard> List;

    public int getCountPage() {
        return CountPage;
    }

    public void setCountPage(int CountPage) {
        this.CountPage = CountPage;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int Count) {
        this.Count = Count;
    }

    public List<PlantCard> getList() {
        return List;
    }

    public void setList(List<PlantCard> List) {
        this.List = List;
    }

    public static class PlantCard implements Serializable{
        /**
         * Id : 2
         * PlantName : 满天星
         * Photo : http://121.41.128.239:8082/yhj/web/upload/2017/11/14/15106633749053zp9dm.jpg
         */

        private String Id;
        private String PlantName;
        private String Photo;

        public String getId() {
            return Id;
        }

        public void setId(String Id) {
            this.Id = Id;
        }

        public String getPlantName() {
            return PlantName;
        }

        public void setPlantName(String PlantName) {
            this.PlantName = PlantName;
        }

        public String getPhoto() {
            return Photo;
        }

        public void setPhoto(String Photo) {
            this.Photo = Photo;
        }
    }
}
