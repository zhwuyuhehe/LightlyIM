package top.zspaces.lightlyim.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import top.zspaces.lightlyim.entity.Users;
import top.zspaces.lightlyim.service.registerService;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping("/api")
public class registerController {

    private final registerService registerService;

    @PostMapping("/register")
    public Mono<ResponseEntity<String> > RegUser(@RequestBody Users userKey ) {
        return registerService.RegUser(userKey);
    }
}
