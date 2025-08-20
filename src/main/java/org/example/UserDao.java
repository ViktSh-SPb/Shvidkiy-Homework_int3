package org.example;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    User save(User user);

    Optional<User> getById(Long id);

    List<User> getAll();

    void update(User user);

    void delete(User user);
}
