import javax.jms.*;
import javax.naming.InitialContext;

public class HelloWorld {

    public static void main(String[] args) throws Exception {
        InitialContext context = new InitialContext();
        ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
        Connection connection = factory.createConnection();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = (Destination) context.lookup("commandQueue");

        MessageProducer producer = session.createProducer(destination);
        TextMessage message = session.createTextMessage("Hello World!");
        message.setStringProperty("command", "print");
        producer.send(message);

        connection.close();
    }
}