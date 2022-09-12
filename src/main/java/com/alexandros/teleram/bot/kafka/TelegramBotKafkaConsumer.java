package com.alexandros.teleram.bot.kafka;

import com.alexandros.teleram.bot.telegram.bot.AlexKapBot;
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
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

@Service
public class TelegramBotKafkaConsumer {

    @Autowired
    private AlexKapBot alexKapBot;

    @Value("${bot.reservation.chat}")
    private long reservationBotChat;

    @Value("${kafka.bootstrap.server}")
    private String bootstrapServer;

    Logger logger = LoggerFactory.getLogger(LoggerFactory.class);

    public void consumeKafkaMessages(){
        String groupId = "consumerBot";
        String topic = "temp";
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
                alexKapBot.sendMessageToUser(getReservationBotChat(),message);
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
