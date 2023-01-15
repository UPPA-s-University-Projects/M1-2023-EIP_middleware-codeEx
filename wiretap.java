import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class HelloWorld {

    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();

        context.addRoutes(new RouteBuilder() {
            public void configure() {
                // Route to send "Hello World" message
                from("direct:start")
                        .transform(constant("Hello World"))
                        .to("stream:out");

                // Wire Tap to intercept messages and send them to another destination
                from("direct:start")
                        .wireTap("direct:log")
                        .to("direct:end");

                // Route to log intercepted messages
                from("direct:log")
                        .to("stream:err");
            }
        });

        context.start();
        context.createProducerTemplate().sendBody("direct:start", "message");
        context.stop();
    }
}
