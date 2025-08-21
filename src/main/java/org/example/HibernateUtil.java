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
            /*
            В этом методе переменной url присваиваю значение из переменной окружения. Если программа запущена в Docker,
            то она есть. Если запущена в Intellij Idea, то переменной нет, условие не выполнится и url подтянется из
            hibernate.cfg. Сделал это для того, чтобы приложение можно было запустить и через Докер и через Идею для
            тестирования. Не знаю, насколько это корректно. Другого способа разбить запуск на 2 сценария не нашел.
             */
            Configuration cfg = new Configuration().configure();
            String url = System.getenv("DB_URL");
            if(url!=null) cfg.setProperty("hibernate.connection.url", url);
            SessionFactory sf = cfg.buildSessionFactory();
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
