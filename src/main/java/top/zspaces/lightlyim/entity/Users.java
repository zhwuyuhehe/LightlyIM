package top.zspaces.lightlyim.entity;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class Users implements Serializable {
    private int id;
    private String nickname;
    private String email;
    private String password;
    private int role; //role 1:管理员,2:普通用户
    private Timestamp last_login;
    private Timestamp create_time;
}
