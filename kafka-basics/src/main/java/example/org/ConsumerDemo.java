package example.org;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class ConsumerDemo {
    int a;
    String b;

    public static void main(String[] args) {

        Logger logger = LoggerFactory.getLogger(ConsumerDemo.class.getName());
        String bootStrapServer = "localhost:9092";
        String groupId = "fourth_application";
        String topic = "first_topic";

        ConsumerDemo c = new ConsumerDemo();

        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServer);
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        KafkaConsumer consumer = new KafkaConsumer(properties);

        consumer.subscribe(Collections.singleton(topic));

        while (true){
            ConsumerRecords<String, String> records =  consumer.poll(Duration.ofMillis(100));
            records.forEach(record -> {
                logger.info(record.key() + record.value());
                logger.info(record.partition() + record.topic());
            });
        }
    }
}
