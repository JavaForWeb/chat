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

/**
 * Created by fernando on 11/9/16.
 */
public class ReceiveQueue
{
	static Logger log = Logger.getLogger(ReceiveQueue.class.getName());

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

	public ReceiveQueue(String brokerUrl, String queueName, String user, String pass)
	{
		this.brokerUrl = brokerUrl;
		this.queueName = queueName;
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
			destination = session.createQueue(queueName);
			consumer = session.createConsumer( destination );

			Message message = null;

			do {
				message = consumer.receive( 10 );

				if( message instanceof TextMessage)
				{
					TextMessage text = (TextMessage) message;
					System.out.println( "Received is : " + text.getText() );
					log.info( text.getText());
				}
			} while (message != null);

			session.close();
			connection.close();
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			log.error(e.getMessage());
		}
	}


}
