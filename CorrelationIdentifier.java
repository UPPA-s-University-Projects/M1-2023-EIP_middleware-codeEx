import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import java.util.UUID;

public class HelloWorld {

    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();

        context.addRoutes(new RouteBuilder() {
            public void configure() {
                // Route to send request message with a correlation ID
                from("direct:start")
                        .setHeader("correlationId", constant(UUID.randomUUID().toString()))
                        .to("jms:queue:requests");

                // Route to receive response message with a correlation ID
                from("jms:queue:responses")
                        .process(exchange -> {
                            String correlationId = exchange.getIn().getHeader("correlationId", String.class);
                            String message = exchange.getIn().getBody(String.class);
                            System.out.println("Response for correlation ID " + correlationId + ": " + message);
                        });
            }
        });

        context.start();
        context.createProducerTemplate().sendBody("direct:start", "Hello World!");
        Thread.sleep(1000);
        context.stop();
    }
}