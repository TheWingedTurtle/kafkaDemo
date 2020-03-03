package com.abhi.example.org;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerDemoWithCallback {

    public static void main(String[] args) {

        Logger logger = LoggerFactory.getLogger(ProducerDemoWithCallback.class);
        //producer properties
        String bootstrapServers = "localhost:9092";
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // producer
        KafkaProducer <String, String> producer = new KafkaProducer<String, String>(properties);


        //send data
        ProducerRecord<String, String> record = new ProducerRecord<String, String>("first_topic", "hello world");
        producer.send(record, (metadata, exception) -> {
            if (exception != null){
                logger.info("Recieved the metadata on topic" + metadata.topic() +
                        "\nReceived partition: " + metadata.partition() +
                        "\nReceived timestamp: " + metadata.timestamp() +
                        "\nRecieved offset: " + metadata.offset());

            } else {
                logger.error("Error in receiving metadata" + metadata.toString());
            }
        });

        producer.flush();

        producer.close();

        //consumer
    }
}
