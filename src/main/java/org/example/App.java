package org.example;

import java.time.LocalDateTime;

/**
 * @author Viktor Shvidkiy
 */
public class App {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();

        System.out.println("----------Создаем 2-х пользователей----------");
        UserDto user1 = UserDto
                .builder()
                .name("Alice")
                .age(20)
                .email("alice@gmail.com")
                .createdAt(LocalDateTime.now())
                .build();
        UserDto user2 = UserDto
                .builder()
                .name("John")
                .age(25)
                .email("john@gmail.com")
                .createdAt(LocalDateTime.now())
                .build();

        user1 = userService.save(user1);
        user2 = userService.save(user2);

        //Попытаемся спровоцировать ошибку при добавлении некорректной записи.
        //System.out.println("----------Добавляем пользователя без имени----------");
        //User user3 = new User();
        //userDao.save(user3);

        System.out.println("----------Получим из базы первого добавленного пользователя----------");
        UserDto loaded = userService.getById(user1.getId());
        System.out.println("Read: " + loaded);

        System.out.println("----------Попытаемся получить несуществующую запись----------");
        UserDto loaded1 = userService.getById(1000L);
        System.out.println("Read: " + loaded1);

        System.out.println("----------Изменим возраст пользователя----------");
        System.out.println("Before update: "+loaded);
        loaded.setAge(21);
        userService.update(loaded);
        System.out.println("After update: " + loaded);

        System.out.println("----------Удалим пользователя----------");
        System.out.println(loaded);
        userService.delete(loaded);
        System.out.println("Delete: " + userService.getById(loaded.getId()));

        System.out.println("----------Выведем всех пользователей----------");
        for (UserDto user : userService.getAll()) {
            System.out.println(user);
        }

    }
}
