package org.example;

import java.time.LocalDateTime;

/**
 * @author Viktor Shvidkiy
 */
public class App {
    public static void main(String[] args) {
        UserDao userDao = new UserDaoImpl();

        System.out.println("----------Создаем 2-х пользователей----------");
        User user1 = new User("Alice", "alice@gmail.com", 20, LocalDateTime.now());
        User user2 = new User("John", "john@gmail.com", 25, LocalDateTime.now());
        userDao.save(user1);
        userDao.save(user2);

        //Попытаемся спровоцировать ошибку при добавлении некорректной записи.
        //System.out.println("----------Добавляем пользователя без имени----------");
        //User user3 = new User();
        //userDao.save(user3);

        System.out.println("----------Получим из базы первого добавленного пользователя----------");
        User loaded = userDao.getById(user1.getId());
        System.out.println("Read: " + loaded);

        System.out.println("----------Попытаемся получить несуществующую запись----------");
        User loaded1 = userDao.getById(1000L);
        System.out.println("Read: " + loaded1);

        System.out.println("----------Изменим возраст пользователя----------");
        System.out.println("Before update: "+loaded);
        loaded.setAge(21);
        userDao.update(loaded);
        System.out.println("After update: " + loaded);

        System.out.println("----------Удалим пользователя----------");
        System.out.println(loaded);
        userDao.delete(loaded);
        System.out.println("Delete: " + userDao.getById(loaded.getId()));

        System.out.println("----------Выведем всех пользователей----------");
        for (User user : userDao.getAll()) {
            System.out.println(user);
        }

    }
}
