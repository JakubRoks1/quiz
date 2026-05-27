package com.jakubroks.quiz.testtools;

import com.jakubroks.quiz.controller.QuizController;
import com.jakubroks.quiz.dto.AuthKey;
import com.jakubroks.quiz.dto.LoginRequest;
import com.jakubroks.quiz.dto.QuestionDTO;
import com.jakubroks.quiz.dto.RegisterRequest;
import com.jakubroks.quiz.entity.Question;
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

    public ResponseEntity<Quiz> updateQuiz(String xKey, Long quizId, String title) {
        return restClient.put()
            .uri("/quizzes/" + quizId)
            .header("X-KEY", xKey)
            .contentType(MediaType.APPLICATION_JSON)
            .body(new QuizController.QuizDto(quizId, title))
            .retrieve()
            .toEntity(Quiz.class);
    }

    public ResponseEntity<Question> createQuestion(String xKey, String text, String correctAnswer) {
        return restClient.post()
                .uri("/questions")
                .header("X-KEY", xKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new QuestionDTO(text, correctAnswer))
                .retrieve()
                .toEntity(Question.class);
    }

    public ResponseEntity<Question> updateQuestion(String xKey, Long questionId, String text, String correctAnswer) {
        return restClient.put()
                .uri("/questions/" + questionId)
                .header("X-KEY", xKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new QuestionDTO(text, correctAnswer))
                .retrieve()
                .toEntity(Question.class);
    }

    public ResponseEntity<Quiz> deleteQuiz(String xKey, Long quizId) {
        return restClient.delete()
                .uri("/quizzes/" + quizId)
                .header("X-KEY", xKey)
                .retrieve()
                .toEntity(Quiz.class);
    }

    public ResponseEntity<Void> deleteQuestion(String xKey, Long questionId) {
        return restClient.delete()
                .uri("/questions/" + questionId)
                .header("X-KEY", xKey)
                .retrieve()
                .toEntity(Void.class);
    }
}
