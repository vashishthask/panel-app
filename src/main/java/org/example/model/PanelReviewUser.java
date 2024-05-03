package org.example.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class PanelReviewUser {
    @Id
    private String id;

    public PanelReviewUser(String id){
        this.id = id;
    }

    public PanelReviewUser(){
    }

    private String emailId;

    @OneToMany(mappedBy = "creator")
    private List<Contest> createdContests = new ArrayList<>();

    @ManyToMany
    @JoinTable(
        name = "contest_collaborators",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "contest_id"))
    private List<Contest> collaboratedContests = new ArrayList<>();

    public String getId() {
        return id;
    }

    public List<Contest> getCreatedContests() {
        return createdContests;
    }

    public void setCreatedContests(List<Contest> createdContests) {
        this.createdContests = createdContests;
    }

    public List<Contest> getCollaboratedContests() {
        return collaboratedContests;
    }

    public void setCollaboratedContests(List<Contest> collaboratedContests) {
        this.collaboratedContests = collaboratedContests;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    // getters and setters
}