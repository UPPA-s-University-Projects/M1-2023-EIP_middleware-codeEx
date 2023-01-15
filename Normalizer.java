import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.xmljson.XmlJsonDataFormat;
import org.apache.camel.impl.DefaultCamelContext;

public class HelloWorld {

    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();

        // Configure the data format for XML to JSON conversion
        XmlJsonDataFormat xmlJsonFormat = new XmlJsonDataFormat();
        xmlJsonFormat.setEncoding("UTF-8");
        xmlJsonFormat.setForceTopLevelObject(true);

        context.addRoutes(new RouteBuilder() {
            public void configure() {
                // Route to receive XML message and convert to JSON
                from("direct:xml")
                        .unmarshal(xmlJsonFormat)
                        .to("direct:json");

                // Route to receive JSON message and log
                from("direct:json")
                        .process(exchange -> {
                            String message = exchange.getIn().getBody(String.class);
                            System.out.println("Received: " + message);
                        });
            }
        });

        context.start();
        String xmlMessage = "<hello>world!</hello>";
        context.createProducerTemplate().sendBody("direct:xml", xmlMessage);
        context.stop();
    }
}