package com.ak.devforge.repository;

import com.ak.devforge.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findByDifficulty(Question.Difficulty difficulty);

    List<Question> findByCategory(Question.Category category);

    List<Question> findByCompanyIgnoreCase(String company);

    List<Question> findByTopicIgnoreCase(String topic);

    @Query("SELECT q FROM Question q WHERE " +
            "(:difficulty IS NULL OR q.difficulty = :difficulty) AND " +
            "(:category IS NULL OR q.category = :category) AND " +
            "(:company IS NULL OR LOWER(q.company) = LOWER(:company)) AND " +
            "(:topic IS NULL OR LOWER(q.topic) = LOWER(:topic))")
    List<Question> filterQuestions(
            @Param("difficulty") Question.Difficulty difficulty,
            @Param("category") Question.Category category,
            @Param("company") String company,
            @Param("topic") String topic);

    @Query("SELECT q FROM Question q WHERE " +
            "LOWER(q.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(q.topic) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(q.company) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Question> searchQuestions(@Param("keyword") String keyword);
}