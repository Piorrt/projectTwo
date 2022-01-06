package pl.sages.javadevpro.projecttwo.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.sages.javadevpro.projecttwo.BaseIT;
import pl.sages.javadevpro.projecttwo.api.user.UserDto;
import pl.sages.javadevpro.projecttwo.domain.UserService;
import pl.sages.javadevpro.projecttwo.domain.user.User;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserEndpointIT extends BaseIT {

    @Autowired
    UserService service;

    @Test
    void admin_should_get_information_about_any_user() {
        //given
        User user = new User(
            "newUser1@example.com",
            "User Name",
            "pass",
            List.of("STUDENT"),
            new ArrayList<>()
        );
        service.saveUser(user);
        String token = getTokenForAdmin();

        //when
        ResponseEntity<UserDto> response = callGetUser(user.getEmail(), token);

        //then
        UserDto body = response.getBody();
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(body.getEmail(), user.getEmail());
        assertEquals(body.getName(), user.getName());
        assertEquals(body.getPassword(), "######");
        assertEquals(body.getRoles().toString(), user.getRoles().toString());
    }

    @Test
    void admin_should_get_response_code_204_when_user_not_exits_in_db() {
        //given
        String token = getTokenForAdmin();

        //when
        ResponseEntity<UserDto> response = callGetUser("notExits@example.com", token);

        //then
        assertEquals(response.getStatusCode(), HttpStatus.NO_CONTENT);
    }

    @Test
    void student_should_not_get_information_about_other_student() {
        //given
        User user1 = new User(
                "newUser4@example.com",
                "User Name",
                "pass",
                List.of("STUDENT"),
                new ArrayList<>()
        );
        User user2 = new User(
                "oldUser5@example.com",
                "Old User Name",
                "pass",
                List.of("STUDENT"),
                new ArrayList<>()
        );
        service.saveUser(user1);
        service.saveUser(user2);
        String accessToken = getAccessTokenForUser(user1.getEmail(), user1.getPassword());

        //when
        ResponseEntity response = callGetUser(user2.getEmail(), accessToken);

        //then
        assertEquals(response.getStatusCode(), HttpStatus.FORBIDDEN);
    }

    @Test
    void admin_should_get_response_code_conflict_when_user_is_in_db() {
        //given
        User user = new User(
            "newUser1@example.com",
            "User Name",
            "pass",
            List.of("STUDENT"),
            new ArrayList<>()
        );
        service.saveUser(user);
        String adminToken = getTokenForAdmin();

        //when
        ResponseEntity<UserDto> response = callSaveUser(user, adminToken);

        //then
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void admin_should_be_able_to_save_new_user() {
        //given
        User user = new User(
            "newUser2@example.com",
            "User Name",
            "pass",
            List.of("STUDENT"),
            new ArrayList<>()
        );
        String adminAccessToken = getTokenForAdmin();

        //when
        ResponseEntity<UserDto> response = callSaveUser(user, adminAccessToken);

        //then
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        //and
        UserDto body = response.getBody();
        assertEquals(body.getEmail(), user.getEmail());
        assertEquals(body.getName(), user.getName());
        assertEquals(body.getPassword(), "######");
        assertEquals(body.getRoles().toString(), user.getRoles().toString());
    }

    @Test
    void student_should_get_information_about_himself() {
        //given
        User user = new User(
                "newUser3@example.com",
                "User Name",
                "pass",
                List.of("STUDENT"),
                new ArrayList<>()
        );
        service.saveUser(user);
        String accessToken = getAccessTokenForUser(user.getEmail(), user.getPassword());

        //when
        ResponseEntity<UserDto> response = callAboutMe(accessToken);

        //then
        UserDto body = response.getBody();
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(body.getEmail(), user.getEmail());
        assertEquals(body.getName(), user.getName());
        assertEquals(body.getPassword(), "######");
        assertEquals(body.getRoles().toString(), user.getRoles().toString());
    }

    @Test
    void admin_should_be_able_to_update_user() {
        //given
        User user = new User(
                "email@email.com",
                "Person",
                "password",
                List.of("STUDENT")
        );
        userService.saveUser(user);

        User userToUpdate = new User(
                "email@email.com",
                "newPerson",
                "newpassword",
                List.of("STUDENT")
        );
        String adminAccessToken = getTokenForAdmin();

        //when
        ResponseEntity<UserDto> response = callUpdateUser(userToUpdate, adminAccessToken);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());

        //and
        UserDto body = response.getBody();
        assertEquals(user.getEmail(), body.getEmail());
        assertEquals(userToUpdate.getName(), body.getName());
        assertEquals("######", body.getPassword());
        assertEquals(userToUpdate.getRoles(), body.getRoles());
    }

    @Test
    void admin_should_be_get_response_code_204_when_update_user_not_exits() {
        //given
        String token = getTokenForAdmin();

        //when
        ResponseEntity<UserDto> response = callGetUser("notUser@email.com", token);

        //then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void student_should_be_not_able_to_update_user() {
        //given
        User user = new User(
                "newUser@example.com",
                "Person",
                "pass",
                List.of("STUDENT"),
                new ArrayList<>()
        );
        userService.saveUser(user);

        User userToUpdate = new User(
                "otherUser@email.com",
                "Person",
                "password",
                List.of("STUDENT")
        );
        String token = getAccessTokenForUser(user.getEmail(), user.getPassword());

        //when
        ResponseEntity<UserDto> response = callUpdateUser(userToUpdate, token);

        //then
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void admin_should_be_able_to_delete_user() {
        //given
        User user = new User(
                "newUser@email.com",
                "Person",
                "pass",
                List.of("STUDENT"),
                new ArrayList<>()
        );
        String adminAccessToken = getTokenForAdmin();
        userService.saveUser(user);

        //when
        ResponseEntity<UserDto> response = callDeleteUser(user, adminAccessToken);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void admin_should_get_response_code_204_when_user_not_exits() {
        //given
        User user = new User(
                "otherUser@email.com",
                "Person",
                "password",
                List.of("STUDENT")
        );
        String token = getTokenForAdmin();

        //when
        ResponseEntity<UserDto> response = callDeleteUser(user, token);

        //then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void student_should_not_be_able_to_delete_user() {
        //given
        User user = new User(
                "newUser@example.com",
                "Person",
                "pass",
                List.of("STUDENT")
        );
        User otherUser = new User(
                "otherUser@email.com",
                "Person",
                "password",
                List.of("STUDENT")
        );
        userService.saveUser(user);
        String token = getAccessTokenForUser(user.getEmail(), user.getPassword());

        //when
        ResponseEntity<UserDto> response = callDeleteUser(otherUser, token);

        //then
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    private ResponseEntity<UserDto> callGetUser(String email, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        headers.add(HttpHeaders.ACCEPT, "application/json");
        headers.add(HttpHeaders.AUTHORIZATION, token);
        return restTemplate.exchange(
            localUrl("/users/" + email),
            HttpMethod.GET,
            new HttpEntity(headers),
            UserDto.class
        );
    }

    private ResponseEntity<UserDto> callAboutMe(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        headers.add(HttpHeaders.ACCEPT, "application/json");
        headers.add(HttpHeaders.AUTHORIZATION, accessToken);
        return restTemplate.exchange(
                localUrl("/users/me"),
                HttpMethod.GET,
                new HttpEntity(headers),
                UserDto.class
        );
    }

    private ResponseEntity<UserDto> callSaveUser(User body, String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        headers.add(HttpHeaders.AUTHORIZATION, accessToken);
        return restTemplate.exchange(
            localUrl("/users"),
            HttpMethod.POST,
            new HttpEntity(body, headers),
            UserDto.class
        );
    }

    private ResponseEntity<UserDto> callUpdateUser(User body, String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        headers.add(HttpHeaders.AUTHORIZATION, accessToken);
        return restTemplate.exchange(
                localUrl("/users"),
                HttpMethod.PUT,
                new HttpEntity(body, headers),
                UserDto.class
        );
    }

    private ResponseEntity<UserDto> callDeleteUser(User body, String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        headers.add(HttpHeaders.AUTHORIZATION, accessToken);
        return restTemplate.exchange(
                localUrl("/users"),
                HttpMethod.DELETE,
                new HttpEntity(body, headers),
                UserDto.class
        );
    }
}
