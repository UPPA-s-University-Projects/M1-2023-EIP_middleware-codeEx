import javax.jms.ConnectionFactory;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;

public class HelloWorld {

    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();

        // Configure ActiveMQ as the JMS provider
        ConnectionFactory connectionFactory = 
            new ActiveMQConnectionFactory("tcp://localhost:61616");
        context.addComponent("jms",
            JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));

        context.addRoutes(new RouteBuilder() {
            public void configure() {
                // Route to send message to the queue
                from("direct:start")
                        .to("jms:queue:hello");

                // Route to receive message from the queue
                from("jms:queue:hello")
                        .process(exchange -> {
                            String message = exchange.getIn().getBody(String.class);
                            System.out.println("Received: " + message);
                        });
            }
        });

        context.start();
        context.createProducerTemplate().sendBody("direct:start", "Hello World!");
        Thread.sleep(1000);
        context.stop();
    }
}
