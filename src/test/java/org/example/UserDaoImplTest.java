package org.example;

import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Viktor Shvidkiy
 */
@Testcontainers
public class UserDaoImplTest {
    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:17-alpine")
            .withDatabaseName("test_db")
            .withUsername("test")
            .withPassword("test");

    private static UserDaoImpl userDao;

    @BeforeAll
    static void setup() {
        System.setProperty("hibernate.connection.url", postgreSQLContainer.getJdbcUrl());
        System.setProperty("Hibernate.connection.username", postgreSQLContainer.getUsername());
        System.setProperty("Hibername.connection.password", postgreSQLContainer.getPassword());

        userDao = new UserDaoImpl();
    }

    @AfterEach
    void cleanUp() {
        try (var session = HibernateUtil.getSessionFactory().openSession()) {
            var tx = session.beginTransaction();
            session.createMutationQuery("delete from User").executeUpdate();
            tx.commit();
        }
    }

    @Test
    void testSaveUser() {
        LocalDateTime time = LocalDateTime.now();
        User user = new User("John", "john@gmail.com", 25, time);
        User savedUser = userDao.save(user);
        assertNotNull(savedUser.getId());
        assertEquals("John", savedUser.getName());
    }

    @Test
    void testGetById() {
        LocalDateTime time = LocalDateTime.now();
        User user = new User("Bill", "bill@gmail.com", 30, time);
        userDao.save(user);
        Optional<User> found = userDao.getById(user.getId());
        assertTrue(found.isPresent());
        assertEquals("Bill", found.get().getName());
    }

    @Test
    void testGetAll() {
        userDao.save(new User("User1", "user1@gmail.com", 20, LocalDateTime.now()));
        userDao.save(new User("User2", "user2@gmail.com", 21, LocalDateTime.now()));
        List<User> users = userDao.getAll();
        assertEquals(2, users.size(), "В базе должно быть 2 пользователя");
    }

    @Test
    void testUpdate(){
        LocalDateTime time = LocalDateTime.now();
        User user = new User("Bob", "bob@gmail.com", 40, time);
        userDao.save(user);
        user.setName("BobUpdated");
        userDao.update(user);
        Optional<User> updated = userDao.getById(user.getId());
        assertTrue(updated.isPresent());
        assertEquals("BobUpdated", updated.get().getName());
    }

    @Test
    void testDelete(){
        User user = new User ("Charlie", "charlie@gmail.com", 22, LocalDateTime.now());
        userDao.save(user);
        userDao.delete(user);
        Optional<User> deleted = userDao.getById(user.getId());
        assertTrue(deleted.isEmpty(), "Пользователь должен быть удален");
    }
}
