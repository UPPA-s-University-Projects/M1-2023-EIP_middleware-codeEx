import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class HelloWorld {

    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();

        context.addRoutes(new RouteBuilder() {
            public void configure() {
                // Route to receive message from JMS queue
                from("jms:queue:input")
                        .process(exchange -> {
                            String message = exchange.getIn().getBody(String.class);
                            System.out.println("Received from JMS: " + message);
                        })
                        .to("kafka:output");

                // Route to receive message from Kafka topic
                from("kafka:input")
                        .process(exchange -> {
                            String message = exchange.getIn().getBody(String.class);
                            System.out.println("Received from Kafka: " + message);
                        })
                        .to("jms:queue:output");
            }
        });

        context.start();
        context.createProducerTemplate().sendBody("jms:queue:input", "Hello World!");
        Thread.sleep(1000);
        context.stop();
    }
}