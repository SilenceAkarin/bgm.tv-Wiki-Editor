package cn.itcast.bgmtv_wikieditor_;

public class UserInfo {
    private int user_id;
    private String email;
    private String password;
    private String nickname;

    public UserInfo(int user_id, String email, String password, String nickname) {
        this.user_id = user_id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
