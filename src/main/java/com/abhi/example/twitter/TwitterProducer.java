package com.abhi.example.twitter;

import com.google.common.collect.Lists;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class TwitterProducer {

    Logger logger = LoggerFactory.getLogger(TwitterProducer.class.getName());

    String consumerKey = "mHGr8vyj7RrVP5DsPmYkp6kyb",
            consumerSecret = "DqnNyZoP2t4QAyo7PWbgwtbsepqvXsUK2hLGL4giEJTnvMAm3Q",
            token = "1232007140769816577-Uyk6s0m0nt4O87An4RBfCHaQqJ2aSv",
            tokenSecret = "V0SIcmRK9U6b3ZCYl5IaQCNYcvAr61mRGHsxXjUmi1xsC";

    List<String> terms = Lists.newArrayList("kafka");


    public TwitterProducer(){}

    public static void main(String[] args) {
        new TwitterProducer().run();
    }

    private void run(){

        /** Set up your blocking queues: Be sure to size these properly based on expected TPS of your stream */
        BlockingQueue<String> msgQueue = new LinkedBlockingQueue<String>(1000);
        //Creating twitter client
        Client client = createTwitterClient(msgQueue);
        client.connect();

        //creating a kafka producer

        KafkaProducer<String, String> kafkaProducer = createKafkaProducer();

        //sending tweets
        // on a different thread, or multiple different threads....
        while (!client.isDone()) {
            String msg = null;
            try {
                msg = msgQueue.poll(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
                client.stop();
            }
            if (null != msg){
                kafkaProducer.send(new ProducerRecord<>("twitter_tweets",null, msg), ((recordMetadata, e) -> {
                    if (e == null){
                        logger.info("Sent data successfully");
                    } else {
                        logger.error("Data send unsuccesfull", e);
                    }
                }));
//                logger.info(msg);
            }
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutting down application");
            client.stop();
            kafkaProducer.close();
            logger.info("End done");
        }));
        logger.info("End of application");
    }



    private Client createTwitterClient(BlockingQueue<String> msgQueue){

        //Creating a twitter client
        /** Declare the host you want to connect to, the endpoint, and authentication (basic auth or oauth) */
        Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
        StatusesFilterEndpoint hosebirdEndpoint = new StatusesFilterEndpoint();
// Optional: set up some followings and track terms
        hosebirdEndpoint.trackTerms(terms);

// These secrets should be read from a config file
        Authentication hosebirdAuth = new OAuth1(consumerKey, consumerSecret, token, tokenSecret);

        ClientBuilder builder = new ClientBuilder()
                .name("Hosebird-Client-01")                              // optional: mainly for the logs
                .hosts(hosebirdHosts)
                .authentication(hosebirdAuth)
                .endpoint(hosebirdEndpoint)
                .processor(new StringDelimitedProcessor(msgQueue));

        Client hosebirdClient = builder.build();
        return hosebirdClient;
    }

    private KafkaProducer<String, String> createKafkaProducer(){
        //producer properties
        String bootstrapServers = "localhost:9092";
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // producer
        KafkaProducer <String, String> producer = new KafkaProducer<String, String>(properties);

        return producer;

    }
}
