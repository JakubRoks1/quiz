package com.example.quiz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * Aplikacja potrafi załadować quiz, stworzyć pytania do quiz (POSTami) x
 * Usuńmy na razie odpowiedzi - wielokrotny wybór, ma być jedna odpoweidź - przykład Trzecia planeta od słonca? Ziemia x
 * Skonfiguruj aplikację tak, aby dodane quizy były pernamentne - tzn. po restarcie aplikacji dane dalej istniały x
 * usuwanie pytan / usuwanie quizów (+ edycja) x
 *
 */
@SpringBootApplication
public class QuizApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuizApplication.class, args);
    }

}
