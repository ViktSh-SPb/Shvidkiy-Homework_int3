package org.example;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Viktor Shvidkiy
 */
public class UserServiceImpl implements UserService{
    private final UserDao userDao = new UserDaoImpl();

    @Override
    public void save(UserDto user) {
        userDao.save(userDtoToUser(user));
    }

    @Override
    public UserDto getById(Long id) {
        return userToUserDto(userDao.getById(id));
    }

    @Override
    public List<UserDto> getAll() {
        return userDao.getAll()
                .stream()
                .map(UserServiceImpl::userToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public void update(UserDto user) {
        userDao.update(userDtoToUser(user));
    }

    @Override
    public void delete(UserDto user) {
        userDao.delete(userDtoToUser(user));
    }

    public static UserDto userToUserDto(User user){
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .age(user.getAge())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .build();
    }

    public static User userDtoToUser(UserDto userDto){
        User user = new User();
        user.setId(userDto.getId());
        user.setName(userDto.getName());
        user.setAge(user.getAge());
        user.setEmail(user.getEmail());
        user.setCreatedAt(user.getCreatedAt());
        return user;
    }
}
