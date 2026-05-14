package com.jakubroks.quiz.testtools;

import com.jakubroks.quiz.entity.Quiz;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class QuizActions extends ActionsSupport {

    @Test
    void givenUniqueQuiz_whenAdding_thenReturnsCreatedQuiz() {
        String key = registerAndLogin();
        String title = "Quiz-" + System.currentTimeMillis();

        Quiz createdQuiz = createQuiz(key, title);

        Assertions.assertThat(createdQuiz.getId()).isNotNull();
        Assertions.assertThat(createdQuiz.getTitle()).startsWith("Quiz-");
    }

    @Test
    void givenExistingQuiz_whenGettingById_thenReturnsQuiz() {
        String key = registerAndLogin();
        String title = "Quiz-" + System.currentTimeMillis();

        Quiz createdQuiz = createQuiz(key, title);

        ResponseEntity<Quiz> result = restClient.get()
                .uri("/quizzes/" + createdQuiz.getId())
                .header("X-KEY", key)
                .retrieve()
                .toEntity(Quiz.class);

        Assertions.assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
        Assertions.assertThat(result.getBody()).isNotNull();
        Assertions.assertThat(result.getBody().getId()).isEqualTo(createdQuiz.getId());
        Assertions.assertThat(result.getBody().getTitle()).isEqualTo(createdQuiz.getTitle());
    }

    @Test
    void givenExistingQuiz_whenUpdating_thenReturnsUpdatedQuiz() {
        String key = registerAndLogin();
        Quiz createdQuiz = createQuiz(key, "Quiz-" + System.currentTimeMillis());

        String updatedTitle = "Updated quiz-" + System.currentTimeMillis();

        String json = """
        {
          "title": "%s"
        }
        """.formatted(updatedTitle);

        ResponseEntity<Quiz> result = restClient.put()
                .uri("/quizzes/" + createdQuiz.getId())
                .header("X-KEY", key)
                .contentType(MediaType.APPLICATION_JSON)
                .body(json)
                .retrieve()
                .toEntity(Quiz.class);

        Assertions.assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
        Assertions.assertThat(result.getBody()).isNotNull();
        Assertions.assertThat(result.getBody().getId()).isEqualTo(createdQuiz.getId());
        Assertions.assertThat(result.getBody().getTitle()).isEqualTo(updatedTitle);

    }

    @Test
    void givenExistingQuiz_whenDeleting_thenReturnsNoContentOrSuccess() {
        String key = registerAndLogin();
        Quiz createdQuiz = createQuiz(key, "Quiz-" + System.currentTimeMillis());

        ResponseEntity<Void> result = restClient.delete()
                .uri("/quizzes/" + createdQuiz.getId())
                .header("X-KEY", key)
                .retrieve()
                .toEntity(Void.class);

        Assertions.assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
    }

    private Quiz createQuiz(String key, String title) {
        String json = """
        {
          "title": "%s"
        }
        """.formatted(title);

        ResponseEntity<Quiz> result = restClient.post()
                .uri("/quizzes")
                .header("X-KEY", key)
                .contentType(MediaType.APPLICATION_JSON)
                .body(json)
                .retrieve()
                .toEntity(Quiz.class);

        Assertions.assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
        Assertions.assertThat(result.getBody()).isNotNull();

        return result.getBody();
    }
}
