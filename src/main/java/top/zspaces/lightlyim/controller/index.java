package top.zspaces.lightlyim.controller;

import com.alibaba.fastjson2.JSONArray;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zspaces.lightlyim.mapper.UsersMapper;
import top.zspaces.lightlyim.service.WebSocketSessionManager;

@RestController
public class index {

    private final UsersMapper UM;
    private final WebSocketSessionManager webSocketSessionManager;

    public index(UsersMapper usersMapper, WebSocketSessionManager webSocketSessionManager) {
        UM = usersMapper;
        this.webSocketSessionManager = webSocketSessionManager;
    }

    @GetMapping("/")
    public ResponseEntity<JSONArray> indexView() {
        return ResponseEntity.ok(JSONArray.from(UM.ListAll()));
    }

    @PostMapping("/api/ws/online")
    public ResponseEntity<JSONArray> onlineUsers() {
        return ResponseEntity.ok(JSONArray.from(webSocketSessionManager.getOnlineUsers()));
    }
}
