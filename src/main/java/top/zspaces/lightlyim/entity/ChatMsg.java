package top.zspaces.lightlyim.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMsg implements Serializable {
    private int id;
    private String from_user;
    private String content;
    private String type;
    private Timestamp timestamp;
}
