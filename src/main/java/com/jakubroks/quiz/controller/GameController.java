package com.jakubroks.quiz.controller;

import com.jakubroks.quiz.dto.QuestionDTO;
import com.jakubroks.quiz.dto.QuizResultDTO;
import com.jakubroks.quiz.entity.SavedGameEntry;
import com.jakubroks.quiz.entity.User;
import com.jakubroks.quiz.entry.GameEntry;
import com.jakubroks.quiz.input.AnswerInput;
import com.jakubroks.quiz.input.GameInput;
import com.jakubroks.quiz.repository.SavedGameEntryRepository;
import com.jakubroks.quiz.service.AuthService;
import com.jakubroks.quiz.service.GameService;
import com.jakubroks.quiz.service.ReportService;
import com.jakubroks.quiz.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

@RestController
@RequestMapping("/game")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;
    private final ReportService reportService;

    private final UserService userService;

    private final SavedGameEntryRepository savedGameEntryRepository;

    private final AuthService authService;

    @PostMapping("/start")
    public ResponseEntity<?> startGame(
            @RequestAttribute("user") User user,
            @RequestBody GameInput gameInput
    ) {
        GameEntry entry = gameService.startGame(user.getId().toString(), gameInput);
        return ResponseEntity.ok(entry);
    }


    @PostMapping("/answer")
    public ResponseEntity<?> answer(
            @RequestAttribute("user") User user,
            @RequestBody AnswerInput answerInput) throws IOException {
        GameEntry entry = gameService.submitAnswer(user.getId().toString(), answerInput);


        if (entry.questions() != null && entry.answers() != null && entry.score() != null) {
            QuizResultDTO result = new QuizResultDTO(
                    entry.id().toString(),
                    entry.questions().stream()
                            .map(q -> new QuestionDTO(q.getText(), q.getCorrectAnswer()))
                            .toList(),
                    entry.answers(),
                    entry.score()
            );

            gameService.saveFinishedGame(result);

            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.ok(entry);
        }
    }


    @GetMapping("/result/{id}")
    public ResponseEntity<QuizResultDTO> getQuizResult(@PathVariable String id) {
        SavedGameEntry entry = savedGameEntryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        QuizResultDTO result = entry.toQuizResultDTO();
        return ResponseEntity.ok(result);
    }
}
