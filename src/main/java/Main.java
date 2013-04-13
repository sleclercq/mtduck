import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lair.mtduck.irc.TickerIRCMessenger;
import com.lair.mtduck.mtgox.MtGoxManager;
import to.sparks.mtgox.model.Ticker;

/**
 *
 */
public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        Properties prop = loadProperties("config.properties");

        MtGoxManager mtGoxManager = new MtGoxManager();

        try {
            TickerIRCMessenger tickerIRCMessenger = new TickerIRCMessenger(prop);
            tickerIRCMessenger.setVerbose(true);
            tickerIRCMessenger.connect();
            while(true) {
                Ticker ticker = mtGoxManager.getTicker();
                if (ticker == null) {
                    logger.warn("Ticker is null");
                } else {
                    tickerIRCMessenger.updateTicker(ticker.getLast().getNumUnits());
                }
                Thread.sleep(60 * 1000);
            }
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

    private static Properties loadProperties(String propertiesFile) {
        Properties prop = new Properties();

        try {
            InputStream inputStream = Main.class.getClassLoader().getResourceAsStream(propertiesFile);
            prop.load(inputStream);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return prop;
    }


}
