package com.jakubroks.quiz.picker;

import com.jakubroks.quiz.calculator.DifficultyDistributionCalculator;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static com.jakubroks.quiz.entity.Difficulty.EASY;
import static com.jakubroks.quiz.entity.Difficulty.HARD;
import static com.jakubroks.quiz.entity.Difficulty.MEDIUM;

/**
 * Napisz w DifficultyDistributionCalculator różne metody rozdzielania pytań - czyli podajesz na input ile jest pytań (total), i jaki jest stousunek (weights).
 * Każdą z tych wersji przetestuj podając różne inputy (pokazując zarówno działające jak i niedziałające przypadki)
 * Podczas pisania metod w DistributionCalculator skup się na tym aby metody były proste i jednozadaniowe - czyli np. podzel wagi do 1, zsumuj wagi (etc) - czyli wykonują jedną operację
 * Zależy nam aby rozwiązanie było powtarzalne (czyli chcemy unikać randomowego rozkładu pytań) - czyli uruchomienie tej samej metody z tymi samymi parametrami zawsze musi zwrócić ten sam wynik
 * To nie musi być rozwiązanie idealne
 */


class DifficultyDistributionCalculatorTest {

    private DifficultyDistributionCalculator calculator = new DifficultyDistributionCalculator();

    @Test
    void a() {

        var result = calculator.calculate(2, Map.of(
            EASY, 1,
            MEDIUM, 1,
            HARD, 1
        ));

        System.out.println(result);
    }

}











