package bocai.com.yanghuaji.model;

/**
 * 作者 yuanfei on 2017/11/17.
 * 邮箱 yuanfei221@126.com
 */

public class ImageModel {


    /**
     * Avatar : {"Img1":{"Id":16,"RelativePath":"http://121.41.128.239:8082/yhj/web/upload/2017/11/15/20171115171247Dpzj9239.jpg",
     * "SmallThumbnail":"http://121.41.128.239:8082/yhj/web/upload/2017/11/15/thumbnail/20171115171247Dpzj9239.jpg"}}
     */

    private AvatarBean Avatar;

    public AvatarBean getAvatar() {
        return Avatar;
    }

    public void setAvatar(AvatarBean Avatar) {
        this.Avatar = Avatar;
    }

    public static class AvatarBean {
        /**
         * Img1 : {"Id":16,"RelativePath":"http://121.41.128.239:8082/yhj/web/upload/2017/11/15/20171115171247Dpzj9239.jpg","SmallThumbnail":"http://121.41.128.239:8082/yhj/web/upload/2017/11/15/thumbnail/20171115171247Dpzj9239.jpg"}
         */

        private Img1Bean Img1;

        public Img1Bean getImg1() {
            return Img1;
        }

        public void setImg1(Img1Bean Img1) {
            this.Img1 = Img1;
        }

        public static class Img1Bean {
            /**
             * Id : 16
             * RelativePath : http://121.41.128.239:8082/yhj/web/upload/2017/11/15/20171115171247Dpzj9239.jpg
             * SmallThumbnail : http://121.41.128.239:8082/yhj/web/upload/2017/11/15/thumbnail/20171115171247Dpzj9239.jpg
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
}
