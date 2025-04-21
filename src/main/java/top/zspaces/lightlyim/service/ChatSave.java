package top.zspaces.lightlyim.service;

import com.alibaba.fastjson2.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.zspaces.lightlyim.entity.ChatMsg;
import top.zspaces.lightlyim.mapper.ChatMsgMapper;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ChatSave {
    private final StringRedisTemplate redisTemplate;
    private final ChatMsgMapper chatMsgMapper;

    public void saveChatMsgToRedis(ChatMsg chatMsg) {
        String redisKey = "chat:history";
        redisTemplate.opsForList().rightPush(redisKey, JSON.toJSONString(chatMsg));
    }

    @Scheduled(fixedRate = 120000)
    public void flushChatMsg() {
        List<String> redisMsg = redisTemplate.opsForList().range("chat:history", 0, -1);
        if (redisMsg == null || redisMsg.isEmpty()) return;

        List<ChatMsg> chatMsgs = redisMsg.stream()
                .map(stringToJson -> JSON.parseObject(stringToJson, ChatMsg.class))
                .toList();
        int lastMaxId = chatMsgMapper.GetMaxId();
       for (ChatMsg chatMsg : chatMsgs) {
           chatMsg.setId(++lastMaxId);
       }

       chatMsgMapper.insertBatch(chatMsgs);
       redisTemplate.delete("chat:history");
    }
}
