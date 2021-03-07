package com.coursework.jobseeker.service;

import com.coursework.jobseeker.model.Job;
import com.coursework.jobseeker.model.JobPerformance;
import com.coursework.jobseeker.model.User;
import com.coursework.jobseeker.repository.JobPerformanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

@Service
public class JobPerformanceService {

    @Autowired
    JobPerformanceRepository jobPerformanceRepository;

    @Autowired
    JobService jobService;

    @Autowired
    UserService userService;


    // добавить предложение о выполнении задания
    public void addRespond(User user, long jobId){
        JobPerformance jobPerformance = new JobPerformance(user,jobService.getJobById(jobId),false, false);
        jobPerformanceRepository.save(jobPerformance);
    }

    //принять предложение
    public void acceptRespond(long jobId, long userId){
        Job job = jobService.getJobById(jobId);
        User user = userService.getUserById(userId);
        JobPerformance jobPerformance = jobPerformanceRepository.findByJobAndUser(job, user);
        jobPerformance.setPerforming(true);
        jobPerformanceRepository.save(jobPerformance);
    }

    // завершить задание
    public void completeJob(long jobId, long userId){
        Job job = jobService.getJobById(jobId);
        User user = userService.getUserById(userId);
        JobPerformance jobPerformance = jobPerformanceRepository.findByJobAndUser(job, user);
        jobPerformance.setDone(true);
        jobPerformanceRepository.save(jobPerformance);
    }

    // кто откликнулся на данную работу или кто выполняет
    public void getResponds(long jobId, Model model){
        Job job = jobService.getJobById(jobId);
        List<User> users = new ArrayList<>();
        User userPerformingJob = new User();
        boolean isPerforming = false;
        boolean isDone = false;
        List<JobPerformance> jobPerformances= jobPerformanceRepository.findAllByJob(job);
        for (JobPerformance jobPerformance: jobPerformances){
            if (jobPerformance.isPerforming()) {
                userPerformingJob = jobPerformance.getUser();
                isPerforming= true;
                if (jobPerformance.isDone()) {isDone = true;}
            }
            User user = jobPerformance.getUser();
            users.add(user);
        }
        model.addAttribute("isDone", isDone);
        model.addAttribute("isPerforming", isPerforming);
        model.addAttribute("userPerformingJob", userPerformingJob);
        model.addAttribute("users", users);
        model.addAttribute("job",job);
    }

    // откликнулся ли текущий пользователь на задание
    public boolean isResponded(long jobId, User user){
        Job job = jobService.getJobById(jobId);
        List<JobPerformance> jobPerformances= jobPerformanceRepository.findAllByJob(job);
        for (JobPerformance jobPerformance: jobPerformances){
            if (jobPerformance.getUser().getId() == user.getId()) {return true;}
        }
        return false;
    }
}
