package com.jakubroks.quiz.testtools;

import com.jakubroks.quiz.dto.AuthKey;
import com.jakubroks.quiz.dto.LoginRequest;
import com.jakubroks.quiz.dto.RegisterRequest;
import com.jakubroks.quiz.entity.User;
import org.assertj.core.api.Assertions;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

public class ActionsSupport {

    private static final String URL = "http://localhost:8080";

    protected final RestClient restClient = RestClient.builder()
            .baseUrl(URL)
            .build();

    protected String registerAndLogin() {
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

        AuthKey authKey = restClient.post()
                .uri("/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new LoginRequest(email, password))
                .retrieve()
                .body(AuthKey.class);

        Assertions.assertThat(authKey).isNotNull();
        Assertions.assertThat(authKey.key()).isNotBlank();

        return authKey.key();
    }
}
