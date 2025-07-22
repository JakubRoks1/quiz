package com.jakubroks.quiz.controller;

import com.jakubroks.quiz.entry.GameEntry;
import com.jakubroks.quiz.input.AnswerInput;
import com.jakubroks.quiz.input.GameInput;
import com.jakubroks.quiz.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
    public ResponseEntity<?> startGame(
            @RequestHeader("user") String userId,
            @RequestBody GameInput gameInput) {
        GameEntry people = gameService.startGame(userId, gameInput);
        return ResponseEntity.ok(people);
    }

    @PostMapping("/answer")
    public ResponseEntity<?> answer(
            @RequestHeader("user") String userId,
            @RequestBody AnswerInput answerInput) {
        return ResponseEntity.ok(gameService.submitAnswer(userId, answerInput));
    }
}
