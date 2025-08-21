package org.example;

import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
        System.setProperty("Hibernate.connection.password", postgreSQLContainer.getPassword());

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
        assertAll("Проверка всех полей User",
                ()->assertEquals("John", savedUser.getName(), "Имя должно быть Bill"),
                ()->assertEquals("john@gmail.com", savedUser.getEmail(), "email должен быть bill@gmail.com"),
                ()->assertEquals(25, savedUser.getAge(), "Возраст должен быть 30"),
                ()->assertEquals(time, savedUser.getCreatedAt(), "Дата создания должна быть "+time));
    }

    @Test
    void testGetById() {
        LocalDateTime time = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        User user = new User("Bill", "bill@gmail.com", 30, time);
        userDao.save(user);
        Optional<User> found = userDao.getById(user.getId());
        assertTrue(found.isPresent());
        assertAll("Проверка всех полей User",
                ()->assertEquals("Bill", found.get().getName(), "Имя должно быть Bill"),
                ()->assertEquals("bill@gmail.com", found.get().getEmail(), "email должен быть bill@gmail.com"),
                ()->assertEquals(30, found.get().getAge(), "Возраст должен быть 30"),
                ()->assertEquals(time, found.get().getCreatedAt(), "Дата создания должна быть "+time));
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
        assertEquals("BobUpdated", updated.get().getName(), "Имя должно обновиться на BobUpdated");
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
