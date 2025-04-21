package top.zspaces.lightlyim.config;

import com.alibaba.fastjson2.JSON;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import top.zspaces.lightlyim.entity.ChatMsg;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

public class FastJson2MessageConverter implements MessageConverter {

    //    重写了消息转换器，使用FastJSON2
    @Override
    public Message toMessage(Object o, MessageProperties messageProperties) throws MessageConversionException {
        byte[] bytes = JSON.toJSONString(o).getBytes(StandardCharsets.UTF_8);
        messageProperties.setContentLength(bytes.length);
        messageProperties.setContentEncoding("UTF-8");
        messageProperties.setContentType("application/json");
        return new Message(bytes, messageProperties);
    }


    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        String body = new String(message.getBody(), StandardCharsets.UTF_8);
        return JSON.parseObject(body, ChatMsg.class);
    }
}
