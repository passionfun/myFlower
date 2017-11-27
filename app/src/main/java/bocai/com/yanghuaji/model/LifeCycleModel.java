package bocai.com.yanghuaji.model;

import java.util.List;

/**
 * Created by apple on 17-11-27.
 */

public class LifeCycleModel {


    private java.util.List<ListBean> List;

    public List<ListBean> getList() {
        return List;
    }

    public void setList(List<ListBean> List) {
        this.List = List;
    }

    public static class ListBean {
        /**
         * Id : 1
         * Title : 种子期
         */

        private String Id;
        private String Title;

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
    }
}
