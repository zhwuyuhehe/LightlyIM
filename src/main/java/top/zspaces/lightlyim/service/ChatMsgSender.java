package top.zspaces.lightlyim.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.zspaces.lightlyim.config.rabbitMQ;
import top.zspaces.lightlyim.entity.ChatMsg;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ChatMsgSender {
    private final RabbitTemplate rabbitTemplate;

    public void send(ChatMsg chatMsg) {

        try{
            rabbitTemplate.convertAndSend(
                    rabbitMQ.EXCHANGE_NAME,
                    rabbitMQ.ROUTING_KEY,
                    chatMsg);
        }
        catch (Exception e){
            log.error(e.getMessage());
        }
    }
}
