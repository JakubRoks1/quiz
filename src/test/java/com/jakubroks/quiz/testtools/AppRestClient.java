package com.jakubroks.quiz.testtools;

import com.jakubroks.quiz.controller.QuizController;
import com.jakubroks.quiz.dto.AuthKey;
import com.jakubroks.quiz.dto.LoginRequest;
import com.jakubroks.quiz.dto.RegisterRequest;
import com.jakubroks.quiz.entity.User;
import com.jakubroks.quiz.model.Quiz;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

public class AppRestClient {

    private final RestClient restClient;

    public AppRestClient(String url) {
        restClient = RestClient.builder()
            .baseUrl(url)
            .build();
    }


    private <T> ResponseEntity<T> executePost(String uri, Object body, Class<T> responseClass, String authKey) {

        var post = restClient.post().uri(uri);
        if (authKey != null) {
            post = post.header("X-KEY", authKey);
        }
        return post.contentType(MediaType.APPLICATION_JSON)
            .body(body)
            .retrieve()
            .toEntity(responseClass);
    }

    public ResponseEntity<User> registerUser(String userName, String email, String password) {

        return executePost("/register",
            new RegisterRequest(userName, email, password),
            User.class, null);
    }

    public ResponseEntity<AuthKey> authenticate(String email, String password) {
        return executePost("/auth",
            new LoginRequest(email, password),
            AuthKey.class, null);
    }

    public ResponseEntity<Quiz> createQuiz(String authKey, String title) {
        return executePost("/quizzes", new QuizController.QuizDto(null, title), Quiz.class, authKey);
    }
}
