package bo.gob.asfi.utils;

import bo.gob.asfi.entity.Account;
import bo.gob.asfi.entity.Transfer;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.SQLGrammarException;

import java.util.Properties;

/**
 * Created by fernando on 10/17/16.
 */
public class DBSession
{
	static Logger log = Logger.getLogger(DBSession.class.getName());

	private static DBSession ourInstance = new DBSession();
	public static DBSession getInstance()
	{
		return ourInstance;
	}

	private SessionFactory factory = null;

	private DBSession()
	{
	}

	public Session getSession()
	{
		Session session = factory.getCurrentSession();
		return session;
	}

	public Session openSession()
	{
		Session session = factory.openSession();
		return session;
	}

	public void initFactory(Properties config)
	{
		log.info("init factory for hibernate");
		// create session factory
		Configuration configuration = new Configuration()
			.configure("hibernate.cfg.xml")
			.addAnnotatedClass(Account.class)
			.addAnnotatedClass(Transfer.class)
			.setProperty("hibernate.connection.username", config.getProperty("postgresql.user"))
			.setProperty("hibernate.connection.password", config.getProperty("postgresql.pass"));

		if (config.containsKey("show_sql")) {
			configuration.setProperty("hibernate.show_sql", config.getProperty("show_sql"));
		}

		try {
			factory = configuration.buildSessionFactory();
		} catch( SQLGrammarException e) {
			log.error(e.getMessage());
			e.printStackTrace();
			System.exit(0);
		}

	}

}
