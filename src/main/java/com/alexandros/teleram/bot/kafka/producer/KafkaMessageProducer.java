package com.alexandros.teleram.bot.kafka.producer;

import com.alexandros.teleram.bot.dto.MessageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
@Slf4j
public class KafkaMessageProducer {

    @Value("${kafka.topic}")
    private String kafkaTopic;

    @Autowired
    private KafkaTemplate<String, MessageDto> kafkaTemplate;

    public void sendMessage(MessageDto message) {
        ListenableFuture<SendResult<String, MessageDto>> future = kafkaTemplate.send(kafkaTopic, message);
        future.addCallback(new ListenableFutureCallback<SendResult<String, MessageDto>>() {
            @Override
            public void onFailure(Throwable ex) {
                log.info("Unable to send message=["
                    + message + "] due to : " + ex.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, MessageDto> result) {
                log.info("Sent message=[" + message +
                    "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }
        });

    }

}
