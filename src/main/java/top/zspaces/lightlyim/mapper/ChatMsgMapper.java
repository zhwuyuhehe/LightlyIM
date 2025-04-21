package top.zspaces.lightlyim.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import top.zspaces.lightlyim.entity.ChatMsg;

import java.util.List;

@Mapper
public interface ChatMsgMapper {

//    @Insert("INSERT INTO CHATHISTORY (ID,FROM_USER,CONTENT,TYPE,TIMESTAMP)" +
//            "VALUES (#{id},#{from_user},#{content},#{type},NOW())")
//    void insertBatch(ChatMsg msg);

    @Select("SELECT MAX(CHATHISTORY.ID) AS maxid FROM CHATHISTORY")
    Integer GetMaxId();


    void insertBatch(@Param("messages") List<ChatMsg> messages);
}
