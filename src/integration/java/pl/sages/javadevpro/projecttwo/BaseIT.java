package pl.sages.javadevpro.projecttwo;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.sages.javadevpro.projecttwo.config.CredentialsDTO;
import pl.sages.javadevpro.projecttwo.domain.UserService;
import pl.sages.javadevpro.projecttwo.domain.user.User;


import java.util.ArrayList;
import java.util.List;

@AutoConfigureDataMongo
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
    classes = ProjectTwoApplication.class
)
@ExtendWith(SpringExtension.class)
public class BaseIT {

    @Autowired
    protected TestRestTemplate restTemplate;

    @Autowired
    protected UserService userService;

    @Autowired
    MongoTemplate mongoTemplate;

    @BeforeEach
    void init() {
        dropDb();
        addTestUsers();
    }

    protected void dropDb(){
        mongoTemplate.getDb().drop();
    }

    private static User adminUser = new User(
            "admin@example.pl",
            "Stefan Burczymucha",
            "password",
            List.of("ADMIN"),
            new ArrayList<>()
        );

    protected String localUrl(String endpoint) {
        return "http://localhost:7777" + endpoint;
    }

    protected void addTestUsers() {
        userService.saveUser(adminUser);
    }

    protected String getAccessTokenForUser(String email, String password) {
        CredentialsDTO body = new CredentialsDTO();
        body.setEmail(email);
        body.setPassword(password);
        return singIn(body);
    }

    protected String getTokenForAdmin() {
        CredentialsDTO body = new CredentialsDTO();
        body.setEmail(adminUser.getEmail());
        body.setPassword(adminUser.getPassword());
        return singIn(body);
    }

    private String singIn(CredentialsDTO body) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        ResponseEntity<Void> response = restTemplate.exchange(
            localUrl("/login"),
            HttpMethod.POST,
            new HttpEntity(body, headers),
            void.class
        );
        return response.getHeaders().getFirst("Authorization");
    }

}
