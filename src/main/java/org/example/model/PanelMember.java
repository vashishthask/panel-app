package org.example.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class PanelMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    private Contest contest;
    private String email;

    @ManyToMany(mappedBy = "panelMembers")
    private Set<PanelReviewSession> panelReviewSessions = new HashSet<>();

    public PanelMember(String name, String emailId) {
        this.name = name;
        this.email = emailId;
    }

    public PanelMember(){

    }

    // Constructors, getters, and setters


    public Set<PanelReviewSession> getPanelReviewSessions() {
        return panelReviewSessions;
    }

    public void setPanelReviewSessions(Set<PanelReviewSession> panelReviewSessions) {
        this.panelReviewSessions = panelReviewSessions;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Contest getContest() {
        return contest;
    }

    public void setContest(Contest contest) {
        this.contest = contest;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
