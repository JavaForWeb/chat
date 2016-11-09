package bo.gob.asfi;

import bo.gob.asfi.utils.Common;
import bo.gob.asfi.utils.DBSession;
import bo.gob.asfi.utils.ReceiveQueue;
import bo.gob.asfi.utils.SendQueue;
import org.apache.log4j.Logger;

import java.util.Properties;
import java.util.Random;

public class Main {

    static Properties config = null;

    static Logger log = Logger.getLogger(Main.class.getName());
    static final String appTitle = "Message System Demo";

    static Random random = new Random();

    private static void initHibernate()
    {
        Common.displayTitle(appTitle);

        log.info("Starting PostgreSQL tutorial ");

        DBSession.getInstance().initFactory(config);

    }

    private static void initHibernateDebug()
    {

        log.info("Starting Hibernate Factory with Debug enabled ");
        config.setProperty("show_sql", "true");
        DBSession.getInstance().initFactory(config);

    }


    private void setup()
    {

    }

    public void run()
    {
    }

    public static void main(String[] args) {
        config = Common.readConfig("chat.config");

        Common.displayTitle("Message System Demo");
        //initHibernateDebug();

        SendQueue sendMessage = new SendQueue(
            config.getProperty("activemq.brokerUrl"),
            "queue1",
            config.getProperty("activemq.write_user"),
            config.getProperty("activemq.write_pass")
        );

        if (args.length > 0) {
            sendMessage.send(args[0]);
        } else {
            sendMessage.send();
        }

        ReceiveQueue receiveQueue = new ReceiveQueue(
            config.getProperty("activemq.brokerUrl"),
            "queue1",
            config.getProperty("activemq.read_user"),
            config.getProperty("activemq.read_pass")
        );

        receiveQueue.receiveMessages();

        System.out.println("Done");
    }
}
