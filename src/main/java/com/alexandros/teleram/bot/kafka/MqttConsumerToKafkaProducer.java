package com.alexandros.teleram.bot.kafka;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Properties;
import javax.annotation.PostConstruct;


@Component
public class MqttConsumerToKafkaProducer {


    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServer;
    Logger logger = LoggerFactory.getLogger(LoggerFactory.class);

    public void transferMessages(){
        try{
            Properties properties = new Properties();
            properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
            properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
            properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

            KafkaProducer<String, String> kafkaProducer = new KafkaProducer<String, String>(properties);
            ProducerRecord<String, String> record = new ProducerRecord<>("temp", "22");
            kafkaProducer.send(record);
            kafkaProducer.flush();
            kafkaProducer.close();

           /* MQTT mqtt = new MQTT();
            mqtt.setHost(cmd.getOptionValue(MQTT_BROKER_HOST, "localhost"), Integer.parseInt(cmd.getOptionValue(MQTT_BROKER_PORT, "1883")));

            BlockingConnection connection = mqtt.blockingConnection();
            connection.connect();

            String topicsArg = cmd.getOptionValue(MQTT_BROKER_TOPICS, "illumination");
            List<Topic> topicsList = new ArrayList<Topic>();
            String[] topics = topicsArg.split(",");
            for(String topic:topics) {
                topicsList.add(new Topic(topic, QoS.AT_LEAST_ONCE));
            }

            Topic[] mqttTopics = topicsList.toArray(new Topic[]{});
            byte[] qoses = connection.subscribe(mqttTopics);*/
        }catch (Exception e){
            logger.error("Exception caught on bridge",e);
        }
    }
}
