package com.codegeek.hogwartsartifactsonline.hogwartsuser;

import com.codegeek.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    List<HogwartsUser> users;

    @BeforeEach
    void setUp() {
        HogwartsUser u1 = new HogwartsUser();
        u1.setId(1);
        u1.setUsername("john");
        u1.setPassword("123456");
        u1.setEnabled(true);
        u1.setRoles("admin user");

        HogwartsUser u2 = new HogwartsUser();
        u2.setId(2);
        u2.setUsername("sunusi");
        u2.setPassword("654321");
        u2.setEnabled(true);
        u2.setRoles("user");

        HogwartsUser u3 = new HogwartsUser();
        u3.setId(3);
        u3.setUsername("daneji");
        u3.setPassword("qwerty");
        u3.setEnabled(false);
        u3.setRoles("user");

        users = new ArrayList<>();

        users.addAll(List.of(u1,u2,u3));
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findAll() {
        // Given
        given(userRepository.findAll()).willReturn(users);

        // When
        List<HogwartsUser> foundUsers = userService.findAll();

        // Then
       assertThat(foundUsers.size()).isEqualTo(users.size());
       verify(userRepository, times(1)).findAll();
    }

    @Test
    void findById() {
        // Given
        HogwartsUser u1 = new HogwartsUser();
        u1.setId(1);
        u1.setUsername("john");
        u1.setPassword("123456");
        u1.setEnabled(true);
        u1.setRoles("admin user");

        given(userRepository.findById(1)).willReturn(Optional.of(u1));

        // When
        HogwartsUser foundUser = userService.findById(1);

        // Then
        assertThat(foundUser.getId()).isEqualTo(u1.getId());
        assertThat(foundUser.getUsername()).isEqualTo(u1.getUsername());
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void findByIdNotFound() {
        // Given
        given(userRepository.findById(1)).willReturn(Optional.empty());

        // When
        Throwable throwable = catchThrowable(() -> {
            HogwartsUser foundUser = userService.findById(1);
        });

        // Then
        assertThat(throwable).isInstanceOf(ObjectNotFoundException.class).hasMessage("Could not find user with Id 1 :(");
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void save() {
        // Given
        HogwartsUser u1 = new HogwartsUser();
        u1.setId(1);
        u1.setUsername("john");
        u1.setPassword("123456");
        u1.setEnabled(true);
        u1.setRoles("admin user");

        given(userRepository.save(u1)).willReturn(u1);

        // When
        HogwartsUser savedUser = userService.save(u1);

        // Then
        assertThat(savedUser.getId()).isEqualTo(u1.getId());
        assertThat(savedUser.getUsername()).isEqualTo(u1.getUsername());
        verify(userRepository, times(1)).save(u1);
    }

    @Test
    void update() {
        // Given
        HogwartsUser update = new HogwartsUser();
        update.setUsername("john updated");
        update.setPassword("123456");
        update.setEnabled(true);
        update.setRoles("admin user");

        HogwartsUser userToUpdate = new HogwartsUser();
        userToUpdate.setId(1);
        userToUpdate.setUsername("john");
        userToUpdate.setPassword("123456");
        userToUpdate.setEnabled(true);
        userToUpdate.setRoles("admin user");

        given(userRepository.findById(1)).willReturn(Optional.of(userToUpdate));
        given(userRepository.save(Mockito.any(HogwartsUser.class))).willReturn(userToUpdate);

        // When
        HogwartsUser updatedUser = userService.update(1, update);

        // Then
        assertThat(updatedUser.getUsername()).isEqualTo(update.getUsername());
        verify(userRepository, times(1)).findById(1);
        verify(userRepository, times(1)).save(userToUpdate);
    }

    @Test
    void updateNotFound() {
        // Given
        HogwartsUser update = new HogwartsUser();
        update.setUsername("john updated");
        update.setPassword("123456");
        update.setEnabled(true);
        update.setRoles("admin user");

        given(userRepository.findById(1)).willReturn(Optional.empty());

        // When and Then
        assertThrows(ObjectNotFoundException.class, () -> {
            HogwartsUser updatedUser = userService.update(1, update);
        });

        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void delete() {
        // Given
        HogwartsUser toBeDeleted = new HogwartsUser();
        toBeDeleted.setId(1);
        toBeDeleted.setUsername("john updated");
        toBeDeleted.setPassword("123456");
        toBeDeleted.setEnabled(true);
        toBeDeleted.setRoles("admin user");

        given(userRepository.findById(1)).willReturn(Optional.of(toBeDeleted));
        doNothing().when(userRepository).deleteById(1);

        // When
        userService.delete(1);
        verify(userRepository, times(1)).deleteById(1);
    }

    @Test
    void deleteNotFound() {
        // Given
        given(userRepository.findById(1)).willReturn(Optional.empty());

        // When
       Throwable thrown = catchThrowable(() -> {
           userService.delete(1);
       });

       // Then
        assertThat(thrown).isInstanceOf(ObjectNotFoundException.class).hasMessage("Could not find user with Id 1 :(");
        verify(userRepository, times(1)).findById(1);
    }


}