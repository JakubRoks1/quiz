package com.jakubroks.quiz.repository;

import com.jakubroks.quiz.entity.SavedGameEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SavedGameEntryRepository extends JpaRepository<SavedGameEntry, String> {}

