package bocai.com.yanghuaji.model;

/**
 * 作者 yuanfei on 2017/11/16.
 * 邮箱 yuanfei221@126.com
 */

public class AccountRspModel {


    /**
     * Id : 3
     * Token : g4nIbm5A8xpUhS8m2qW0xCspUNr5gpDiSLA6FA34qNa9KCz2ZzobhTwvBrRqWUPo
     * Phone : 18357876718
     * NickName : null
     * Sex : null
     * Birthday : null
     * Avatar : {"RelativePath":null,"SmallThumbnail":null}
     */

    private String Id;
    private String Token;
    private String Phone;
    private Object NickName;
    private Object Sex;
    private Object Birthday;
    private AvatarBean Avatar;

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String Token) {
        this.Token = Token;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String Phone) {
        this.Phone = Phone;
    }

    public Object getNickName() {
        return NickName;
    }

    public void setNickName(Object NickName) {
        this.NickName = NickName;
    }

    public Object getSex() {
        return Sex;
    }

    public void setSex(Object Sex) {
        this.Sex = Sex;
    }

    public Object getBirthday() {
        return Birthday;
    }

    public void setBirthday(Object Birthday) {
        this.Birthday = Birthday;
    }

    public AvatarBean getAvatar() {
        return Avatar;
    }

    public void setAvatar(AvatarBean Avatar) {
        this.Avatar = Avatar;
    }

    public static class AvatarBean {
        /**
         * RelativePath : null
         * SmallThumbnail : null
         */

        private Object RelativePath;
        private Object SmallThumbnail;

        public Object getRelativePath() {
            return RelativePath;
        }

        public void setRelativePath(Object RelativePath) {
            this.RelativePath = RelativePath;
        }

        public Object getSmallThumbnail() {
            return SmallThumbnail;
        }

        public void setSmallThumbnail(Object SmallThumbnail) {
            this.SmallThumbnail = SmallThumbnail;
        }
    }
}
