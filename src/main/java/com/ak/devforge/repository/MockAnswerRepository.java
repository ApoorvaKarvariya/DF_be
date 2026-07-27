package com.ak.devforge.repository;

import com.ak.devforge.model.MockAnswer;
import com.ak.devforge.model.MockInterview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MockAnswerRepository extends JpaRepository<MockAnswer, Long> {

    List<MockAnswer> findByMockInterview(MockInterview mockInterview);
}