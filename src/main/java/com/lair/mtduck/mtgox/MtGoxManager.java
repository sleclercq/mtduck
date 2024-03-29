package com.lair.mtduck.mtgox;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import to.sparks.mtgox.MtGoxHTTPClient;
import to.sparks.mtgox.model.Ticker;

/**
 *
 */
public class MtGoxManager {

    private static final Logger logger = LoggerFactory.getLogger(MtGoxManager.class);

    ApplicationContext context = new ClassPathXmlApplicationContext("to/sparks/mtgox/example/Beans.xml");
    MtGoxHTTPClient mtgoxUSD = (MtGoxHTTPClient) context.getBean("mtgoxUSD");

    public Ticker getTicker() {
        try {
            String mtgoxApiKey = (String) context.getBean("mtgoxapikey");
            String mtgoxApiSecret = ((String) context.getBean("mtgoxapisecret")).substring(0, 5);
            logger.info("Initialized with apikey {} apisecret {} ", mtgoxApiKey, mtgoxApiSecret);
            return mtgoxUSD.getTicker();
        } catch (Exception e) {
            logger.warn("Could not get ticker", e);
            return null;
        }
    }


}
