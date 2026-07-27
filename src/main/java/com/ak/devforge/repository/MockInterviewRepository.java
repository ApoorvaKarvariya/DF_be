package com.ak.devforge.repository;

import com.ak.devforge.model.MockInterview;
import com.ak.devforge.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MockInterviewRepository extends JpaRepository<MockInterview, Long> {

    List<MockInterview> findByUserOrderByCreatedAtDesc(User user);

    List<MockInterview> findByUserAndStatusOrderByCreatedAtDesc(
            User user, MockInterview.Status status);
}