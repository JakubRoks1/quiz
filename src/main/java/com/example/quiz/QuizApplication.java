package com.example.quiz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * Aplikacja potrafi załadować quiz, stworzyć pytania do quiz (POSTami)
 * Usuńmy na razie odpowiedzi - wielokrotny wybór, ma być jedna odpoweidź - przykład Trzecia planeta od słonca? Ziemia
 * Skonfiguruj aplikację tak, aby dodane quizy były pernamentne - tzn. po restarcie aplikacji dane dalej istniały
 * usuwanie pytan / usuwanie quizów (+ edycja)
 *
 */
@SpringBootApplication
public class QuizApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuizApplication.class, args);
    }

}
