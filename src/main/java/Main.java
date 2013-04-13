import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang.ArrayUtils;

import com.lair.mtduck.irc.TickerIRCMessenger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import to.sparks.mtgox.MtGoxHTTPClient;
import to.sparks.mtgox.model.AccountInfo;
import to.sparks.mtgox.model.Lag;
import to.sparks.mtgox.model.Order;
import to.sparks.mtgox.model.Ticker;

/**
 *
 */
public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        Properties prop = new Properties();

        try {
            InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("config.properties");
            prop.load(inputStream);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

//        try {
//            tickerExample();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        try {
            TickerIRCMessenger tickerIRCMessenger = new TickerIRCMessenger();
            tickerIRCMessenger.setVerbose(true);
            tickerIRCMessenger.connect(prop.getProperty("irc.server.host"), 6667, prop.getProperty("irc.server.password"));
            tickerIRCMessenger.joinChannel(prop.getProperty("irc.channel"));
            tickerIRCMessenger.sendMessage(prop.getProperty("irc.channel"), "...aaaaaand we're back!");
            tickerIRCMessenger.sendMessage(prop.getProperty("irc.channel"), getTicker().getLast().toPlainString());
        } catch (Exception e) { // TODO get rid of Exception in mtgox library
            e.printStackTrace();
        }

        try {
            Thread.sleep(1000 * 10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.exit(0);
    }

    public static Ticker getTicker() throws Exception {
        // Obtain a $USD instance of the API
        ApplicationContext context = new ClassPathXmlApplicationContext("to/sparks/mtgox/example/Beans.xml");
        MtGoxHTTPClient mtgoxUSD = (MtGoxHTTPClient) context.getBean("mtgoxUSD");
        return mtgoxUSD.getTicker();
    }


    public static void tickerExample() throws Exception {

        // Obtain a $USD instance of the API
        ApplicationContext context = new ClassPathXmlApplicationContext("to/sparks/mtgox/example/Beans.xml");
        MtGoxHTTPClient mtgoxUSD = (MtGoxHTTPClient) context.getBean("mtgoxUSD");

        Lag lag = mtgoxUSD.getLag();
        logger.info("Current lag: {}", lag.getLag());


        Ticker ticker = mtgoxUSD.getTicker();
        logger.info("Last price: {}", ticker.getLast().toPlainString());

        // Get the private account info
        AccountInfo info = mtgoxUSD.getAccountInfo();
        logger.info("Logged into account: {}", info.getLogin());

        Order[] openOrders = mtgoxUSD.getOpenOrders();

        if (ArrayUtils.isNotEmpty(openOrders)) {
            for (Order order : openOrders) {
                logger.info("Open order: {} status: {} price: {}{} amount: {}", new Object[]{order.getOid(), order.getStatus(), order.getCurrency().getCurrencyCode(), order.getPrice().getDisplay(), order.getAmount().getDisplay()});
            }
        } else {
            logger.info("There are no currently open bid or ask orders.");
        }

    }

}
