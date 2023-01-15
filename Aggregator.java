import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import java.util.UUID;

public class HelloWorld {

    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();

        context.addRoutes(new RouteBuilder() {
            public void configure() {
                // Route to send multiple messages with a correlation ID
                from("direct:start")
                        .setHeader("correlationId", constant(UUID.randomUUID().toString()))
                        .to("jms:queue:parts");

                // Route to aggregate messages with the same correlation ID
                from("jms:queue:parts")
                        .aggregate(header("correlationId"), new BodyAggregationStrategy())
                        .completionSize(3)
                        .process(exchange -> {
                            String correlationId = exchange.getIn().getHeader("correlationId", String.class);
                            String message = exchange.getIn().getBody(String.class);
                            System.out.println("Aggregated message for correlation ID " + correlationId + ": " + message);
                        });
            }
        });

        context.start();
        context.createProducerTemplate().sendBodyAndHeader("direct:start", "Hello World 1!", "correlationId", "123");
        context.createProducerTemplate().sendBodyAndHeader("direct:start", "Hello World 2!", "correlationId", "123");
        context.createProducerTemplate().sendBodyAndHeader("direct:start", "Hello World 3!", "correlationId", "123");
        Thread.sleep(1000);
        context.stop();
    }
}

class BodyAggregationStrategy implements AggregationStrategy {
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        if (oldExchange == null) {
            return newExchange;
        }
        String oldBody = oldExchange.getIn().getBody(String.class);
        String newBody = newExchange.getIn().getBody(String.class);
        oldExchange.getIn().setBody(oldBody + " " + newBody);
        return oldExchange;
    }
}
In this example, the program sends multiple messages from the "direct:start" endpoint to a JMS queue and sets a unique correlation identifier using a UUID. Then it aggregates all the messages with the same correlation identifier into a single message and prints it to the console.

It uses Apache Camel as a framework for implementing the Aggregator pattern. The route starts from the "direct:start" endpoint and sends multiple messages to the "jms:queue:parts" endpoint. Then it uses the aggregate method to group all messages with the same correlation identifier and the completion size set to 3.

This example shows how the Aggregator pattern can be used to group related messages together and then process them as a single unit, improving the efficiency and consistency of the system.




