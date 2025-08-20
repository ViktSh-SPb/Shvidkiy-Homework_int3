package org.example;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Viktor Shvidkiy
 */
public class UserServiceImpl implements UserService{
    private final UserDao userDao = new UserDaoImpl();

    @Override
    public UserDto save(UserDto userDto) {
        return userToUserDto(userDao.save(userDtoToUser(userDto)));
    }

    @Override
    public UserDto getById(Long id) {
        return userDao.getById(id)
                .map(UserServiceImpl::userToUserDto)
                .orElse(null);
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
        user.setAge(userDto.getAge());
        user.setEmail(userDto.getEmail());
        user.setCreatedAt(userDto.getCreatedAt());
        return user;
    }
}
