package com.jakubroks.quiz.testtools;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

/** Przepisz wszystkie akcje tutaj w podobnym stylu, nie uzywaj JSONów tylko obiektów modelowych */
class Actions {

    private static final String USER_NAME = "mat2";
    private static final String EMAIL = "mat2@wp.pl";
    private static final String PASSWORD = "pwd1pwd1pwd1";
    private static final String QUIZ_NAME = "MyQuizTest 22222";
    private static final String UPDATED_QUIZ_NAME = "Updated Quiz Name";
    private static final AppRestClient APP_REST_CLIENT = new AppRestClient("http://localhost:8080");
    private static final String QUESTION_TEXT = "What is the capital of Spain?";
    private static final String CORRECT_ANSWER = "Madrid";
    private static final String UPDATED_QUESTION_TEXT = "What is the capital of Italy?";
    private static final String UPDATED_CORRECT_ANSWER = "Rome";

    // context
    private String xKey = "00787cd0-c0a1-4314-847b-f22b0a84bb49";
    private Long quizId;
    private Long questionId;

    @Test
    void createUser() {
        var response = APP_REST_CLIENT.registerUser(USER_NAME, EMAIL, PASSWORD);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void authUser() {
        var response = APP_REST_CLIENT.authenticate(EMAIL, PASSWORD);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        var key = response.getBody().key();
        System.out.println(key);
        xKey = key;
    }

    @Test
    void addQuiz() {
        Assertions.assertThat(xKey).isNotNull();
        var response = APP_REST_CLIENT.createQuiz(xKey, QUIZ_NAME);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        this.quizId = response.getBody().getId();
        System.out.println("Quiz created: " + response.getBody().getId() + " " + response.getBody().getTitle());

    }

    @Test
    void updateQuiz() {
        authUser();
        addQuiz();

        var response = APP_REST_CLIENT.updateQuiz(xKey, quizId, UPDATED_QUIZ_NAME);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().getId()).isEqualTo(quizId);
        Assertions.assertThat(response.getBody().getTitle()).isEqualTo(UPDATED_QUIZ_NAME);

        System.out.println("Quiz updated: " + response.getBody().getId() + " " + response.getBody().getTitle());

    }

    @Test
    void deleteQuiz() {
        authUser();
        addQuiz();

        Assertions.assertThat(quizId).isNotNull();

        var response = APP_REST_CLIENT.deleteQuiz(xKey, quizId);

        Assertions.assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();

        System.out.println("Quiz deleted: " + quizId);
    }

    @Test
    void addQuestion() {
        Assertions.assertThat(xKey).isNotNull();

        var response = APP_REST_CLIENT.createQuestion(xKey, QUESTION_TEXT, CORRECT_ANSWER);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();

        this.questionId = response.getBody().getId();

        System.out.println("Question created: " + response.getBody().getId() + " " + response.getBody().getText());
    }

    @Test
    void updateQuestion() {
        authUser();
        addQuestion();

        var response = APP_REST_CLIENT.updateQuestion(xKey, questionId, UPDATED_QUESTION_TEXT, UPDATED_CORRECT_ANSWER);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().getId()).isEqualTo(questionId);
        Assertions.assertThat(response.getBody().getText()).isEqualTo(UPDATED_QUESTION_TEXT);
        Assertions.assertThat(response.getBody().getCorrectAnswer()).isEqualTo(UPDATED_CORRECT_ANSWER);

        this.questionId = response.getBody().getId();
        System.out.println("Question updated: " + response.getBody().getId() + " " + response.getBody().getText());
    }

    @Test
    void deleteQuestion() {
        authUser();
        addQuestion();

        Assertions.assertThat(questionId).isNotNull();

        var response = APP_REST_CLIENT.deleteQuestion(xKey, questionId);

        Assertions.assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();

        System.out.println("Question deleted: " + questionId);
    }

    @Test
    void createAndAuthUserThenCreateQuiz() {
        createUser();
        authUser();
        addQuiz();
    }

    @Test
    void authUserThenCreateQuestion() {
        authUser();
        addQuestion();
    }

    @Test
    void authUserThenCreateAndUpdateQuiz() {
        authUser();
        addQuiz();
        updateQuiz();
    }

    @Test
    void authUserThenCreateAndUpdateQuestion() {
        authUser();
        addQuestion();
        updateQuestion();
    }

    @Test
    void authUserThenCreateQuizAndQuestion() {
        authUser();
        addQuiz();
        addQuestion();
    }

    @Test
    void authUserThenCreateQuizUpdateQuizAndCreateQuestion() {
        authUser();
        addQuiz();
        updateQuiz();
        addQuestion();
    }

    @Test
    void authUserThenCreateAndDeleteQuiz() {
        authUser();
        addQuiz();
        deleteQuiz();
    }

    @Test
    void authUserThenCreateUpdateAndDeleteQuiz() {
        authUser();
        addQuiz();
        updateQuiz();
        deleteQuiz();
    }

    @Test
    void authUserThenCreateAndDeleteQuestion() {
        authUser();
        addQuestion();
        deleteQuestion();
    }

    @Test
    void authUserThenCreateUpdateAndDeleteQuestion() {
        authUser();
        addQuestion();
        updateQuestion();
        deleteQuestion();
    }

    @Test
    void fullFlow() {
        createUser();
        authUser();
        addQuiz();
        updateQuiz();
        addQuestion();
        updateQuestion();
    }


}
