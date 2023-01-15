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
                // Route to handle request and send reply
                from("direct:start")
                        .process(new Processor() {
                            public void process(Exchange exchange) throws Exception {
                                String request = exchange.getIn().getBody(String.class);
                                String response = "Hello " + request;
                                exchange.getOut().setBody(response);
                            }
                        })
                        .to("direct:end");

                // Route to log request and reply
                from("direct:end")
                        .process(new Processor() {
                            public void process(Exchange exchange) throws Exception {
                                String request = exchange.getIn().getBody(String.class);
                                System.out.println("Request: " + request);
                            }
                        });
            }
        });

        context.start();
        String response = context.createFluentProducerTemplate().to("direct:start").withBody("world!").request(String.class);
        System.out.println("Reply: " + response);
        context.stop();
    }
}
