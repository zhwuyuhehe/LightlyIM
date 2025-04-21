package top.zspaces.lightlyim.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class WebSocketSessionManager {

    private final Map<String, WebSocketSession> onlineUsers = new ConcurrentHashMap<>();

    public void addSession(String email, WebSocketSession session) {
        onlineUsers.put(email, session);
    }

    public void removeSession(String email) {
        onlineUsers.remove(email);
    }

    public WebSocketSession getSession(String email) {
        return onlineUsers.get(email);
    }

    public void broadcastUserMsg(String message) {

        for (WebSocketSession session : onlineUsers.values()) {
            session.send(Mono.just(session.textMessage(message)))
                    .subscribe(null, e -> log.error("发送失败！", e));
        }
    }

    public Set<String> getOnlineUsers() {
        return onlineUsers.keySet();
    }

    public void broadcastOnlineUsers(){
        Set<String> online = getOnlineUsers();
        JSONObject msg = new JSONObject();
        msg.put("online", online);
        msg.put("type", "online");

        String json = JSON.toJSONString(msg);
        onlineUsers.values().forEach(session ->
                session.send(Mono.just(session.textMessage(json))).subscribe(null, e -> {log.error(e.toString(), e);}));

    }

    public void broadcastSystemMsg(String content) {
        String json = "{\"type\":\"system\",\"content\":\"" + content + "\"}";
        onlineUsers.values().forEach(webSocketSession -> webSocketSession.send(Mono.just(webSocketSession.textMessage(json))).subscribe());
    }

}
