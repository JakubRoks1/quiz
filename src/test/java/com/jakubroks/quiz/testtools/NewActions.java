package com.jakubroks.quiz.testtools;

import com.jakubroks.quiz.dto.AuthKey;
import com.jakubroks.quiz.dto.LoginRequest;
import com.jakubroks.quiz.dto.RegisterRequest;
import com.jakubroks.quiz.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.ObjectMapper;

//@SpringBootTest
class NewActions {

//    @Autowired
    private ObjectMapper objectMapper;

    private RestClient restClient = RestClient.builder()
        .baseUrl("http://localhost:8080")
        .build();

    @Test
    void registerUser() {

        var result = restClient.post()
            .uri("/register")
            .body(new RegisterRequest("user7", "user7@wp.pl", "abc1abc2"))
            .retrieve()
            .toEntity(User.class);

        Assertions.assertThat(result)
                .returns(HttpStatusCode.valueOf(201), ResponseEntity::getStatusCode);
    }

    @Test
    void registerUserJSON() {

        var json = """
            {
             "username": "user-json1",
             "email": "user-json1@wp.pl",
             "password": "abc1abc2"
            }
            """;


        var result = restClient.post()
            .uri("/register")
            .body(json)
            .contentType(MediaType.APPLICATION_JSON)
            .retrieve()
            .toEntity(User.class);

        Assertions.assertThat(result)
            .returns(HttpStatusCode.valueOf(201), ResponseEntity::getStatusCode);
    }

    @Test
    void authUser() {
        var result = restClient.post()
            .uri("/auth")
            .body(new LoginRequest("user7@wp.pl", "abc1abc2"))
            .retrieve()
            .body(AuthKey.class);

        System.out.println(result.key());
    }

    @Test
    void createAndAuth() {
        registerUser();
        authUser();
    }
}
