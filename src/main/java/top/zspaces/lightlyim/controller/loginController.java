package top.zspaces.lightlyim.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import top.zspaces.lightlyim.service.loginService;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class loginController {


    private final loginService loginService;

    @PostMapping("/login")
    public Mono<ResponseEntity<Map<String, Object>>> login(@RequestBody Map<String, String> loginKey) {
        String email = loginKey.get("email");
        String password = loginKey.get("password");

        return loginService.authenticate(email, password)

                .flatMap(auth -> {
                    UserDetails userDetails = (UserDetails) auth.getPrincipal(); // 获取用户信息
                    Map<String, Object> userInfo = new HashMap<>();
                    userInfo.put("email", userDetails.getUsername());
                    userInfo.put("roles", userDetails
                            .getAuthorities()
                            .stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toList()));

                    Map<String, Object> response = new HashMap<>();
                    response.put("code", 200);
                    response.put("message", "登录成功");
                    response.put("role", userDetails.getAuthorities());
                    response.put("user", userInfo);

                    return Mono.just(ResponseEntity.ok(response));
                })
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("用户 {} 登录失败：用户名或密码错误", email);
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("code", 401);
                    errorResponse.put("message", "用户名或密码错误");
                    return Mono.just(ResponseEntity.status(401).body(errorResponse));
                }));
    }

}
