package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

/**
 * @author Viktor Shvidkiy
 */
public class UserDaoImpl implements UserDao {
    private static final Logger logger = LogManager.getLogger(UserDao.class);

    @Override
    public User save(User user) {
        Transaction tx = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            tx = session.beginTransaction();
            session.persist(user);
            tx.commit();
            logger.info("Запись сохранена: {}", user);
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
                logger.warn("Произошла ошибка. Транзакция отменена.");
            }
            logger.error("Ошибка при сохранении записи: {}", e.getMessage());
            throw new RuntimeException("Ошибка при сохранении записи.", e);
        } finally {
            session.close();
        }
        return user;
    }

    @Override
    public Optional<User> getById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            User user = session.get(User.class, id);
            if (user == null) {
                logger.warn("Запись с id: {} не найдена.", id);
            } else {
                logger.info("Запись получена: {}", user);
            }
            return Optional.ofNullable(user);
        } catch (Exception e) {
            logger.error("Ошибка при поиске записи: {}", e.getMessage());
            throw new RuntimeException("Ошибка при поиске записи.", e);
        }
    }

    @Override
    public List<User> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<User> users = session.createQuery("from User", User.class).list();
            logger.info("Получено {} записей.", users.size());
            return users;
        } catch (Exception e) {
            logger.error("Ошибка при получении списка записей: {}", e.getMessage());
            throw new RuntimeException("Ошибка при получении списка записей.", e);
        }
    }

    @Override
    public void update(User user) {
        Transaction tx = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            tx = session.beginTransaction();
            session.merge(user);
            tx.commit();
            logger.info("Запись изменена: {}", user);
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            logger.error("Ошибка при изменении записи: {}", e.getMessage());
            throw new RuntimeException("Ошибка при изменении записи.", e);
        } finally {
            session.close();
        }
    }

    @Override
    public void delete(User user) {
        Transaction tx = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            tx = session.beginTransaction();
            session.remove(user);
            tx.commit();
            logger.info("Запись удалена: {}", user);
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            logger.error("Ошибка при удалении записи: {}", e.getMessage());
            throw new RuntimeException("Ошибка при удалении записи.", e);
        } finally {
            session.close();
        }
    }
}
