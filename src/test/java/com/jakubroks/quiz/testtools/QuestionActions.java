package com.jakubroks.quiz.testtools;

import com.jakubroks.quiz.entity.Question;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class QuestionActions extends ActionsSupport {

    @Test
    void givenQuestion_whenAdding_thenReturnsCreatedQuestion() {
        String key = registerAndLogin();

        String unique = String.valueOf(System.currentTimeMillis());
        String questionText = "Question-" + unique;
        String correctAnswer = "Answer-" + unique;

        Question createdQuestion = createQuestion(key, questionText, correctAnswer);

        Assertions.assertThat(createdQuestion.getId()).isNotNull();
        Assertions.assertThat(createdQuestion.getText()).isEqualTo(questionText);
        Assertions.assertThat(createdQuestion.getCorrectAnswer()).isEqualTo(correctAnswer);
    }

    @Test
    void givenQuestionJson_whenAdding_thenReturnsCreatedQuestion() {
        String key = registerAndLogin();

        String unique = String.valueOf(System.currentTimeMillis());
        String questionText = "Question-" + unique;
        String correctAnswer = "Answer-" + unique;

        String json = """
        {
          "text": "%s",
          "correctAnswer": "%s"
        }
        """.formatted(questionText, correctAnswer);

        ResponseEntity<Question> result = restClient.post()
                .uri("/questions")
                .header("X-KEY", key)
                .contentType(MediaType.APPLICATION_JSON)
                .body(json)
                .retrieve()
                .toEntity(Question.class);

        Assertions.assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
        Assertions.assertThat(result.getBody()).isNotNull();
        Assertions.assertThat(result.getBody().getId()).isNotNull();
        Assertions.assertThat(result.getBody().getText()).isEqualTo(questionText);
        Assertions.assertThat(result.getBody().getCorrectAnswer()).isEqualTo(correctAnswer);
    }



    private Question createQuestion(String key, String questionText, String correctAnswer) {
        String json = """
        {
          "text": "%s",
          "correctAnswer": "%s"
        }
        """.formatted(questionText, correctAnswer);

        ResponseEntity<Question> result = restClient.post()
                .uri("/questions")
                .header("X-KEY", key)
                .contentType(MediaType.APPLICATION_JSON)
                .body(json)
                .retrieve()
                .toEntity(Question.class);

        Assertions.assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
        Assertions.assertThat(result.getBody()).isNotNull();

        return result.getBody();
    }

}
