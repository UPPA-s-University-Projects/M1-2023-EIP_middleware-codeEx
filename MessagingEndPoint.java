import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class HelloWorld {

    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();

        context.addRoutes(new RouteBuilder() {
            public void configure() {
                // Route to send message to a JMS queue and a topic
                from("direct:start")
                        .to("jms:queue:orders")
                        .to("jms:topic:notifications");

                // Route to receive message from a file
                from("file:data/messages?fileName=message.txt")
                        .process(exchange -> {
                            String message = exchange.getIn().getBody(String.class);
                            System.out.println("Received from file: " + message);
                        });

                // Route to receive message from a JMS queue
                from("jms:queue:orders")
                        .process(exchange -> {
                            String message = exchange.getIn().getBody(String.class);
                            System.out.println("Received from JMS queue: " + message);
                        });

                // Route to receive message from a JMS topic
                from("jms:topic:notifications")
                        .process(exchange -> {
                            String message = exchange.getIn().getBody(String.class);
                            System.out.println("Received from JMS topic: " + message);
                        });
            }
        });

        context.start();
        context.createProducerTemplate().sendBody("direct:start", "Hello World!");
        Thread.sleep(1000);
        context.stop();
    }
}