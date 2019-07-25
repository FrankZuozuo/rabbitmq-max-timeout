package com.wretchant.rabbitmqmaxtimeout.config;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * @author Created by 谭健 on 2019/7/24. 星期三. 9:58.
 * © All Rights Reserved.
 */

@Slf4j
@Component
public class QueueListener {


    @Bean("a")
    DirectExchange directExchange() {
        return new DirectExchange("a");
    }

    @Bean("t")
    Queue queue() {
        return new Queue("t");
    }


    @Bean
    Binding binding(Queue t, DirectExchange a) {
        return BindingBuilder.bind(t).to(a).with("t");
    }


    @RabbitListener(queues = "t")
    public void confirm(String msg) {
        log.info("收到消息 ： {} ", msg);
    }


    // 延迟队列

    @Bean("prod")
    DirectExchange prod() {
        return new DirectExchange("prod");
    }

    @Bean("consu")
    DirectExchange consu() {
        return new DirectExchange("consu");
    }

    @Bean("prodQ")
    Queue prodQ() {
        Map<String, Object> args = Maps.newHashMap();
        args.put("x-dead-letter-exchange", "consu");
        args.put("x-dead-letter-routing-key", "consuQ");
        args.put("x-message-ttl", Long.MAX_VALUE);

        return new Queue("prodQ", true, false, false, args);
    }

    @Bean("consuQ")
    Queue consuQ() {
        return new Queue("consuQ");
    }

    @Bean
    Binding send(Queue prodQ, DirectExchange prod) {
        return BindingBuilder.bind(prodQ).to(prod).with("prodQ");
    }

    @Bean
    Binding receive(Queue consuQ, DirectExchange consu) {
        return BindingBuilder.bind(consuQ).to(consu).with("consuQ");
    }

    @RabbitListener(queues = "consuQ")
    public void exec(String msg) {
        log.info("我的延迟队列，接收时间为 {} ， 消息 {}", DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now()), msg);
    }
}
