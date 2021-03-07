package com.coursework.jobseeker.model;

import javax.persistence.*;

@Entity
@Table(name="job_performance")
public class JobPerformance {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "job_id")
    private Job job;

    @Column(name="is_performing")
    private boolean isPerforming;

    @Column(name="is_done")
    private boolean isDone;

    public JobPerformance() {
    }

    public JobPerformance(User user, Job job, boolean isPerforming, boolean isDone) {
        this.user = user;
        this.job = job;
        this.isPerforming = isPerforming;
        this.isDone = isDone;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public boolean isPerforming() {
        return isPerforming;
    }

    public void setPerforming(boolean performing) {
        isPerforming = performing;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }
}
