package com.alexandros.teleram.bot;

import com.alexandros.teleram.bot.dto.MessageDto;
import com.alexandros.teleram.bot.kafka.producer.KafkaMessageProducer;
import lombok.extern.slf4j.Slf4j;
import org.fusesource.mqtt.client.BlockingConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.Message;
import org.fusesource.mqtt.client.QoS;
import org.fusesource.mqtt.client.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;

@Service
@Slf4j
public class MqttToKafkaTranslator {

    @Value("${mqtt.mosquito.host}")
    private String mqttHost;

    @Value("${mqtt.mosquito.port}")
    private String mqttPort;

    @Autowired
    private KafkaMessageProducer kafkaMessageProducer;

    @PostConstruct
    public void produceMessagesToKafka(){

        try{
            MQTT mqtt = new MQTT();
            mqtt.setHost(mqttHost,Integer.parseInt(mqttPort));
            BlockingConnection connection = mqtt.blockingConnection();
            connection.connect();
            List<Topic> topicsList = new ArrayList<Topic>();
            topicsList.add(new Topic("temp", QoS.AT_LEAST_ONCE)); //todo fix this to be parameterizable
            byte[] qoses = connection.subscribe(topicsList.toArray(new Topic[]{}));

            //TODO check how it will run
            while(connection.isConnected()){
                Message message = connection.receive();
                byte[] payload = message.getPayload();
                String strPayload = new String(payload);
                // process the message then:
                message.ack();
                MessageDto kafkaMessage = new MessageDto(message.getTopic(),strPayload);
                kafkaMessageProducer.sendMessage(kafkaMessage);
            }

        }catch (Exception e){
            log.error("Exception caught while producing messages from mqtt broker to kafka");
        }


    }
}
