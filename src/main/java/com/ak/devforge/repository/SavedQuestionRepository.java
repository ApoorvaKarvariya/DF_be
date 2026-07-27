package com.ak.devforge.repository;

import com.ak.devforge.model.Question;
import com.ak.devforge.model.SavedQuestion;
import com.ak.devforge.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavedQuestionRepository extends JpaRepository<SavedQuestion, Long> {

    List<SavedQuestion> findByUser(User user);

    Optional<SavedQuestion> findByUserAndQuestion(User user, Question question);

    Boolean existsByUserAndQuestion(User user, Question question);

    void deleteByUserAndQuestion(User user, Question question);
}