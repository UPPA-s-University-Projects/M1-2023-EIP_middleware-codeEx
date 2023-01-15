import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class HelloWorld {

    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();

        context.addRoutes(new RouteBuilder() {
            public void configure() {
                // Route to filter messages based on the type header
                from("direct:start")
                        .filter(header("type").isEqualTo("A"))
                        .process(exchange -> {
                            String message = exchange.getIn().getBody(String.class);
                            System.out.println("Processing message of type A: " + message);
                        })
                        .end()
                        .filter(header("type").isEqualTo("B"))
                        .process(exchange -> {
                            String message = exchange.getIn().getBody(String.class);
                            System.out.println("Rejecting message of type B: " + message);
                        });
            }
        });

        context.start();
        context.createProducerTemplate().sendBodyAndHeader("direct:start", "Hello World!", "type", "A");
        context.createProducerTemplate().sendBodyAndHeader("direct:start", "Hello World!", "type", "B");
        context.createProducerTemplate().sendBody("direct:start", "Hello World!");
        Thread.sleep(1000);
        context.stop();
    }
}