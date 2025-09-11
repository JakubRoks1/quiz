package com.jakubroks.quiz.controller;

import com.jakubroks.quiz.dto.QuestionDTO;
import com.jakubroks.quiz.dto.QuizResultDTO;
import com.jakubroks.quiz.entity.SavedGameEntry;
import com.jakubroks.quiz.entry.GameEntry;
import com.jakubroks.quiz.input.AnswerInput;
import com.jakubroks.quiz.input.GameInput;
import com.jakubroks.quiz.service.GameService;
import com.jakubroks.quiz.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@RestController
@RequestMapping("/game")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;
    private final ReportService reportService;


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
            @RequestBody AnswerInput answerInput) throws IOException {
        GameEntry entry = gameService.submitAnswer(userId, answerInput);

        if (entry.questions() != null && entry.answers() != null && entry.score() != null) {
            QuizResultDTO result = new QuizResultDTO(
                    entry.id().toString(),
                    entry.questions().stream()
                            .map(q -> new QuestionDTO(q.getText(), q.getCorrectAnswer()))
                            .toList(),
                    entry.answers(),
                    entry.score()
            );
            byte[] pdfBytes = reportService.generateReport(result);

            SavedGameEntry s = new SavedGameEntry(result);
            // tu trzeba javowo zserializowac obiekt
            System.out.println(Arrays.toString(s.getQuizResult()));
            System.out.println(s.toQuizResultDTO());

            Path filePath = Path.of("reports", "quiz_report_" + result.id() + ".pdf");
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, pdfBytes);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=quiz_report.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);
        } else {
            return ResponseEntity.ok(entry);
        }
    }
}
