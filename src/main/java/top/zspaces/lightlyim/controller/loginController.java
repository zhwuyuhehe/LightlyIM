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
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;
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
    public Mono<ResponseEntity<Map<String, Object>>> login(@RequestBody Map<String, String> loginKey, ServerWebExchange exchange) {
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
                    userInfo.put("isAdmin", userDetails.getAuthorities().stream()
                            .anyMatch(
                                    grantedAuthority ->
                                            grantedAuthority.getAuthority()
                                                    .equals("ROLE_ADMIN")));

                    Map<String, Object> response = new HashMap<>();
                    response.put("code", 200);
                    response.put("message", "登录成功");
                    response.put("role", userDetails.getAuthorities());
                    response.put("user", userInfo);

                    return exchange.getSession().doOnNext(
                                    webSession -> webSession.getAttributes()
                                            .put("UserInfo", userDetails))
                            .thenReturn(ResponseEntity.ok(response));

                })
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("用户 {} 登录失败：用户名或密码错误", email);
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("code", 401);
                    errorResponse.put("message", "用户名或密码错误");
                    return Mono.just(ResponseEntity.ok(errorResponse));
                }));
    }

    @PostMapping("/WhoAmI")
    public Mono<ResponseEntity<Map<String, Object>>> whoAmI(ServerWebExchange exchange) {
        return exchange.getSession()
                .map(WebSession::getAttributes)
                .map(attrs -> {
                    Object user = attrs.get("UserInfo");
                    if (user != null) {
                        boolean isAdmin = false;
                        if (user instanceof UserDetails userDetails) {
                            isAdmin = userDetails.getAuthorities().stream()
                                    .anyMatch(g -> g.getAuthority().equals("ROLE_ADMIN"));
                        }
                        return ResponseEntity.ok(Map.of(
                                "code", 200,
                                "message", "获取成功",
                                "isAdmin", isAdmin,
                                "user", user
                        ));
                    } else {
                        return ResponseEntity.status(401).body(Map.of(
                                "code", 401,
                                "message", "未登录"
                        ));
                    }
                });
    }

}
