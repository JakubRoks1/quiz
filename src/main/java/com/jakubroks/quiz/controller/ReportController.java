package com.jakubroks.quiz.controller;

import com.jakubroks.quiz.dto.QuizResultDTO;
import com.jakubroks.quiz.service.GameService;
import com.jakubroks.quiz.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {

    private final GameService gameService;
    private final ReportService reportService;

    @GetMapping
    public ResponseEntity<byte[]> downloadReport(@PathVariable String gameId) {
        QuizResultDTO result = gameService.getFinishedGame(gameId);
        if (result == null) {
            return ResponseEntity.notFound().build();
        }

        byte[] pdfBytes = reportService.generateReport(result);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=quiz_report_" + gameId + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

}
