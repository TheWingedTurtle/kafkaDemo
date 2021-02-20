package example.org;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class ConsumerDemoWithThreads {


    public static void main(String[] args) {
        new  ConsumerDemoWithThreads().run();
    }
    private void run(){
        Logger logger = LoggerFactory.getLogger(ConsumerDemoWithThreads.class.getName());
        String bootStrapServer = "localhost:9092";
        String groupId = "fifth_application";
        String topic = "first_topic";
        CountDownLatch latch = new CountDownLatch(1);

        Runnable consumerRunner = new ConsumerRunner(bootStrapServer, groupId, topic, latch);

        Thread consumerThread = new Thread(consumerRunner);
        consumerThread.start();

        Runtime.getRuntime().addShutdownHook(new Thread( () -> {
            logger.info("Interruption hook caught");
            ((ConsumerRunner) consumerRunner).shutdown();
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }));


        try {
            latch.await();
        } catch (InterruptedException e) {
            //e.printStackTrace();
            logger.error("application interrupted");
        } finally {
            logger.error("application closing");
        }

    }

    public class ConsumerRunner implements Runnable{

        private Logger logger = LoggerFactory.getLogger(ConsumerRunner.class.getName());
        private CountDownLatch latch;
        private KafkaConsumer<String, String> consumer;
        public ConsumerRunner( String bootStrapServer, String groupId, String topic, CountDownLatch latch){
            this.latch = latch;
            Properties properties = new Properties();
            properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServer);
            properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
            properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
            consumer = new KafkaConsumer<String, String>(properties);
            consumer.subscribe(Collections.singleton(topic));
        }

        @Override
        public void run() {
            try {
                while (true){
                    ConsumerRecords<String, String> records =  consumer.poll(Duration.ofMillis(100));
                    records.forEach(record -> {
                        logger.info(record.key() + record.value());
                        logger.info(record.partition() + record.topic());
                    });
                }

            } catch (WakeupException e){
                logger.info("interrupted wakeup call");
            } finally {
                consumer.close();
                latch.countDown();
            }
        }

        public void shutdown(){
            consumer.wakeup();
        }
    }
}
