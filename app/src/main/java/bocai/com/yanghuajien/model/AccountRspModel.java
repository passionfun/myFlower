package bocai.com.yanghuajien.model;

import android.widget.RelativeLayout;

import bocai.com.yanghuajien.model.db.User;

/**
 * 作者 yuanfei on 2017/11/16.
 * 邮箱 yuanfei221@126.com
 */

public class AccountRspModel {


    /**
     * Id : 3
     * Token : g4nIbm5A8xpUhS8m2qW0xCspUNr5gpDiSLA6FA34qNa9KCz2ZzobhTwvBrRqWUPo
     * Email: "1990994609@qq.com"
     * NickName : null
     * Sex : null
     * Birthday : null
     * Avatar : {"RelativePath":null,"SmallThumbnail":null}
     */







    private String Id;
    private String Token;
    private String Email;
    private String Phone;
    private String NickName;
    private String Sex;
    private String Birthday;
    private AvatarBean Avatar;

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

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


    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String sex) {
        Sex = sex;
    }

    public String getBirthday() {
        return Birthday;
    }

    public void setBirthday(String birthday) {
        Birthday = birthday;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public AvatarBean getAvatar() {
        return Avatar;
    }

    public void setAvatar(AvatarBean Avatar) {
        this.Avatar = Avatar;
    }


    // 缓存一个对应的User, 不能被GSON框架解析使用ø
    private transient User user;

    public User build() {
        if (user == null) {
            User user = new User();
            user.setId(Id);
            user.setToken(Token);
            user.setEmail(Email);
            user.setNickName(NickName);
            user.setSex(Sex);
            user.setBirthday(Birthday);
            user.setRelativePath(getAvatar().getRelativePath());
            user.setSmallThumbnail(getAvatar().getSmallThumbnail());
            this.user = user;
        }
        return user;
    }


    public static class AvatarBean {
        /**
         * RelativePath : null
         * SmallThumbnail : null
         */

        private String RelativePath;
        private String SmallThumbnail;

        public String getRelativePath() {
            return RelativePath;
        }

        public void setRelativePath(String relativePath) {
            RelativePath = relativePath;
        }

        public String getSmallThumbnail() {
            return SmallThumbnail;
        }

        public void setSmallThumbnail(String smallThumbnail) {
            SmallThumbnail = smallThumbnail;
        }

        @Override
        public String toString() {
            return "RelativePath:"+ RelativePath+",SmallThumbnail:"+SmallThumbnail;
        }
    }

    @Override
    public String toString() {
        return "id:"+Id+",Token:"+Token+",Email:"+Email+",Phone:"+Phone+",NickName:"+NickName+",Sex:"+Sex+",Birthday:"+Birthday+",AvatarBean:"+Avatar.toString();
    }
}
