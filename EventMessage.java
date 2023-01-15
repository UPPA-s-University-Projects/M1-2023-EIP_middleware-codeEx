import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class HelloWorld {

    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();

        context.addRoutes(new RouteBuilder() {
            public void configure() {
                // Route to send event message to a JMS topic
                from("direct:start")
                        .to("jms:topic:events");

                // Route to receive event message from a JMS topic
                from("jms:topic:events")
                        .process(exchange -> {
                            String message = exchange.getIn().getBody(String.class);
                            System.out.println("Event Received: " + message);
                        });
            }
        });

        context.start();
        context.createProducerTemplate().sendBody("direct:start", "Hello World event!");
        Thread.sleep(1000);
        context.stop();
    }
}