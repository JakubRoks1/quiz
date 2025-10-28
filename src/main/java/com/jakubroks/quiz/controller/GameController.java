package com.jakubroks.quiz.controller;

import com.jakubroks.quiz.dto.QuestionDTO;
import com.jakubroks.quiz.dto.QuizResultDTO;
import com.jakubroks.quiz.entity.SavedGameEntry;
import com.jakubroks.quiz.entity.User;
import com.jakubroks.quiz.entry.GameEntry;
import com.jakubroks.quiz.input.AnswerInput;
import com.jakubroks.quiz.input.GameInput;
import com.jakubroks.quiz.repository.SavedGameEntryRepository;
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
//@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;
    private final ReportService reportService;

    private final UserService userService;

    private final SavedGameEntryRepository savedGameEntryRepository;

    public GameController(GameService gameService, ReportService reportService, SavedGameEntryRepository savedGameEntryRepository, UserService userService) {
        this.gameService = gameService;
        this.reportService = reportService;
        this.savedGameEntryRepository = savedGameEntryRepository;
        this.userService = userService;
    }

    private ResponseEntity<Object> requireLogin(String key, Function<User, ResponseEntity<Object>> ok) {
        if (key == null || key.isBlank()) {
            return ResponseEntity.status(401).body(Map.of("message","Missing X-KEY"));
        }
        return userService.getByKey(key)
                .map(ok)
                .orElseGet(() -> ResponseEntity.status(401).body(Map.of("message","Invalid or expired key")));
    }


    @PostMapping("/start")
    public ResponseEntity<Object> start(
            @RequestHeader(value="X-KEY", required=false) String key,
            @RequestBody GameInput in) {
        return requireLogin(key, u -> ResponseEntity.ok(gameService.startGame(u.getId().toString(), in)));
    }

    @PostMapping("/answer")
    public ResponseEntity<Object> answer(
            @RequestHeader(value = "X-KEY", required = false) String key,
            @RequestBody AnswerInput answerInput) throws IOException {

        return requireLogin(key, u -> {
            GameEntry entry = gameService.submitAnswer(u.getId().toString(), answerInput);

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

            byte[] pdfBytes = reportService.generateReport(result);

            SavedGameEntry s = new SavedGameEntry(result);
            // tu trzeba javowo zserializowac obiekt
            System.out.println(Arrays.toString(s.getQuizResult()));
            System.out.println(s.toQuizResultDTO());

            Path filePath = Path.of("reports", "quiz_report_" + result.id() + ".pdf");

            try {
                Files.createDirectories(filePath.getParent());
                Files.write(filePath, pdfBytes);
            } catch (IOException e) {
                throw new RuntimeException("Failed to save PDF report", e);
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=quiz_report.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);
        } else {
            return ResponseEntity.ok(entry);
        }
    });
    }


    @GetMapping("/result/{id}")
    public ResponseEntity<QuizResultDTO> getQuizResult(@PathVariable String id) {
        SavedGameEntry entry = savedGameEntryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        QuizResultDTO result = entry.toQuizResultDTO();
        return ResponseEntity.ok(result);
    }
}
