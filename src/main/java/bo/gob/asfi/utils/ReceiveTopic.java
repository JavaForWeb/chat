package bo.gob.asfi.utils;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.Date;

/**
 * Created by fernando on 11/9/16.
 */
public class ReceiveTopic
{
	static Logger log = Logger.getLogger(ReceiveTopic.class.getName());

	private String brokerUrl;
	private String topicName;
	private String user;
	private String pass;

	private ConnectionFactory factory = null;
	private Connection connection = null;
	private Session session = null;
	private Destination destination = null;
	private MessageProducer producer = null;
	private MessageConsumer consumer = null;

	public ReceiveTopic(String brokerUrl, String queueName, String user, String pass)
	{
		this.brokerUrl = brokerUrl;
		this.topicName = queueName;
		this.user = user;
		this.pass = pass;
	}

	public void receiveMessages()
	{
		try {
			factory = new ActiveMQConnectionFactory( brokerUrl );
			if (user != null && !user.equals("")) {
				connection = factory.createConnection(user, pass);
			} else {
				connection = factory.createConnection();
			}

			connection.start();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			destination = session.createTopic(topicName);
			consumer = session.createConsumer( destination );

			Message message = null;

			Long endTime = new Date().getTime() + 60*1000;
			do {
				do {
					message = consumer.receive(10);

					if(message instanceof TextMessage) {
						TextMessage text = (TextMessage) message;
						System.out.println("Received topic is : " + text.getText());
						log.info(text.getText());
					}
				}
				while(message != null);

				try {
					Thread.sleep(250);
				} catch(InterruptedException e) {
				}

			} while( new Date().getTime() <= endTime);

			session.close();
			connection.close();
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			log.error(e.getMessage());
		}
	}


}
