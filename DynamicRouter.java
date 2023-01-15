import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class HelloWorld {

    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();

        context.addRoutes(new RouteBuilder() {
            public void configure() {
                // Route to send message to a JMS queue or topic based on the message type
                from("direct:start")
                        .choice()
                            .when(header("type").isEqualTo("queue"))
                                .to("jms:queue:orders")
                            .when(header("type").isEqualTo("topic"))
                                .to("jms:topic:notifications")
                            .otherwise()
                                .to("log:default");
            }
        });

        context.start();
        context.createProducerTemplate().sendBodyAndHeader("direct:start", "Hello World!", "type", "queue");
        context.createProducerTemplate().sendBodyAndHeader("direct:start", "Hello World!", "type", "topic");
        context.createProducerTemplate().sendBody("direct:start", "Hello World!");
        Thread.sleep(1000);
        context.stop();
    }
}