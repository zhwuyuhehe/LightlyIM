package top.zspaces.lightlyim.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class rabbitMQ {
    public static final String EXCHANGE_NAME = "chat.exchange";
    public static final String QUEUE_NAME = "chat.queue";
    public static final String ROUTING_KEY = "chat.message";

    @Bean
    public TopicExchange chatExchange() {
//        定义广播交换机
        return new TopicExchange(EXCHANGE_NAME, true, false);
    }

    @Bean
    public Queue chatQueue() {
//        定义一个队列
        return QueueBuilder.durable(QUEUE_NAME).build();
    }

    @Bean
    public Binding chatBinding(Queue chatQueue, TopicExchange chatExchange) {
//        绑定队列到交换机
        return BindingBuilder.bind(chatQueue).to(chatExchange).with(ROUTING_KEY);
    }

    @Bean
    public FastJson2MessageConverter fastJson2MessageConverter() {
        return new FastJson2MessageConverter();
    }

//    使用消息转换器
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,FastJson2MessageConverter fastJson2MessageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(fastJson2MessageConverter);
        return rabbitTemplate;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory, FastJson2MessageConverter fastJson2MessageConverter) {
        SimpleRabbitListenerContainerFactory factory =new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(fastJson2MessageConverter);
        return factory;

    }
}
