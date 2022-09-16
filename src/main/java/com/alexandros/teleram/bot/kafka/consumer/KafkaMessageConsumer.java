package com.alexandros.teleram.bot.kafka.consumer;

import com.alexandros.teleram.bot.dto.MessageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaMessageConsumer {

    @KafkaListener(
        topics = "${kafka.topic}",
        containerFactory = "messageKafkaListenerContainerFactory")
    public void greetingListener(@Payload MessageDto messageDto, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
        @Header(KafkaHeaders.GROUP_ID) String groupId) {
        log.info("New message arrived: {} with partition {} and groupId {}", messageDto, partition, groupId);
    }
}
