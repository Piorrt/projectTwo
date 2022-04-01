package pl.sages.javadevpro.projecttwo.domain.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.sages.javadevpro.projecttwo.external.storage.user.UserAlreadyExistsException;

import java.util.Optional;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private final User fakeUser = new User(
            "ID28",
            "email@email.any",
            "user name",
            "pass",
            Set.of(UserRole.STUDENT)
    );

    @Test
    void save_method_should_return_saved_user_when_user_does_not_exist() {
        Mockito.when(userRepository.save(fakeUser)).thenReturn(fakeUser);

        //when
        User savedUser = userService.save(fakeUser);

        //then
        Assertions.assertNotNull(savedUser);
        Assertions.assertEquals(fakeUser.getId(), savedUser.getId());
        Assertions.assertEquals(fakeUser.getEmail(), savedUser.getEmail());
        Assertions.assertEquals(fakeUser.getName(), savedUser.getName());
        Assertions.assertEquals(fakeUser.getPassword(), savedUser.getPassword());
    }

    @Test
    void save_method_should_throw_user_already_exist_exception_when_user_exist() {
        Mockito.when(userRepository.save(fakeUser)).thenThrow(new UserAlreadyExistsException());

        //when
        //then
        Assertions.assertThrows(UserAlreadyExistsException.class,
                ()-> userService.save(fakeUser));
    }

    @Test
    void update_method_should_return_updates_user_when_user_exist() {
        Mockito.when(userRepository.update(fakeUser)).thenReturn(Optional.of(fakeUser));

        //when
        User updatedUser = userService.update(fakeUser);

        //then
        Assertions.assertNotNull(updatedUser);
        Assertions.assertEquals(fakeUser.getId(), updatedUser.getId());
        Assertions.assertEquals(fakeUser.getEmail(), updatedUser.getEmail());
        Assertions.assertEquals(fakeUser.getName(), updatedUser.getName());
        Assertions.assertEquals(fakeUser.getPassword(), updatedUser.getPassword());
    }

    @Test
    void update_method_should_throw_user_not_found_exception_when_user_does_not_exist() {
        Mockito.when(userRepository.update(fakeUser)).thenReturn(Optional.empty());

        //when
        //then
        Assertions.assertThrows(UserNotFoundException.class,
                ()-> userService.update(fakeUser));
    }

    @Test
    void remove_by_id_method_should_return_removed_user_when_user_exist() {
        Mockito.when(userRepository.remove(fakeUser.getId())).thenReturn(Optional.of(fakeUser));

        //when
        User removedUser = userService.removeById(fakeUser.getId());

        //then
        Assertions.assertNotNull(removedUser);
        Assertions.assertEquals(fakeUser.getId(), removedUser.getId());
        Assertions.assertEquals(fakeUser.getEmail(), removedUser.getEmail());
        Assertions.assertEquals(fakeUser.getName(), removedUser.getName());
        Assertions.assertEquals(fakeUser.getPassword(), removedUser.getPassword());
    }

    @Test
    void remove_by_id_method_should_throw_user_not_found_exception_when_user_does_not_exist() {
        Mockito.when(userRepository.remove(fakeUser.getId())).thenReturn(Optional.empty());

        //when
        //then
        Assertions.assertThrows(UserNotFoundException.class,
                ()-> userService.removeById(fakeUser.getId()));
    }

    @Test
    void find_by_email_method_should_return_founded_user_when_user_exist() {
        Mockito.when(userRepository.findByEmail(fakeUser.getEmail())).thenReturn(Optional.of(fakeUser));

        //when
        User foundedUser = userService.findByEmail(fakeUser.getEmail());

        //then
        Assertions.assertNotNull(foundedUser);
        Assertions.assertEquals(fakeUser.getId(), foundedUser.getId());
        Assertions.assertEquals(fakeUser.getEmail(), foundedUser.getEmail());
        Assertions.assertEquals(fakeUser.getName(), foundedUser.getName());
        Assertions.assertEquals(fakeUser.getPassword(), foundedUser.getPassword());
    }

    @Test
    void find_by_email_method_should_throw_user_not_found_exception_when_user_does_not_exist() {
        Mockito.when(userRepository.findByEmail(fakeUser.getEmail())).thenReturn(Optional.empty());

        //when
        //then
        Assertions.assertThrows(UserNotFoundException.class,
                ()-> userService.findByEmail(fakeUser.getEmail()));
    }

    @Test
    void find_by_id_method_should_return_founded_user_when_user_exist() {
        Mockito.when(userRepository.findById(fakeUser.getId())).thenReturn(Optional.of(fakeUser));

        //when
        User foundedUser = userService.findById(fakeUser.getId());

        //then
        Assertions.assertNotNull(foundedUser);
        Assertions.assertEquals(fakeUser.getId(), foundedUser.getId());
        Assertions.assertEquals(fakeUser.getEmail(), foundedUser.getEmail());
        Assertions.assertEquals(fakeUser.getName(), foundedUser.getName());
        Assertions.assertEquals(fakeUser.getPassword(), foundedUser.getPassword());
    }

    @Test
    void find_by_id_method_should_throw_user_not_found_exception_when_user_does_not_exist() {
        Mockito.when(userRepository.findById(fakeUser.getId())).thenReturn(Optional.empty());

        //when
        //then
        Assertions.assertThrows(UserNotFoundException.class,
                ()-> userService.findById(fakeUser.getId()));
    }

}