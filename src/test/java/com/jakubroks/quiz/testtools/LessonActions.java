package com.jakubroks.quiz.testtools;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

/** Przepisz wszystkie akcje tutaj w podobnym stylu, nie uzywaj JSONów tylko obiektów modelowych */
public class LessonActions {

    private static final String USER_NAME = "mat2";
    private static final String EMAIL = "mat2@wp.pl";
    private static final String PASSWORD = "pwd1pwd1pwd1";
    private static final String QUIZ_NAME = "MyQuizTest 22222";
    private static final AppRestClient APP_REST_CLIENT = new AppRestClient("http://localhost:8080");

    // context
    private String xKey = "ea905379-04e6-4e82-bebb-a1deab418299";

    @Test
    public void createUser() {
        var response = APP_REST_CLIENT.registerUser(USER_NAME, EMAIL, PASSWORD);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void authUser() {
        var response = APP_REST_CLIENT.authenticate(EMAIL, PASSWORD);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        var key = response.getBody().key();
        System.out.println(key);
        xKey = key;
    }

    @Test
    public void addQuiz() {
        Assertions.assertThat(xKey).isNotNull();
        var response = APP_REST_CLIENT.createQuiz(xKey, QUIZ_NAME);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        System.out.println("Quiz created: " + response.getBody().getId() + " " + response.getBody().getTitle());

    }

    @Test
    public void createAndAuthUserThenCreateQuiz() {
//        createUser();
        authUser();
        addQuiz();
    }


}
