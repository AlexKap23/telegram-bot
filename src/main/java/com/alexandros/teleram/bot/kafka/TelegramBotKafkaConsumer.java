package com.alexandros.teleram.bot.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import javax.annotation.PostConstruct;

@Service
public class TelegramBotKafkaConsumer {

    @Value("${bot.reservation.chat}")
    private long reservationBotChat;

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServer;

    Logger logger = LoggerFactory.getLogger(TelegramBotKafkaConsumer.class);

//    @PostConstruct
//    public void init(){
//        consumeKafkaMessages();
//    }


    @KafkaListener(topics = "test", groupId = "console-consumer-11642")
    public void listen(String message) {
        logger.info("Received Messasge in group - group-id: " + message);
    }

    public void consumeKafkaMessages(){
        String groupId = "console-consumer-11642";
        String topic = "test";
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, getBootstrapServer());
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG,groupId);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");

        KafkaConsumer<String,String> consumer = new KafkaConsumer<String,String>(properties);
        consumer.subscribe(Arrays.asList(topic));

        while (true){
            ConsumerRecords<String,String> records = consumer.poll(Duration.ofMillis(100));
            for(ConsumerRecord<String,String> record:records){
                String message = "Key: "+record.key() + ", Value: "+record.value();
                logger.info("Consumer: " + message);
            }
        }
    }

    public long getReservationBotChat() {
        return reservationBotChat;
    }

    public void setReservationBotChat(long reservationBotChat) {
        this.reservationBotChat = reservationBotChat;
    }

    public String getBootstrapServer() {
        return bootstrapServer;
    }

    public void setBootstrapServer(String bootstrapServer) {
        this.bootstrapServer = bootstrapServer;
    }
}
