package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * @author Viktor Shvidkiy
 */
public class HibernateUtil {
    private static final Logger logger = LogManager.getLogger(HibernateUtil.class);
    private static final SessionFactory sessionFactory = buildSessionFactory();

    public static SessionFactory createSessionFactory(String url, String usr, String pass){
        Configuration cfg = new Configuration().configure();
        cfg.setProperty("hibernate.connection.url", url);
        cfg.setProperty("hibernate.connection.username", usr);
        cfg.setProperty("hibernate.connection.password", pass);
        return cfg.buildSessionFactory();
    }

    private static SessionFactory buildSessionFactory() {
        try {
            SessionFactory sf = new Configuration().configure().buildSessionFactory();
            logger.info("Hibernate SessionFactory инициализирована.");
            return sf;
        } catch (Exception e) {
            logger.error("Не удалось инициализировать SessionFactory: {}", e.getMessage());
            throw new ExceptionInInitializerError(e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        logger.info("Закрытие SessionFactory.");
        getSessionFactory().close();
    }
}
