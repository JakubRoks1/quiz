package com.jakubroks.quiz.testtools;

import com.jakubroks.quiz.dto.AuthKey;
import com.jakubroks.quiz.dto.LoginRequest;
import com.jakubroks.quiz.dto.RegisterRequest;
import com.jakubroks.quiz.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

class AuthActions extends ActionsSupport {




    @Test
    void givenUniqueUser_whenRegistering_thenReturnsCreatedUser() {
        String unique = String.valueOf(System.currentTimeMillis());

        ResponseEntity<User> result = restClient.post()
                .uri("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new RegisterRequest(
                        "user-" + unique,
                        "user-" + unique + "@wp.pl",
                        "abc1abc2"
                ))
                .retrieve()
                .toEntity(User.class);

        Assertions.assertThat(result.getStatusCode())
                .isEqualTo(HttpStatus.CREATED);

        Assertions.assertThat(result.getBody())
                .isNotNull();
    }

    @Test
    void givenUniqueUserJson_whenRegistering_thenReturnsCreatedUser() {
        String unique = String.valueOf(System.currentTimeMillis());

        String json = """
            {
             "username": "user-json-%s",
             "email": "user-json-%s@wp.pl",
             "password": "abc1abc2"
            }
            """.formatted(unique, unique);

        ResponseEntity<User> result = restClient.post()
                .uri("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .body(json)
                .retrieve()
                .toEntity(User.class);

        Assertions.assertThat(result.getStatusCode())
                .isEqualTo(HttpStatus.CREATED);

        Assertions.assertThat(result.getBody())
                .isNotNull();
    }

    @Test
    void givenRegisteredUser_whenLoggingIn_thenReturnsAuthKey() {
        String unique = String.valueOf(System.currentTimeMillis());

        String username = "user-" + unique;
        String email = "user-" + unique + "@wp.pl";
        String password = "abc1abc2";

        restClient.post()
                .uri("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new RegisterRequest(username, email, password))
                .retrieve()
                .toEntity(User.class);

        AuthKey result = restClient.post()
                .uri("/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new LoginRequest(email, password))
                .retrieve()
                .body(AuthKey.class);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.key()).isNotBlank();
    }


}
