package bocai.com.yanghuaji.model;

import java.util.List;

/**
 * 作者 yuanfei on 2017/11/17.
 * 邮箱 yuanfei221@126.com
 */

public class ImageModel {


    private List<AvatarBean> Avatar;

    public List<AvatarBean> getAvatar() {
        return Avatar;
    }

    public void setAvatar(List<AvatarBean> Avatar) {
        this.Avatar = Avatar;
    }

    public static class AvatarBean {
        /**
         * Id : 66
         * RelativePath : http://121.41.128.239:8082/yhj/web/upload/2017/11/21/20171121103117wPsP4311.png
         * SmallThumbnail : http://121.41.128.239:8082/yhj/web/upload/2017/11/21/thumbnail/20171121103117wPsP4311.png
         */

        private int Id;
        private String RelativePath;
        private String SmallThumbnail;

        public int getId() {
            return Id;
        }

        public void setId(int Id) {
            this.Id = Id;
        }

        public String getRelativePath() {
            return RelativePath;
        }

        public void setRelativePath(String RelativePath) {
            this.RelativePath = RelativePath;
        }

        public String getSmallThumbnail() {
            return SmallThumbnail;
        }

        public void setSmallThumbnail(String SmallThumbnail) {
            this.SmallThumbnail = SmallThumbnail;
        }
    }
}
