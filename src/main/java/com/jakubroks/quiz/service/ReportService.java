package com.jakubroks.quiz.service;

import com.jakubroks.quiz.dto.QuizResultDTO;
import com.jakubroks.quiz.service.report.PdfReportGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final PdfReportGenerator pdfReportGenerator;

    @Value("${app.reports.save-to-disk}")
    private boolean saveToDisk;

    @Value("${app.reports.dir}")
    private String reportsDir;

    public byte[] generateReport(QuizResultDTO quizResultDTO) {
        byte[] pdfBytes = pdfReportGenerator.saveQuizResultToPdf(quizResultDTO);

        if (saveToDisk) {
            saveReportToFile(pdfBytes, quizResultDTO.id());
        }


        return pdfBytes;
    }

    private void saveReportToFile(byte[] pdfBytes, String resultId) {
        try {
            Path dirPath = Path.of(reportsDir);
            Files.createDirectories(dirPath);

            Path filePath = dirPath.resolve("quiz_report_" + resultId + ".pdf");
            Files.write(filePath, pdfBytes);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save PDF report to file", e);
        }

    }
}
