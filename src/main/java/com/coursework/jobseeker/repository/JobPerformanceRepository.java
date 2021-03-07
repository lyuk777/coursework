package com.coursework.jobseeker.repository;

import com.coursework.jobseeker.model.Job;
import com.coursework.jobseeker.model.JobPerformance;
import com.coursework.jobseeker.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface JobPerformanceRepository extends PagingAndSortingRepository<JobPerformance, Long> {
    List<JobPerformance> findAllByJob(Job job);
    JobPerformance findByJobAndUser(Job job, User user);
}
