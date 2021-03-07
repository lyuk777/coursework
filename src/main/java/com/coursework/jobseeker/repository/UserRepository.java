package com.coursework.jobseeker.repository;

import com.coursework.jobseeker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
        User findByUsername(String username);
        User findByEmail(String email);
        User findById(long id);
}
