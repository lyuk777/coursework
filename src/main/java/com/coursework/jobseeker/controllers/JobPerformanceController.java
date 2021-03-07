package com.coursework.jobseeker.controllers;

import com.coursework.jobseeker.model.User;
import com.coursework.jobseeker.service.JobPerformanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/perform")
public class JobPerformanceController {

    @Autowired
    JobPerformanceService jobPerformanceService;

    // откликнуться на задание
    @PostMapping("/respond/{jobId}")
    public String respondJob(@PathVariable long jobId,  @AuthenticationPrincipal User user, Model model){
        jobPerformanceService.addRespond(user, jobId);
        return "redirect:/jobs/showjob/" + jobId;
    }

    //показать предложения
    @GetMapping("/show-responds/{jobId}")
    public String showResponds(@PathVariable long jobId, @AuthenticationPrincipal User user, Model model){
        jobPerformanceService.getResponds(jobId, model);
        return "job/show-responds";
    }

    // выбрать исполнителя
    @PostMapping("/accept/{jobId}/{userId}")
    public String acceptOffer(@PathVariable("jobId") long jobId, @PathVariable("userId") long userId, @AuthenticationPrincipal User user){
        jobPerformanceService.acceptRespond(jobId, userId);
        return "redirect:/perform/show-responds/" + jobId;
    }

    // пометить задание, как выполненное
    @PostMapping("/complete/{jobId}/{userId}")
    public String completeJob(@PathVariable("jobId") long jobId, @PathVariable("userId") long userId, @AuthenticationPrincipal User user){
        jobPerformanceService.completeJob(jobId, userId);
        return "redirect:/perform/show-responds/" + jobId;
    }
}
