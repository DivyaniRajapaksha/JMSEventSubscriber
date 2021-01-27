package com.gateway.gateway;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;
import java.util.Properties;

public class NewTry implements MessageListener {

    @Override
    public void onMessage(Message message) {
        System.out.println("Hiiiiii");
        System.out.println(message);
    }

    public void setSubscriber() throws NamingException, JMSException, InterruptedException, IOException {
        Properties properties = new Properties();
        properties.put("java.naming.factory.initial","org.wso2.andes.jndi.PropertiesFileInitialContextFactory");
        properties.setProperty("connectionfactory.TopicConnectionFactory","amqp://admin:admin@clientid/carbon?brokerlist='tcp://localhost:5672?retries='5'%26connectdelay='50';tcp://localhost:5672?retries='5'%26connectdelay='50';'");
        properties.setProperty("topic.notification","notification");
        Context context = new InitialContext(properties);

        ConnectionFactory connectionFactory
                = (ConnectionFactory) context.lookup("TopicConnectionFactory");
        Connection connection = connectionFactory.createConnection("admin","admin");
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic topicLabel = (Topic) context.lookup("notification");

        MessageConsumer subscriber1 =((TopicSession) session).createSubscriber(topicLabel);

        subscriber1.setMessageListener(this::onMessage);

        System.out.println("Listnening to the Topic"+topicLabel);
        Thread.sleep(10000);



    }

}
