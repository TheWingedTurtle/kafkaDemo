package example.org;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerDemoWithKeys {

    public static void main(String[] args) {

        Logger logger = LoggerFactory.getLogger(ProducerDemoWithKeys.class);
        //producer properties
        int i = 0;
        String bootstrapServers = "localhost:9092";
        String topic = "first_topic";
        String message = "message";
        String key;
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // producer
        KafkaProducer <String, String> producer = new KafkaProducer<String, String>(properties);


        for ( i = 0; i < 10; i++) {
            //key = String.valueOf(i%2);
            //send data
            //logger.info(key);
            ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, String.valueOf(i), message + String.valueOf(i));
            producer.send(record, (metadata, exception) -> {
                if (exception == null) {
                    if (null != metadata) {
                        logger.info("Recieved the metadata on topic" + metadata.topic() +
                                "\nReceived partition: " + metadata.partition() +
                                "\nReceived timestamp: " + metadata.timestamp() +
                                "\nRecieved offset: " + metadata.offset());
                    }

                } else {
                    exception.printStackTrace();
                }
            });
        }
        producer.flush();
        producer.close();



        //consumer
    }
}
