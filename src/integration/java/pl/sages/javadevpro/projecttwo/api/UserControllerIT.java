package pl.sages.javadevpro.projecttwo.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import pl.sages.javadevpro.projecttwo.BaseIT;
import pl.sages.javadevpro.projecttwo.TestUserFactory;
import pl.sages.javadevpro.projecttwo.api.user.UserDto;
import pl.sages.javadevpro.projecttwo.api.usertask.MessageResponse;
import pl.sages.javadevpro.projecttwo.domain.user.User;
import pl.sages.javadevpro.projecttwo.domain.user.UserRole;
import pl.sages.javadevpro.projecttwo.domain.user.UserService;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserControllerIT extends BaseIT {

    @Autowired
    UserService service;

    @Test
    void admin_should_get_information_about_any_user() {
        //given
        User user = TestUserFactory.createStudent();
        service.save(user);
        String token = getTokenForAdmin();

        //when
        var response = callHttpMethod(HttpMethod.GET,
                "/users/" + user.getId(),
                token,
                null,
                UserDto.class);

        //then
        UserDto body = response.getBody();
        System.out.println(body);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertNotNull(body);
        assertEquals(user.getId(), body.getId());
        assertEquals(user.getEmail(), body.getEmail());
        assertEquals(user.getName(), body.getName());
        assertEquals("######", body.getPassword());
        assertEquals(user.getRoles().toString(), body.getRoles().toString());
    }

    @Test
    void admin_should_get_response_code_404_when_user_not_exits_in_db() {
        //given
        String token = getTokenForAdmin();

        //when
        var response = callHttpMethod(HttpMethod.GET,
                "/users/fakeId",
                token,
                null,
                MessageResponse.class);

        //then
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    void student_should_not_get_information_about_other_student() {
        //given
        User user1 = TestUserFactory.createStudent();
        User user2 = TestUserFactory.createStudent();
        service.save(user1);
        service.save(user2);
        String accessToken = getAccessTokenForUser(user1.getEmail(), user1.getPassword());

        //when
        var response = callHttpMethod(HttpMethod.GET,
                "/users/" + user2.getId(),
                accessToken,
                null,
                MessageResponse.class);

        //then
        assertEquals(response.getStatusCode(), HttpStatus.FORBIDDEN);
    }

    @Test
    void admin_should_get_response_code_conflict_when_user_is_in_db() {
        //given
        User user = TestUserFactory.createStudent();
        service.save(user);
        String adminToken = getTokenForAdmin();

        //when
        var response = callHttpMethod(HttpMethod.POST,
                "/users",
                adminToken,
                user,
                MessageResponse.class);

        //then
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }


    @Test
    void admin_should_be_able_to_save_new_user() {
        //given
        User user = TestUserFactory.createStudent();
        String adminAccessToken = getTokenForAdmin();

        //when
        var response = callHttpMethod(HttpMethod.POST,
                "/users",
                adminAccessToken,
                user,
                UserDto.class);

        //then
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        //and
        UserDto body = response.getBody();
        assertNotNull(body);
        assertEquals(body.getEmail(), user.getEmail());
        assertEquals(body.getName(), user.getName());
        assertEquals(body.getPassword(), "######");
        assertEquals(body.getRoles().toString(), user.getRoles().toString());
    }

    @Test
    void student_should_get_information_about_himself() {
        //given
        User user = TestUserFactory.createStudent();
        service.save(user);
        String accessToken = getAccessTokenForUser(user.getEmail(), user.getPassword());

        //when
        var response = callHttpMethod(HttpMethod.GET,
                "/users/me",
                accessToken,
                null,
                UserDto.class);

        //then
        UserDto body = response.getBody();
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        //and
        assertNotNull(body);
        assertEquals(body.getId(), user.getId());
        assertEquals(body.getEmail(), user.getEmail());
        assertEquals(body.getName(), user.getName());
        assertEquals(body.getPassword(), "######");
        assertEquals(body.getRoles().toString(), user.getRoles().toString());
    }

    @Test
    void admin_should_be_able_to_update_user() {
        //given
        User user = TestUserFactory.createStudent();
        userService.save(user);
        User toUpdate = new User(
                user.getId(),
                "email@email.com",
                "newPerson",
                "newpassword",
                List.of(UserRole.STUDENT)
        );
        String adminAccessToken = getTokenForAdmin();

        //when
        var response = callHttpMethod(HttpMethod.PUT,
                "/users",
                adminAccessToken,
                toUpdate,
                UserDto.class);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        //and
        UserDto body = response.getBody();
        assertNotNull(body);
        assertEquals(user.getId(), body.getId());
        assertEquals(toUpdate.getEmail(), body.getEmail());
        assertEquals(toUpdate.getName(), body.getName());
        assertEquals("######", body.getPassword());
        assertEquals(toUpdate.getRoles().stream().map(UserRole::getValue).collect(Collectors.toList()), body.getRoles());
    }

    @Test
    void admin_should_be_get_response_code_404_when_update_user_not_exits() {
        //given
        String token = getTokenForAdmin();
        User fakeUser = TestUserFactory.createStudent();

        //when
        var response = callHttpMethod(HttpMethod.PUT,
                "/users",
                token,
                fakeUser,
                MessageResponse.class);

        //then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void student_should_be_not_able_to_update_user() {
        //given
        User user = TestUserFactory.createStudent();
        userService.save(user);
        User userToUpdate = new User(
                user.getId(),
                "otherUser@email.com",
                "Person",
                "password",
                List.of(UserRole.STUDENT)
        );
        String token = getAccessTokenForUser(user.getEmail(), user.getPassword());

        //when
        var response = callHttpMethod(HttpMethod.PUT,
                "/users",
                token,
                userToUpdate,
                MessageResponse.class);

        //then
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void admin_should_be_able_to_delete_user() {
        //given
        User user = TestUserFactory.createStudent();
        String adminAccessToken = getTokenForAdmin();
        userService.save(user);

        //when
        var response = callHttpMethod(
                HttpMethod.DELETE,
                "/users/" + user.getId(),
                adminAccessToken,
                null,
                UserDto.class);

        //then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void admin_should_get_response_code_404_when_user_not_exits() {
        //given
        User user = TestUserFactory.createStudent();
        String token = getTokenForAdmin();

        //when
        var response = callHttpMethod(
                HttpMethod.DELETE,
                "/users/" + user.getId(),
                token,
                null,
                MessageResponse.class);

        //then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void student_should_not_be_able_to_delete_user() {
        //given
        User firstUser = TestUserFactory.createStudent();
        User secondUser = TestUserFactory.createStudent();
        userService.save(firstUser);
        userService.save(secondUser);
        String token = getAccessTokenForUser(firstUser.getEmail(), firstUser.getPassword());

        //when
        var response = callHttpMethod(
                HttpMethod.DELETE,
                "/users/" + secondUser.getId(),
                token,
                null,
                MessageResponse.class);

        //then
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }


}
