package org.example;

import java.util.List;

public interface UserService {
    void save(UserDto user);
    UserDto getById(Long id);
    List<UserDto> getAll();
    void update(UserDto user);
    void delete(UserDto user);
}
