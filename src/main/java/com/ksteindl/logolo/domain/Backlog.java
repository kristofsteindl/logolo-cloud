package com.ksteindl.logolo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Backlog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int taskSequence = 0;
    // TODO seems reduntant
    private String projectKey;

    // Each project has one backlog, and a backlog only relyes on a project
    // Lazy is more resource friendly
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="project_id", nullable = false)
    @JsonIgnore
    private Project project;

    // OneToMany with the Task-s
    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER, mappedBy = "backlog")
    private List<Task> tasks = new ArrayList<>();

    public Backlog() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTaskSequence() {
        return taskSequence;
    }

    public void setTaskSequence(int taskSequence) {
        this.taskSequence = taskSequence;
    }

    public String getProjectKey() {
        return projectKey;
    }

    public void setProjectKey(String projectKey) {
        this.projectKey = projectKey;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
