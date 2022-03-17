package pl.sages.javadevpro.projecttwo;

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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.sages.javadevpro.projecttwo.domain.user.User;
import pl.sages.javadevpro.projecttwo.domain.user.UserRole;
import pl.sages.javadevpro.projecttwo.domain.user.UserService;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.List;

@ActiveProfiles("it")
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

    private static final User adminUser = new User(
            "ID3",
            "admin@example.pl",
            "Stefan Burczymucha",
            "password",
            List.of(UserRole.ADMIN)
        );

    protected String localUrl(String endpoint) {
        return "http://localhost:7777" + endpoint;
    }

    protected void addTestUsers() {
        userService.save(adminUser);
    }

    protected String getAccessTokenForUser(String email, String password) {
        String token = "Basic ";
        try {
            token = token + Base64.getEncoder()
                .encodeToString((email + ":" + password)
                    .getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return token;
    }

    protected String getTokenForAdmin() {
        String adminToken = "Basic ";
        try {
            adminToken = adminToken + Base64.getEncoder()
                .encodeToString((adminUser.getEmail() + ":" + adminUser.getPassword())
                .getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return adminToken;
    }

    protected <T, U> ResponseEntity<U> callHttpMethod(
            HttpMethod httpMethod,
            String url,
            String accessToken,
            T body,
            Class<U> mapToObject
    ) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        headers.add(HttpHeaders.AUTHORIZATION, accessToken);
        headers.add(HttpHeaders.ACCEPT, "application/json");
        return restTemplate.exchange(
                localUrl(url),
                httpMethod,
                new HttpEntity<>(body, headers),
                mapToObject
        );
    }

}
