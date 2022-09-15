package com.alexandros.teleram.bot.kafka;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


@Component
public class MqttConsumerToKafkaProducer {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServer;

    @Autowired
    private final KafkaTemplate<String, String> kafkaTemplate;
    Logger logger = LoggerFactory.getLogger(LoggerFactory.class);

    public MqttConsumerToKafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void transferMessages(){
        try{

            kafkaTemplate.send("temp", "22")
                .addCallback(
                    result -> logger.info("Message sent to topic: {}", 22),
                    ex -> logger.error("Failed to send message", ex)
                );

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
