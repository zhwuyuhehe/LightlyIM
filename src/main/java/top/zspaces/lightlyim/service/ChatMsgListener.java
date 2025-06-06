package top.zspaces.lightlyim.service;

import com.alibaba.fastjson2.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.zspaces.lightlyim.config.rabbitMQ;
import top.zspaces.lightlyim.entity.ChatMsg;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ChatMsgListener {
    private final WebSocketSessionManager webSocketSessionManager;
    private final ChatSave chatSave;
    @RabbitListener(queues = rabbitMQ.QUEUE_NAME)
    public void handleChatMsg(ChatMsg chatMsg) {
        chatSave.saveChatMsgToRedis(chatMsg);
        webSocketSessionManager.broadcastUserMsg(JSON.toJSONString(chatMsg));
    }

}
