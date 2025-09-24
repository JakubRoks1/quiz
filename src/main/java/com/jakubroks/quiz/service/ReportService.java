package com.jakubroks.quiz.service;

import com.jakubroks.quiz.dto.QuizResultDTO;
import com.jakubroks.quiz.service.report.PdfReportGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
//@RequiredArgsConstructor
public class ReportService {

    private final PdfReportGenerator pdfReportGenerator;

    public ReportService(PdfReportGenerator pdfReportGenerator) {
        this.pdfReportGenerator = pdfReportGenerator;

    }

    public byte[] generateReport(QuizResultDTO quizResultDTO) {
        return pdfReportGenerator.saveQuizResultToPdf(quizResultDTO);
    }

}
