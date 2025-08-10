package com.phumlanidev.productservice.publisher;

import com.phumlanidev.commonevents.events.ProductCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class ProductEventPublisher {
  private final KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;
  private static final String TOPIC = "product.created";

  public void publishProductCreated(ProductCreatedEvent event) {
    log.info("📤 Sending product created event: {}", event);
    kafkaTemplate.send(TOPIC, event);
  }
}
