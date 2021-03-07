package com.coursework.jobseeker.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="jobs")
public class Job {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="city")
    private String city;

    @Column(name="category")
    private String category;

    @Column(name="headline")
    private String headline;

    @Column(name="description")
    private String description;

    @Column(name="sum")
    private int sum;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "job",
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    private List<JobPerformance> jobPerformance;

    public Job(String city, String category, String headline, String description, int sum, User user) {
        this.city = city;
        this.category = category;
        this.headline = headline;
        this.description = description;
        this.sum = sum;
        this.user = user;
    }

    public Job() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public User getUser() {
        return user;
    }
}
