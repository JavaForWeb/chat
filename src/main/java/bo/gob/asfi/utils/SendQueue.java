package bo.gob.asfi.utils;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.Date;

/**
 * Created by fernando on 11/9/16.
 */
public class SendQueue
{
	static Logger log = Logger.getLogger(SendQueue.class.getName());

	private String brokerUrl;
	private String queueName;
	private String user;
	private String pass;

	private ConnectionFactory factory = null;
	private Connection connection = null;
	private Session session = null;
	private Destination destination = null;
	private MessageProducer producer = null;
	private MessageConsumer consumer = null;

	public SendQueue(String brokerUrl, String queueName, String user, String pass)
	{
		this.brokerUrl = brokerUrl;
		this.queueName = queueName;
		this.user = user;
		this.pass = pass;
	}

	private String createSampleMessage()
	{
		Date date = new Date();

		String text = date.getTime() + " hello from: " + Thread.currentThread().getName();

		return text;
	}

	public void send() {
		send (createSampleMessage());
	}
	public void send(String message) {
		try {
			factory = new ActiveMQConnectionFactory( brokerUrl );

			if (user != null && !user.equals("")) {
				connection = factory.createConnection(user, pass);
			} else {
				connection = factory.createConnection();
			}
			connection.start();

			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			destination = session.createQueue(queueName);
			producer = session.createProducer(destination);
			TextMessage message2 = session.createTextMessage();

			message2.setText( message );
			producer.send( message2 );
			System.out.println("Sent: " + message);
			log.info("Sent: " + message);

			// Clean up
			session.close();
			connection.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			log.error(e.getMessage());
		}

	}
}
