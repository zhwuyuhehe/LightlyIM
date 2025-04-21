package top.zspaces.lightlyim.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

import java.util.Map;
@RestController
@RequestMapping("/api")
public class logoutController {
    @PostMapping("/logout")
    public Mono<ResponseEntity<Map<String, Object>>> logout(ServerWebExchange exchange) {
        return exchange.getSession().doOnNext(WebSession::invalidate)
                .thenReturn(ResponseEntity.ok(Map.of("code", 200, "message", "退出成功")));
    }
}
