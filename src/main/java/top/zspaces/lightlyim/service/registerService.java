package top.zspaces.lightlyim.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;
import top.zspaces.lightlyim.entity.Users;
import top.zspaces.lightlyim.mapper.UsersMapper;


@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class registerService {

    private final UsersMapper usersMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Mono<ResponseEntity<String> > RegUser(@RequestBody Users userData) {
        return Mono.fromSupplier(()->{
            Users user = new Users();
            if (usersMapper.IsExistUser(userData.getNickname(),userData.getEmail())>0){
                return ResponseEntity.ok("请勿重复注册，昵称或邮箱重复！");
            }
            user.setId(usersMapper.GetMaxId()+1);
            user.setNickname(userData.getNickname());
            user.setEmail(userData.getEmail());
            user.setPassword(bCryptPasswordEncoder.encode(userData.getPassword()));
            user.setRole(2);
            System.out.println(user);
            usersMapper.insertUser(user);
            return ResponseEntity.ok("注册成功");
        });
    }
}
