package com.wretchant.rabbitmqmaxtimeout;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@Slf4j
@SpringBootApplication
public class RabbitmqMaxTimeoutApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbitmqMaxTimeoutApplication.class, args);
    }



    @Autowired
    private AmqpTemplate amqpTemplate;


    @Bean
    ApplicationRunner applicationRunner(){
        return args -> {
            amqpTemplate.convertAndSend("a","t","消息");
            amqpTemplate.convertAndSend("prod","prodQ","我是消息内容");
        };
    }

}
