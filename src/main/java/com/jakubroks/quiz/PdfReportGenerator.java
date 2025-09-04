package com.jakubroks.quiz;

import com.jakubroks.quiz.dto.QuestionDTO;
import com.jakubroks.quiz.dto.QuizResultDTO;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import java.io.OutputStream;
import java.io.IOException;

public class PdfReportGenerator {
    public static void saveQuizResultToPdf(QuizResultDTO result, OutputStream out) throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        int y = 750;
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
        contentStream.beginText();
        contentStream.newLineAtOffset(50, y);
        contentStream.showText("Quiz Results");
        contentStream.endText();

        y -= 40;
        contentStream.setFont(PDType1Font.HELVETICA, 14);
        contentStream.beginText();
        contentStream.newLineAtOffset(50, y);
        contentStream.showText("Score: " + result.score());
        contentStream.endText();

        y -= 30;
        for (int i = 0; i < result.questions().size(); i++) {
            QuestionDTO q = result.questions().get(i);
            String userAnswer = i < result.answers().size() ? result.answers().get(i) : "";

            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(50, y);
            contentStream.showText("Q" + (i + 1) + ": " + q.text());
            contentStream.endText();
            y -= 20;

            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(60, y);
            contentStream.showText("Your Answer: " + userAnswer);
            contentStream.endText();
            y -= 15;

            contentStream.beginText();
            contentStream.newLineAtOffset(60, y);
            contentStream.showText("Correct Answer: " + q.correctAnswer());
            contentStream.endText();
            y -= 25;
        }

        contentStream.close();
        document.save(out);
        document.close();
    }
}
