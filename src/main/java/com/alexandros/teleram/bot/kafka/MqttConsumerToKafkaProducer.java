package com.alexandros.teleram.bot.kafka;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Properties;


@Service
public class MqttConsumerToKafkaProducer {

    private static final String MQTT_BROKER_TOPICS = "mqttbrokertopics";
    private static final String MQTT_BROKER_PORT = "mqttbrokerport";
    private static final String MQTT_BROKER_HOST = "mqttbrokerhost";
    private static final String SERIALIZER_CLASS = "serializerclass";
    private static final String BROKER_LIST = "brokerlist";

    Logger logger = LoggerFactory.getLogger(LoggerFactory.class);

    public void transferMessages(){
        try{
            String bootstrapServers = "127.0.0.1:9092";
            Properties properties = new Properties();
            properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
            properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
            properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());

            KafkaProducer<String,String> sensorDataProducer = new KafkaProducer<String, String>(properties);

            //some data from mqtt to test the kafka client
            ProducerRecord<String,String> record = new ProducerRecord<String,String>("temp","22");
            sensorDataProducer.send(record, new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    logger.info("Sending data to kafka onCompletion");
                    if(Objects.isNull(e)){
                        logger.info("Sending data to kafka completed successfully");
                    }else {
                        logger.error("Error while producing messages",e);
                    }
                }
            });
            sensorDataProducer.flush();
            sensorDataProducer.close();
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
