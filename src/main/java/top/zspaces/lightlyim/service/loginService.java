package top.zspaces.lightlyim.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import top.zspaces.lightlyim.mapper.UsersMapper;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class loginService {

    private final UsersMapper um;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Mono<Authentication> authenticate(String email, String password) {
        return Mono.fromSupplier(
                () -> um.FindByEmail(email)
        ).filter(
                users -> users != null && bCryptPasswordEncoder.matches(password, users.getPassword())
        ).map(
                users -> {
                    UserDetails userDetails = User.withUsername(users.getEmail())
                            .password(bCryptPasswordEncoder.encode(password))
                            .roles(users.getRole() == 1 ? "ADMIN" : "USER")
                            .build();
                    return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
                }
        );
    }
}
