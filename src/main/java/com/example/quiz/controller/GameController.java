package com.example.quiz.controller;

import com.example.quiz.entry.GameEntry;
import com.example.quiz.input.AnswerInput;
import com.example.quiz.input.GameInput;
import com.example.quiz.service.GameService;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/start")
    public ResponseEntity<?> startGame(@RequestBody GameInput gameInput) {
        GameEntry people = gameService.startGame(gameInput);

        return ResponseEntity.ok(people);
    }

    @PostMapping("/answer")
    public ResponseEntity<?> answer(@RequestBody AnswerInput answerInput) {
        return ResponseEntity.ok(gameService.submitAnswer(answerInput));
    }
}
