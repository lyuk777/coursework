package com.coursework.jobseeker.repository;

import com.coursework.jobseeker.model.Job;
import com.coursework.jobseeker.model.JobPerformance;
import com.coursework.jobseeker.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface JobRepository extends PagingAndSortingRepository<Job, Long> {
    List<Job> findAllByUser(User user,Pageable pageable);
    List<Job> findAllByCategory(String category, Pageable pageable);
    List<Job> findAllBy(Pageable pageable);
    List<Job> findAll();
    List<Job> findAllByUser(User user);
    List<Job> findAllByCategory(String category);
}
