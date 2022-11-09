package com.io.github.msj.msinscricao.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {

    @Value("${mq.queues.finalizar-inscricao}")
    private String finalizarInscricaoFila;

    @Bean
    public Queue queueFinalizarInscricao() {
        return new Queue(finalizarInscricaoFila, true);
    }
}
