package top.zspaces.lightlyim.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import top.zspaces.lightlyim.mapper.UsersMapper;


@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class security {

    private final UsersMapper usersMapper;

    @Bean
    public ReactiveUserDetailsService reactiveUserDetailsService() {
        return username -> Mono.fromCallable(() ->
//                ReactiveUserDetailsService中的loadByUsername重载，username就是String username参数。
                        usersMapper.FindByEmail(username))
                .flatMap(users -> {
                    if (users == null) {
                        return Mono.empty(); //用户不存在，返回空。
                    }
                    UserDetails userDetails = org.springframework.security.core.userdetails.User
                            .withUsername(users.getEmail())
                            .password(users.getPassword())
                            .roles(users.getRole() == 1 ? "ADMIN" : "USER")
                            .build();
                    return Mono.just(userDetails);
                }).subscribeOn(Schedulers.boundedElastic());
    }

    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager(ReactiveUserDetailsService reactiveUserDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        UserDetailsRepositoryReactiveAuthenticationManager manager = new UserDetailsRepositoryReactiveAuthenticationManager(reactiveUserDetailsService);
        manager.setPasswordEncoder(bCryptPasswordEncoder);
        return manager;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity serverHttpSecurity,ReactiveAuthenticationManager reactiveAuthenticationManager) {
        serverHttpSecurity
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .authorizeExchange(authorizeExchangeSpec ->
                        authorizeExchangeSpec
                                .pathMatchers("/api/login", "/api/register", "/")
                                .permitAll()
                                .anyExchange()
                                .authenticated())
                .authenticationManager(reactiveAuthenticationManager)
                .securityContextRepository(new WebSessionServerSecurityContextRepository())
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .logout(ServerHttpSecurity.LogoutSpec::disable);
        return serverHttpSecurity.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

