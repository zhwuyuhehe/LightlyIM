package top.zspaces.lightlyim.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Users {
    private int id;
    private String nickname;
    private String email;
    private String password;
    private int role; //role 1:管理员,2:普通用户,3:未激活账户
    private Timestamp last_login;
    private Timestamp created_time;
}


