package bocai.com.yanghuaji.model;

import java.util.List;

/**
 * 作者 yuanfei on 2017/11/20.
 * 邮箱 yuanfei221@126.com
 */

public class DiaryListModel {
    private List<DiaryModl> List;
    private int CountPage;
    private int Count;

    public List<DiaryModl> getList() {
        return List;
    }

    public void setList(List<DiaryModl> list) {
        List = list;
    }

    public int getCountPage() {
        return CountPage;
    }

    public void setCountPage(int countPage) {
        CountPage = countPage;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }

    public static class DiaryModl{

        /**
         * Id : 2
         * BookName : 玫瑰花的日记
         * Timeline : 1510572981
         * Photos : ["http://121.41.128.239:8082/yhj/web/upload/2017/11/13/20171113105950Gh4i5291.jpg",
         * "http://121.41.128.239:8082/yhj/web/upload/2017/11/13/20171113110443eCD0547.jpg",
         * "http://121.41.128.239:8082/yhj/web/upload/2017/11/13/201711131047271NS967731.png"]
         * Link :
         */

        private String Id;
        private String BookName;
        private String Timeline;
        private String Link;
        private List<String> Photos;

        public String getId() {
            return Id;
        }

        public void setId(String Id) {
            this.Id = Id;
        }

        public String getBookName() {
            return BookName;
        }

        public void setBookName(String BookName) {
            this.BookName = BookName;
        }

        public String getTimeline() {
            return Timeline;
        }

        public void setTimeline(String Timeline) {
            this.Timeline = Timeline;
        }

        public String getLink() {
            return Link;
        }

        public void setLink(String Link) {
            this.Link = Link;
        }

        public List<String> getPhotos() {
            return Photos;
        }

        public void setPhotos(List<String> Photos) {
            this.Photos = Photos;
        }
    }
}
