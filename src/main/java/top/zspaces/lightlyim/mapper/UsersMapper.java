package top.zspaces.lightlyim.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import top.zspaces.lightlyim.entity.Users;

import java.util.List;

@Mapper
public interface UsersMapper {
    @Select("SELECT ID, NICKNAME, EMAIL, PASSWORD, ROLE, LAST_LOGIN, CREATE_TIME FROM USERS")
    List<Users> ListAll();

    @Select("SELECT COUNT(*) from USERS WHERE NICKNAME = #{nickname} OR EMAIL = #{email}")
    int IsExistUser(String nickname,String email);

    @Select("SELECT ID, NICKNAME, EMAIL, PASSWORD, ROLE, LAST_LOGIN, CREATE_TIME from USERS WHERE EMAIL = #{email}")
    Users FindByEmail(String email);

    @Insert("INSERT INTO USERS (ID, NICKNAME, EMAIL, PASSWORD, ROLE, LAST_LOGIN, CREATE_TIME) VALUES ( #{id},#{nickname},#{email},#{password},#{role},NOW(),now() )")
    void insertUser(Users users);

    @Select("SELECT MAX(USERS.ID) as maxid from USERS ")
    Integer GetMaxId();
}
