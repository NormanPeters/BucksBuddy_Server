package com.bucksbuddy.bucksbuddy.journey;

import com.bucksbuddy.bucksbuddy.expenditure.Expenditure;
import com.bucksbuddy.bucksbuddy.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "journeys")
public class Journey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;


    @Column(nullable = false)
    private String homeCurr;

    @Column(nullable = false)
    private String vacCurr;


    @Column(nullable = false)
    private int budget;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date startDate;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date endDate;

    @ManyToOne
    @JoinColumn(name = "user_uuid", referencedColumnName = "uuid", nullable = false)
    @JsonBackReference
    private User user;

    @OneToMany(mappedBy = "journey", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<Expenditure> expenditures;

    public Journey() {
    }

    public Journey(String name, User user, String HomeCurr, String VacCurr, int Budget, Date StartDate, Date EndDate) {
        this.name = name;
        this.user = user;
        this.homeCurr = HomeCurr;
        this.vacCurr = VacCurr;
        this.budget = Budget;
        this.startDate = StartDate;
        this.endDate = EndDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String destination) {
        this.name = destination;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getHomeCurr() {
        return homeCurr;
    }

    public void setHomeCurr(String homeCurr) {
        this.homeCurr = homeCurr;
    }

    public String getVacCurr() {
        return vacCurr;
    }

    public void setVacCurr(String vacCurr) {
        this.vacCurr = vacCurr;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public String getStartDate() {
        return startDate.toString();
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate.toString();
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
