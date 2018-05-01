package users;

import annotations.Column;
import annotations.Table;

@Table("users")
public class UserBean  {
    @Column("UserName")
    private String UserName;
    @Column("NickName")
    private String NickName;
    @Column("Password")
    private String Password;
    @Column("Head")
    private String Head;

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getHead() {
        return Head;
    }

    public void setHead(String head) {
        Head = head;
    }
}
