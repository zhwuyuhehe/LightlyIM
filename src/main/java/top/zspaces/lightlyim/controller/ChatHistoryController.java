package top.zspaces.lightlyim.controller;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import top.zspaces.lightlyim.entity.ChatMsg;
import top.zspaces.lightlyim.mapper.ChatMsgMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
public class ChatHistoryController {

    private final StringRedisTemplate redisTemplate;
    private final ChatMsgMapper chatMsgMapper;

    public ChatHistoryController(StringRedisTemplate redisTemplate, ChatMsgMapper chatMsgMapper) {
        this.redisTemplate = redisTemplate;
        this.chatMsgMapper = chatMsgMapper;
    }

    @PostMapping("/chatHistory")
    public List<ChatMsg> getChatHistory(@RequestBody Map<String,String> info) {
        int offset = Integer.parseInt(info.get("offset"));
        int limit = Integer.parseInt(info.get("limit"));
        List<ChatMsg> result = new ArrayList<>();
        if (offset == 0){
            List<String> cached = redisTemplate.opsForList().range("chat:history",0,-1);
            if (cached != null && !cached.isEmpty()){
                List<ChatMsg> redisMsgs = cached.stream()
                        .map(json -> JSON.parseObject(json, ChatMsg.class))
                        .toList();
                result.addAll(redisMsgs);
            }
        }
        List<ChatMsg> dbMsgs = chatMsgMapper.selectHistory(offset, limit);
        result.addAll(dbMsgs);
        return result;
    }
}
