import org.apache.kafka.clients.producer.*;

import java.util.Properties;

public class HelloWorld {

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<>(props);

        ProducerRecord<String, String> record = new ProducerRecord<>("my-topic", "Hello World!");
        producer.send(record, (metadata, exception) -> {
            if (exception == null) {
                System.out.println("Message sent to topic: " + metadata.topic());
            } else {
                exception.printStackTrace();
            }
        });

        producer.close();
    }
}