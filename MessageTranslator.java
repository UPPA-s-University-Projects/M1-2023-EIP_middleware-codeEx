import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.xmljson.XmlJsonDataFormat;
import org.apache.camel.impl.DefaultCamelContext;

public class HelloWorld {

    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();

        XmlJsonDataFormat xmlJsonFormat = new XmlJsonDataFormat();
        xmlJsonFormat.setEncoding("UTF-8");
        xmlJsonFormat.setForceTopLevelObject(true);
        xmlJsonFormat.setTrimSpaces(true);

        context.addRoutes(new RouteBuilder() {
            public void configure() {
                // Route to translate a message from XML to JSON
                from("direct:start")
                        .marshal(xmlJsonFormat)
                        .process(exchange -> {
                            String message = exchange.getIn().getBody(String.class);
                            System.out.println("Translated message: " + message);
                        });
            }
        });

        context.start();
        context.createProducerTemplate().sendBody("direct:start", "<message>Hello World!</message>");
        Thread.sleep(1000);
        context.stop();
    }
}