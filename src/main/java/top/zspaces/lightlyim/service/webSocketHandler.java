package top.zspaces.lightlyim.service;

import com.alibaba.fastjson2.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;
import top.zspaces.lightlyim.entity.ChatMsg;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class webSocketHandler implements WebSocketHandler {
    private final WebSocketSessionManager webSocketSessionManager;
    private final ChatMsgSender chatMsgSender;

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        return session
                .getHandshakeInfo()
                .getPrincipal()
                .flatMap(principal -> {
                    String email = principal.getName();
                    if (email == null || email.isBlank()) {
                        log.warn("WebSocket链接异常，无session信息！");
                        return session.close();
                    }

                    webSocketSessionManager.addSession(email, session);
                    log.info("用户[{}]已连接", email);
                    webSocketSessionManager.broadcastSystemMsg("用户[" + email + "]已上线");
                    webSocketSessionManager.broadcastOnlineUsers();
                    return session.receive()
                            .map(WebSocketMessage::getPayloadAsText)
                            .flatMap(msg->{
                                ChatMsg chatMsg = JSON.parseObject(msg, ChatMsg.class);
                                chatMsg.setFrom(email);
                                chatMsg.setTimestamp(System.currentTimeMillis());
                                log.warn(JSON.toJSONString(chatMsg));

                                chatMsgSender.send(chatMsg);
                                return Mono.empty();
                            })
                            .doFinally(signalType -> {
                                log.info("用户断开链接：{}", email);
                                webSocketSessionManager.broadcastSystemMsg("用户[" + email + "]已下线");
                                webSocketSessionManager.removeSession(email);
                                webSocketSessionManager.broadcastOnlineUsers();
                            })
                            .then();
                }).switchIfEmpty(Mono.defer(() -> {
                    log.warn("WS链接失败，登录状态异常！");
                    log.error("Headers:{}", session.getHandshakeInfo().getHeaders());
                    return session.close();
                }));
    }


}
