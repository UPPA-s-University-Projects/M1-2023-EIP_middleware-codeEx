import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class HelloWorld {

    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();
        context.addRoutes(new RouteBuilder() {
            public void configure() {
                // Route for handling document messages
                from("activemq:documentQueue")
                        .process(exchange -> {
                            String document = exchange.getIn().getBody(String.class);
                            System.out.println(document);
                        });
            }
        });

        context.start();
        context.createProducerTemplate().sendBody("activemq:documentQueue", "Hello World!");
        Thread.sleep(1000);
        context.stop();
    }
}