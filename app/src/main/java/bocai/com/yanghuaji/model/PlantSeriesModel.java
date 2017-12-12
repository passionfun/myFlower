package bocai.com.yanghuaji.model;

import java.io.Serializable;
import java.util.List;

/**
 * 作者 yuanfei on 2017/11/29.
 * 邮箱 yuanfei221@126.com
 */

public class PlantSeriesModel {


    /**
     * List : [{"Id":"5","Title":"植记系列"},{"Id":"6","Title":"三生石系列"},{"Id":"7","Title":"其他系列"}]
     * CountPage : 1
     * Count : 3
     */

    private int CountPage;
    private int Count;
    private java.util.List<PlantSeriesCard> List;

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

    public List<PlantSeriesCard> getList() {
        return List;
    }

    public void setList(List<PlantSeriesCard> List) {
        this.List = List;
    }

    public static class PlantSeriesCard implements Serializable{
        /**
         * Id : 5
         * Title : 植记系列
         * Series: WG101
         */

        private String Id;
        private String Title;
        private String Series;

        public String getId() {
            return Id;
        }

        public void setId(String Id) {
            this.Id = Id;
        }

        public String getTitle() {
            return Title;
        }

        public void setTitle(String Title) {
            this.Title = Title;
        }

        public String getSeries() {
            return Series;
        }

        public void setSeries(String series) {
            Series = series;
        }
    }
}
