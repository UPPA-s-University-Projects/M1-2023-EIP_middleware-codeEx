import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class HelloWorld {

    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();

        context.addRoutes(new RouteBuilder() {
            public void configure() {
                // Route to split a message into multiple messages based on a delimiter
                from("direct:start")
                        .split(body().tokenize(","))
                        .process(exchange -> {
                            String message = exchange.getIn().getBody(String.class);
                            System.out.println("Processing message: " + message);
                        });
            }
        });

        context.start();
        context.createProducerTemplate().sendBody("direct:start", "Hello,World,!");
        Thread.sleep(1000);
        context.stop();
    }
}