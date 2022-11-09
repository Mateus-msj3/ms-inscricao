package com.io.github.msj.msinscricao.infra.mqueue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.io.github.msj.msinscricao.dto.request.InscricaoFinalizacaoRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FinalizarInscricaoPublisher {
    private final RabbitTemplate rabbitTemplate;
    private final Queue queue;

    public void finalizarInscricao(InscricaoFinalizacaoRequestDTO dto) throws JsonProcessingException {
        var json = converterParaJson(dto);
        rabbitTemplate.convertAndSend(queue.getName(), json);
    }

    private String converterParaJson(InscricaoFinalizacaoRequestDTO dto) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        var json = objectMapper.writeValueAsString(dto);
        return json;
    }

}
