import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class HelloWorld {

    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();

        context.addRoutes(new RouteBuilder() {
            public void configure() {
                // Route for the proxy
                from("direct:proxy")
                        .process(new Processor() {
                            public void process(Exchange exchange) throws Exception {
                                String request = exchange.getIn().getBody(String.class);
                                System.out.println("Message received: " + request);
                                exchange.getOut().setBody("Hello " + request);
                            }
                        })
                        .to("direct:end");

                // Route for the sender
                from("direct:start")
                        .to("direct:proxy");

                // Route for the receiver
                from("direct:end")
                        .process(new Processor() {
                            public void process(Exchange exchange) throws Exception {
                                String message = exchange.getIn().getBody(String.class);
                                System.out.println("Message received: " + message);
                            }
                        });
            }
        });

        context.start();
        context.createProducerTemplate().sendBody("direct:start", "world!");
        context.stop();
    }
}
