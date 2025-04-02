package top.zspaces.lightlyim.mapper;

import top.zspaces.lightlyim.entity.Users;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UsersMapper {
    @Select("SELECT * FROM Users")
    Users ListAll();

    @Select("SELECT * from USERS WHERE EMAIL = #{email}")
    Users findByEmail(String email);
}
