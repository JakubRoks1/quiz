package com.jakubroks.quiz.service;

import com.jakubroks.quiz.dto.QuizResultDTO;
import com.jakubroks.quiz.service.report.PdfReportGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportService {

    private final PdfReportGenerator pdfReportGenerator;

    @Value("${app.reports.save-to-disk}")
    private boolean saveToDisk;

    @Value("${app.reports.dir}")
    private String reportsDir;

    public byte[] generateReport(QuizResultDTO quizResultDTO) {
        String resultId = quizResultDTO.id();

        log.info("Generating PDF report for resultId={}", resultId);

        byte[] pdfBytes = pdfReportGenerator.saveQuizResultToPdf(quizResultDTO);

        log.debug(
                "PDF report generated for resultId={}, size={} bytes",
                resultId,
                pdfBytes.length
        );

        if (saveToDisk) {
            saveReportToFile(pdfBytes, resultId);
        } else {
            log.debug(
                    "Saving report to disk is disabled for resultId={}",
                    resultId
            );
        }

        return pdfBytes;
    }

    private void saveReportToFile(byte[] pdfBytes, String resultId) {
        Path dirPath = Path.of(reportsDir);
        Path filePath = dirPath.resolve("quiz_report_" + resultId + ".pdf");


        try {
            Files.createDirectories(dirPath);
            Files.write(filePath, pdfBytes);

            log.info(
                    "PDF report saved successfully for resultId={}, path={}",
                    resultId,
                    filePath.toAbsolutePath()
            );
        } catch (IOException e) {
            log.error(
                    "Failed to save PDF report for resultId={}, path={}",
                    resultId,
                    filePath.toAbsolutePath(),
                    e
            );

            throw new RuntimeException(
                    "Failed to save PDF report to file",
                    e
            );
        }
    }
}
