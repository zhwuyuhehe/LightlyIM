package top.zspaces.lightlyim.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;
import top.zspaces.lightlyim.entity.Users;
import top.zspaces.lightlyim.mapper.UsersMapper;

@Service
public class login implements UserDetailsService {
    private final UsersMapper UM;

    @Autowired
    public login(UsersMapper um) {
        UM = um;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users user = UM.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在："+email);
        }
        String role = user.getRole() == 1 ? "ADMIN" : "USER";
        return User.withUsername(user.getEmail()).password(user.getPassword()).roles(role).build();
    }

}
