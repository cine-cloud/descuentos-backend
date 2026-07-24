package com.unrn.descuentos.service;

import com.unrn.descuentos.event.dto.Event;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DescuentoEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final String exchangeName;
    private final String routingKey;

    public DescuentoEventPublisher(
            RabbitTemplate rabbitTemplate,
            @Value("${app.rabbitmq.exchange}") String exchangeName,
            @Value("${app.rabbitmq.routing.key}") String routingKey) {

        this.rabbitTemplate = rabbitTemplate;
        this.exchangeName = exchangeName;
        this.routingKey = routingKey;
    }

    public void enviarEvento(Event<Integer, ?> evento) {
        rabbitTemplate.convertAndSend(exchangeName, routingKey, evento);
        System.out.println(
                "Evento publicado en " + exchangeName +
                " con routing key " + routingKey +
                ": " + evento.getEventType() +
                " para descuento " + evento.getKey()
        );
    }
}
