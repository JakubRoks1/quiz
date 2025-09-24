package com.jakubroks.quiz.entity;

import com.jakubroks.quiz.dto.QuizResultDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Data;

import java.io.*;

@Entity
@Table(name = "quiz_results")
@Data
public class SavedGameEntry {

    @Id
    private String id;

    @Lob
    private byte[] quizResult;

    // konstruktor pomocniczy
    public SavedGameEntry(QuizResultDTO quizResultDTO) {
        this.id = quizResultDTO.id();
        this.quizResult = serialize(quizResultDTO);
    }

    public SavedGameEntry() {
    }

    public byte[] getQuizResult() {
        return quizResult;
    }

    // getter
    public QuizResultDTO toQuizResultDTO() {
        return deserialize(this.quizResult);
    }

    private byte[] serialize(QuizResultDTO obj) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream out = new ObjectOutputStream(bos)) {
            out.writeObject(obj);
            return bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private QuizResultDTO deserialize(byte[] bytes) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
             ObjectInputStream in = new ObjectInputStream(bis)) {
            return (QuizResultDTO) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
