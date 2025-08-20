package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Viktor Shvidkiy
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserDto userDto;
    private LocalDateTime now;

    @BeforeEach
    void setup() {
        now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        user = new User("Jack", "jack@gmail.com", 21, now);
        userDto = UserServiceImpl.userToUserDto(user);
    }

    @Test
    void testSave() {
        when(userDao.save(any(User.class))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId(1L);
            return u;
        });

        UserDto saved = userService.save(userDto);
        assertNotNull(saved);
        assertEquals(1L, saved.getId());
        assertAll(
                () -> assertEquals("Jack", saved.getName()),
                () -> assertEquals("jack@gmail.com", saved.getEmail()),
                () -> assertEquals(21, saved.getAge()),
                () -> assertEquals(now, saved.getCreatedAt())
        );
        verify(userDao, times(1)).save(any(User.class));
    }

    @Test
    void testGetByIdFound() {
        user.setId(1L);

        when(userDao.getById(1L)).thenReturn(Optional.of(user));

        UserDto found = userService.getById(1L);
        assertNotNull(found);
        assertAll(
                () -> assertEquals("Jack", found.getName()),
                () -> assertEquals("jack@gmail.com", found.getEmail()),
                () -> assertEquals(21, found.getAge()),
                () -> assertEquals(now, found.getCreatedAt())
        );
        verify(userDao, times(1)).getById(1L);
    }

    @Test
    void testGetByIdNotFound() {
        when(userDao.getById(100L)).thenReturn(Optional.empty());

        UserDto found = userService.getById(100L);
        assertNull(found);
        verify(userDao, times(1)).getById(100L);
    }

    @Test
    void testGetAll() {
        now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        User user1 = new User("Bill", "bill@gmail.com", 22, now);
        user.setId(1L);
        user1.setId(2L);

        when(userDao.getAll()).thenReturn(Arrays.asList(user, user1));

        List<UserDto> users = userService.getAll();
        assertEquals(2, users.size());
        assertAll(
                () -> assertEquals("Jack", user.getName()),
                () -> assertEquals("jack@gmail.com", user.getEmail()),
                () -> assertEquals(21, user.getAge()),
                () -> assertEquals(this.now, user.getCreatedAt())
        );
        assertAll(
                () -> assertEquals("Bill", user1.getName()),
                () -> assertEquals("bill@gmail.com", user1.getEmail()),
                () -> assertEquals(22, user1.getAge()),
                () -> assertEquals(now, user1.getCreatedAt())
        );
        verify(userDao, times(1)).getAll();
    }

    @Test
    void testUpdate(){
        doNothing().when(userDao).update(any(User.class));

        userDto.setId(1L);
        userService.update(userDto);
        verify(userDao, times(1)).update(any(User.class));
    }

    @Test
    void testDelete(){
        doNothing().when(userDao).delete(any(User.class));

        userDto.setId(1L);
        userService.delete(userDto);
        verify(userDao,times(1)).delete(any(User.class));
    }
}
