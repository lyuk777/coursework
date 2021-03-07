package com.coursework.jobseeker.service;

import com.coursework.jobseeker.model.Job;
import com.coursework.jobseeker.model.JobPerformance;
import com.coursework.jobseeker.model.User;
import com.coursework.jobseeker.repository.JobPerformanceRepository;
import com.coursework.jobseeker.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.*;

@Service
public class JobService {

    @Autowired
    UserService userService;

    @Autowired
    JobRepository jobRepository;

    @Autowired
    JobPerformanceRepository jobPerformanceRepository;

    @Autowired
    PaginationService paginationService;

    @Autowired
    InfoMessageService infoMessageService;

    // создание нового задания, если это возможно
    public boolean createJob( String city,
                          String category,
                          String headline,
                          String description,
                          String sum,
                          User user, Model model) throws NumberFormatException {
        int new_sum;
        if (city.isEmpty() || category.isEmpty() || headline.isEmpty() || description.isEmpty() || sum.isEmpty()) {
            infoMessageService.createErrorMessage(model, "Работа не создана",
                    "Заполните все поля");
            return false;
        }
        else {
            try {
                new_sum = Integer.parseInt(sum);
            }
            catch (NumberFormatException e){
                infoMessageService.createErrorMessage(model, "Работа не создана",
                        "Некорректная сумма");
                return false;
            }
        }
        if (new_sum < 0) {
            infoMessageService.createErrorMessage(model, "Работа не создана",
                    "Отрицательная сумма");
            return false;
        }
            Job job = new Job(city, category, headline, description, new_sum, user);
            jobRepository.save(job);
            infoMessageService.createSuccessMessage(model, "Успешно",
                    "Задание добавлено");
            return true;
    }

    // проверка возможности редактирования задания
    public boolean ifEditingIsPermited(Job job, Model model){
        List<JobPerformance> jobPerformanceList = jobPerformanceRepository.findAllByJob(job);
        for (JobPerformance jobPerformance: jobPerformanceList){
            if (jobPerformance.isPerforming()){
                infoMessageService.createErrorMessage(model,"Ошибка",
                        "Невозможно редактировать выполняемое задание");
                return false;
            }
        }
        model.addAttribute("job",job);
        return true;
    }

    // редактирвоание задания
    public boolean editJob( String city,
                           String category,
                           String description,
                           String sum, Job job, Model model) {
        int new_sum;
        if (city.isEmpty() || category.isEmpty() || description.isEmpty() || sum.isEmpty()) {
            infoMessageService.createErrorMessage(model, "Задание не изменено",
                    "Вы не заполнили  все поля");
            return false;
        } else {
            try {
                new_sum = Integer.parseInt(sum);
            }
            catch (NumberFormatException e){
                infoMessageService.createErrorMessage(model, "Задание не изменено",
                        "Некорректная сумма");
                return false;
            }
            if (new_sum < 0){
                infoMessageService.createErrorMessage(model, "Задание не изменено",
                        "Отрицательная сумма");
                return false;
            }
            job.setCity(city);
            job.setCategory(category);
            job.setDescription(description);
            job.setSum(new_sum);
            jobRepository.save(job);
            infoMessageService.createSuccessMessage(model,"Успешно",
                    "Задание изменено");
            return true;
        }
    }

    // удаление задания
    public void deleteJob(Job job, Model model) {
        List<JobPerformance> jobPerformanceList = jobPerformanceRepository.findAllByJob(job);
        for (JobPerformance jobPerformance: jobPerformanceList){
            if (jobPerformance.isPerforming()){
                infoMessageService.createErrorMessage(model,"Ошибка",
                        "Невозможно удалить выполняемое задание");
                return;
            }
        }
        jobRepository.delete(job);
        infoMessageService.createSuccessMessage(model,"Успешно",
                "Задание удалено");
    }


    public List<Job> getJobsByUser(User user, PageRequest pageRequest) {
        List<Job> jobs = jobRepository.findAllByUser(user, pageRequest);
        return jobs;
    }

    public List<Job> getJobsByUser(User user) {
        List<Job> jobs = jobRepository.findAllByUser(user);
        return jobs;
    }

    public List<Job> getAllJobs() {
        List<Job> jobs = jobRepository.findAll();
        return jobs;
    }

    public Job getJobById(long id){
        Job job = jobRepository.findById(id).orElseThrow(IllegalStateException::new);
        return job;
    }

    // получение задания и инициалов его создателя
    private Map<Job, String> getMapOfJobsAndCreators(List<Job> jobs){
        Map<Job, String> jobsAndCreators = new HashMap<>();
        for (Job job: jobs){
            User creator = job.getUser();
            String creatorInitials = userService.getInitials(creator);
            jobsAndCreators.put(job, creatorInitials);
        }
        return jobsAndCreators;
    }

    public Map<Job, String> getAllJobsAndCreators(PageRequest pageRequest){
        List<Job> jobs = jobRepository.findAllBy(pageRequest);
        return getMapOfJobsAndCreators(jobs);
    }

    public Map<Job, String> getJobsAndCreatorsByCategory(String category,PageRequest pageRequest){
        List<Job> jobs = jobRepository.findAllByCategory(category, pageRequest);
        return getMapOfJobsAndCreators(jobs);
    }

    public Map<Job, String> getJobsAndCreatorsByCategory(String category){
        List<Job> jobs = jobRepository.findAllByCategory(category);
        return getMapOfJobsAndCreators(jobs);
    }

    public void pagination(Object collection, int page, int last, int
            size, Model model, String url, String attributeName){
        paginationService.setPages(page,last,size,model);
        model.addAttribute("urlBegin", url);
        model.addAttribute(attributeName,collection);
    }

    // возвращает все задания
    public void allJobsPagination(int page,int size, Model model){
        PageRequest request = PageRequest.of(page - 1, size);
        Map<Job, String> allJobsAndCreators = getAllJobsAndCreators(request);
        int last = (getAllJobs().size() - 1) / size + 1;
        pagination(allJobsAndCreators, page, last, size, model, "/jobs/?","jobsAndCreators");
    }
}
