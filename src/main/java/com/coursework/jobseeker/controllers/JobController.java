package com.coursework.jobseeker.controllers;

import com.coursework.jobseeker.model.Job;
import com.coursework.jobseeker.model.User;
import com.coursework.jobseeker.service.JobPerformanceService;
import com.coursework.jobseeker.service.JobService;
import com.coursework.jobseeker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/jobs")
public class JobController {

    @Autowired
    JobService jobService;

    @Autowired
    UserService userService;

    @Autowired
    JobPerformanceService jobPerformanceService;

    // все задания
    @GetMapping("/")
    public String jobsPage(@AuthenticationPrincipal User user, Model model,
                           @RequestParam(value = "page", defaultValue = "1") int page,
                           @RequestParam(value = "size", defaultValue = "5") int size)
    {
        jobService.allJobsPagination(page, size, model);
        return "/job/jobs";
    }

    //страница добвадения нового задания
    @GetMapping("/addjob")
    public String addJobPage(@AuthenticationPrincipal User user, Model model)
    {
        return "/job/addjob";
    }

    // создать новое задание
    @PostMapping("/addjob")
    public String addProject(@RequestParam String city,
                             @RequestParam String category,
                             @RequestParam String headline,
                             @RequestParam String description,
                             @RequestParam String sum,
                             @AuthenticationPrincipal User user,Model model) {
        boolean ifCreated = jobService.createJob(city, category,headline, description, sum, user, model);
        if (ifCreated){
            return showMyJobs(user,model,1,5);
        }
        else{
            return "/job/addjob";
        }
    }

    // страница редактирования задания
    @GetMapping("/edit/{jobId}")
    public String editJobPage(@PathVariable long jobId, @AuthenticationPrincipal User user, Model model)
    {
        Job job = jobService.getJobById(jobId);
        if (!jobService.ifEditingIsPermited(job, model)){
            return showMyJobs(user,model,1,5);
        }
        return "/job/edit-job";
    }

    // отредактировать задание
    @PostMapping("/edit/{jobId}")
    public String editJob(@PathVariable long jobId,
                          @RequestParam String city,
                          @RequestParam String category,
                          @RequestParam String description,
                          @RequestParam String sum,
                          @AuthenticationPrincipal User user, Model model)
    {
        Job job = jobService.getJobById(jobId);
        boolean isEdited =jobService.editJob(city, category, description, sum, job, model);
        if (isEdited) {
            return showMyJobs(user,model,1,5);
        } else {
            return editJobPage(jobId,user,model);
        }
    }

    @PostMapping("/delete/{jobId}")
    public String deleteJob(@PathVariable long jobId,
                          @AuthenticationPrincipal User user, Model model)
    {
        Job job = jobService.getJobById(jobId);
        jobService.deleteJob(job, model);
        return showMyJobs(user,model,1,5);
    }


    // страница заданий пользователя
    @GetMapping("/myjob")
    public String showMyJobs(@AuthenticationPrincipal User user, Model model,
                             @RequestParam(value = "page", defaultValue = "1") int page,
                             @RequestParam(value = "size", defaultValue = "5") int size) {
        PageRequest request = PageRequest.of(page - 1, size);
        List<Job> jobList = jobService.getJobsByUser(user, request);
        int last = (jobService.getJobsByUser(user).size() - 1) / size + 1;
        jobService.pagination(jobList, page, last, size, model, "/jobs/myjob/?","myjobs");
        return "/job/myjobs";
    }

    // поиск заданий по категории
    @GetMapping("/search")
    public String searchJobs(@RequestParam String category, @AuthenticationPrincipal User user, Model model,
                             @RequestParam(value = "page", defaultValue = "1") int page,
                             @RequestParam(value = "size", defaultValue = "5") int size) {
        if (category.isEmpty()){
            jobService.allJobsPagination(page, size, model);
            model.addAttribute("category","Категория: Не выбрано");
        } else {
            PageRequest request = PageRequest.of(page - 1, size);
            Map<Job, String> jobList = jobService.getJobsAndCreatorsByCategory(category, request);
            int last = (jobService.getJobsAndCreatorsByCategory(category).size() - 1) / size +1;
            jobService.pagination(jobList, page, last, size, model, "/jobs/search/?category=" + category + "&","jobsAndCreators");
            model.addAttribute("category","Категория: " + category);
        }
        return "/job/jobs";
    }

    // просмотреть выбранное задание
    @GetMapping("/showjob/{jobId}")
    public String showJob(@PathVariable long jobId, @AuthenticationPrincipal User user, Model model)
    {
        Job job = jobService.getJobById(jobId);
        model.addAttribute("job",job);
        String creator = userService.getInitials(job.getUser());
        boolean isResponded = jobPerformanceService.isResponded(jobId,user);
        model.addAttribute("creator",creator);
        model.addAttribute("user",user);
        model.addAttribute("isResponded",isResponded);
        return "/job/showjob";
    }

}
